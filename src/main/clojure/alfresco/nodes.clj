(ns alfresco.nodes
  (:require [alfresco.core :as c]
            [alfresco.model :as m]
            [alfresco.search :as s]
            [alfresco.auth :as a])
  (:import [org.alfresco.model ContentModel]
           [org.alfresco.service.cmr.repository ChildAssociationRef StoreRef]))

(defonce *node-service* (.getNodeService c/*alfresco-services*))

(defprotocol Node
  "Clojure friendly interface for an Alfresco node"
  (aspect? [this aspect] "True if aspect is applied to the the current ")
  (properties [this] "Provides all the metadata fields currently held by this node")
  (aspects [this] "Provides all the aspect QNames of this node")
  (dir? [this] "True if the node is of type or subtype of cm:folder "))

(defrecord SimpleNode
  [ref]

  Node
  (aspect? [_ aspect] (.hasAspect *node-service* ref (m/qname aspect)))
  (properties [_] (into {} (let [prop-map (.getProperties *node-service* ref)]
                             (zipmap (map m/qname-str (keys prop-map))
                                     (vals prop-map)))))
  (aspects [_] (into #{} (doall (map m/qname-str (.getAspects *node-service* ref)))))
  (dir? [_] (= (m/qname "cm:folder" (.getType *node-service* ref)))))

(defonce *company-home*
  (a/as-admin (s/query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
                        "xpath"
                        "/app:company_home")))

(defn create-child-assoc
  "Creates a new node. Accepts a map containing the following parameters:
   - parent : parent nodeRef
   - assoc-type : OPTIONAL - the association type. Defaults to cm:contains
   - assoc : OPTIONAL - the association name. Defaults to the new node cm:name
   - props : QName -> Serializable map of initial node metadata.
             It must at least contain an entry for cm:name
   - type : new node type
   It returns a map describing the new ChildAssociationRef"
  [{:keys [parent assoc-type assoc props type]}]
  (let [assoc-qname (if assoc-type
                      (m/qname assoc-type)
                      (m/qname "cm:contains"))
        assoc-name (if assoc
                    (m/qname assoc)
                    (m/qname (str "cm:" (props ContentModel/PROP_NAME))))
        ^ChildAssociationRef assoc-ref (.createNode *node-service*
                                                    parent
                                                    assoc-qname
                                                    assoc-name
                                                    type
                                                    props)]
  
    {:type assoc-qname
     :name assoc-name
     :parent parent
     :child (SimpleNode. (.getChildRef assoc-ref))}))

(defn children
  "Returns a seq of the node direct children"
  [node]
  (into #{} (doall
             (map #(SimpleNode. (.getChildRef %))
                  (.getChildAssocs *node-service* (.ref node))))))

(defn parent
  "Gives the primary parent of node"
  [node]
  (SimpleNode. (.getParentRef
                (.getPrimaryParent *node-service* (.ref node)))))

(defn delete!
  "Deletes a node"
  [node]
  (let [ref (if (isa? SimpleNode node)
              (.ref node)
              node)]
    (.deleteNode *node-service* node)))

(defn add-aspect!
  "Adds aspects to a node. The aspects are provide as aspect-qname aspect-props couples."
  [node & aspect-defs]
  {:pre [(even? (count aspect-defs))]}
  (for [[aspect props] (partition 2 aspect-defs)]
    (.addAspect *node-service* (m/qname aspect) props)))

(defn del-aspect!
  "Removes an aspect from a node"
  [aspect node]
  (.removeAspect *node-service* (.ref node) (m/qname aspect)))

(defn prop
  "Returns the value of a propery for the given node"
  [property node]
  (.getProperty *node-service* (.ref node) (m/qname property)))

(defn defs2map
  "Creates a map out of a vararg parameter definition, e.g.:
  (defn foo [x & varargs]
    (let [as-map (defs2map varargs)]
      (clojure.pprint/pprint as-map))))
  Prints"
  [& varargs]
  (apply conj {} (for [[k v] (apply partition 2 varargs)]
     [k v])))

(defn set-properties!
   "Sets the given metadata properties onto a node"
   ([node prop-map]
      (let [qnamed-map (zipmap (map m/qname (keys prop-map))
                               (vals prop-map))]
        (.setProperties *node-service* node qnamed-map)))
   ([node prop val]
      (set-properties! node {(m/qname prop) val}))
   ([node prop val & prop-defs]
      {:pre [(even? (count prop-defs))]}
      (let [prop-map (assoc (defs2map prop-defs) prop val)]
        (set-properties! node prop-map))))

(defn type-qname
  "Returns the qname of the provided node's type"
  [node]
  (m/qname (.getType *node-service* (.ref node))))

(defn set-type!
  "Sets node to be of a specific type. Yields nil."
  [type node]
  (.setType *node-service* (.ref node) (m/qname type)))
