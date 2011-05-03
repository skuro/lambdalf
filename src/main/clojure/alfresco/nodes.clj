(ns alfresco.nodes
  (:require [alfresco.core :as c]
            [alfresco.model :as m]
            [alfresco.search :as s]
            [alfresco.auth :as a])
  (:import [alfresco.core SimpleNode]
           [org.alfresco.model ContentModel]
           [org.alfresco.service.cmr.repository ChildAssociationRef
            StoreRef
            NodeRef]))

(defonce *node-service* (.getNodeService c/*alfresco-services*))

(extend-protocol c/Clojurify
  NodeRef
  (j2c [^NodeRef n]
       (SimpleNode. (.getId n)
                    (str (.getStoreRef n))))
  (c2j [^NodeRef n]
       n)

  SimpleNode
  (j2c [^SimpleNode s] s)
  (c2j [^SimpleNode s] (NodeRef. (str (.store s) "/" (.id s)))))

(defonce *company-home*
  (a/as-admin (first (s/query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
                        "xpath"
                        "/app:company_home"))))
(defprotocol Node
  "Clojure friendly interface for an Alfresco node"
  (aspect? [this aspect] "True if aspect is applied to the the current ")
  (properties [this] "Provides all the metadata fields currently held by this node")
  (property [this prop] "Retrieves a property from a node")
  (set-properties! [this & prop-defs] "Set properties on a node")
  (aspects [this] "Provides all the aspect QNames of this node")
  (dir? [this] "True if the node is of type or subtype of cm:folder ")
  (create-child-assoc [this propmap] "Creates a new node. Accepts a map containing the following parameters:
   - parent : parent nodeRef
   - assoc-type : OPTIONAL - the association type. Defaults to cm:contains
   - assoc : OPTIONAL - the association name. Defaults to the new node cm:name
   - props : QName -> Serializable map of initial node metadata.
             It must at least contain an entry for cm:name
   - type : new node type
   It returns a map describing the new ChildAssociationRef")
  (children [this] "Returns a seq of the node direct children")
  (parent [this] "Gives the primary parent of node")
  (delete! [this] "Deletes a node")
  (add-aspect! [this & aspect-defs] "Adds aspects to a node. The aspects are provide as aspect-qname aspect-props couples")
  (del-aspect! [this aspect] "Removes an aspect from a node")
  (type-qname [this] "Returns the qname of the provided node's type")
  (set-type! [this type] "Sets the provided type onto the node. Yields nil"))

(defn defs2map
  "Creates a map out of a vararg parameter definition, e.g.:
  (defn foo [x & varargs]
    (let [as-map (defs2map varargs)]
      (clojure.pprint/pprint as-map))))
  Prints"
  [& varargs]
  (apply conj {} (for [[k v] (apply partition 2 varargs)]
     [k v])))

(extend-protocol Node
  SimpleNode

  (aspect? [node aspect] (.hasAspect *node-service* (c/c2j node) (m/qname aspect)))

  (properties [node] (into {} (let [prop-map (.getProperties *node-service* (c/c2j node))]
                             (zipmap (map m/qname-str (keys prop-map))
                                     (vals prop-map)))))
  (aspects [node] (into #{} (doall (map m/qname-str (.getAspects *node-service* (c/c2j node))))))
  
  (dir? [node] (= (m/qname "cm:folder" (.getType *node-service* (c/c2j node)))))

  (create-child-assoc [node {:keys [assoc-type assoc props type]}]
    (let [assoc-qname (if assoc-type
                        (m/qname assoc-type)
                        (m/qname "cm:contains"))
          assoc-name (if assoc
                       (m/qname assoc)
                       (m/qname (str "cm:" (props ContentModel/PROP_NAME))))
          ^ChildAssociationRef assoc-ref (.createNode *node-service*
                                                      (c/c2j node)
                                                      assoc-qname
                                                      assoc-name
                                                      type
                                                      props)]
  
      {:type assoc-qname
       :name assoc-name
       :parent (c/j2c node)
       :child (c/j2c (.getChildRef assoc-ref))}))

  (children
   [node]
   (into #{} (doall
              (map #(c/j2c (.getChildRef %))
                   (.getChildAssocs *node-service* (c/c2j node))))))

  (parent
   [node]
   (c/j2c (.getParentRef
         (.getPrimaryParent *node-service* (c/c2j node)))))

  (delete!
   [node]
   (.deleteNode *node-service* (c/c2j node)))

  (add-aspect!
   [node & aspect-defs]
   {:pre [(even? (count aspect-defs))]}
   (for [[aspect props] (partition 2 aspect-defs)]
     (.addAspect *node-service* (c/c2j node) (m/qname aspect) props)))

  (del-aspect!
   [node aspect]
   (.removeAspect *node-service* (c/c2j node) (m/qname aspect)))

  (property
   [node prop]
   (.getProperty *node-service* (c/c2j node) (m/qname prop)))



  (set-properties!
   [node & prop-defs]
   {:pre [(or (nil? prop-defs) (even? (count prop-defs)))]}
   (let [prop-map (defs2map prop-defs)
         qnamed-map (zipmap (map m/qname (keys prop-map))
                            (vals prop-map))]
     (.setProperties *node-service* (c/c2j node) qnamed-map)))

  (type-qname
    [node]
    (m/qname (.getType *node-service* (c/c2j node))))

  (set-type!
   [type node]
   (.setType *node-service* (c/c2j node) (m/qname type))))
