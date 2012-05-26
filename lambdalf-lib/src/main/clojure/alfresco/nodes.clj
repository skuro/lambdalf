;
; Copyright (C) 2011,2012 Carlo Sciolla
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
; 
;     http://www.apache.org/licenses/LICENSE-2.0
;  
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
 
(ns alfresco.nodes
  (:require [alfresco.core :as c]
            [alfresco.model :as m]
            [alfresco.search :as s]
            [alfresco.auth :as a])
  (:import [org.alfresco.model ContentModel]
           [org.alfresco.service.cmr.repository ChildAssociationRef
            NodeService
            StoreRef
            NodeRef]))

(defn ^NodeService node-service
  []
  (.getNodeService (c/alfresco-services)))

(defn company-home
  []
  (first (s/query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
                  "xpath"
                  "/app:company_home")))
(defprotocol Node
  "Clojure friendly interface for an Alfresco node"
  (aspect? [this aspect] "True if aspect is applied to the the current ")
  (properties [this] "Provides all the metadata fields currently held by this node")
  (property [this prop] "Retrieves a property from a node")
  (set-properties! [this & prop-defs] "Set properties on a node")
  (aspects [this] "Provides all the aspect QNames of this node")
  (dir? [this] "True if the node is of type or subtype of cm:folder ")
  (create-child-assoc [this propmap] "Creates a new node. Accepts a map containing the following parameters:
   - assoc-type : OPTIONAL - the association type. Defaults to cm:contains
   - assoc : OPTIONAL - the association name. Defaults to the new node cm:name
   - props : QName -> Serializable map of initial node metadata.
             It must at least contain an entry for cm:name
   - type : new node type
   It returns a map describing the new ChildAssociationRef")
  (children [this] "Returns a seq of the node direct children")
  (parent [this] "Gives the primary parent of node")
  (delete! [this] "Deletes a node")
  (add-aspect! [this aspect props] "Adds an aspect to a node.")
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
  NodeRef

  (aspect? [node aspect] (.hasAspect (node-service) node (m/qname aspect)))

  (properties [node] (into {} (.getProperties (node-service) node)))
  
  (aspects [node] (into #{} (doall (map m/qname-str (.getAspects (node-service) node)))))
  
  (dir? [node] (= (m/qname :cm/folder) (.getType (node-service) node)))

  (create-child-assoc
    [node {:keys [assoc-type assoc props type]}]
    (let [props* (zipmap (map m/qname (keys props))
                         (vals props))
          assoc-qname (if assoc-type
                        (m/qname assoc-type)
                        (m/qname :cm/contains))
          assoc-name (if assoc
                       (m/qname assoc)
                       (m/qname (keyword "cm" (props* ContentModel/PROP_NAME))))
          ^ChildAssociationRef assoc-ref (.createNode (node-service)
                                                      node
                                                      assoc-qname
                                                      assoc-name
                                                      (m/qname type)
                                                      props*)]
     
      {:type assoc-qname
       :name assoc-name
       :parent node
       :child (.getChildRef assoc-ref)}))
  
  (children
   [node]
   (into #{} (doall
              (map #(.getChildRef %)
                   (.getChildAssocs (node-service) node)))))

  (parent
   [node]
    (.getParentRef
     (.getPrimaryParent (node-service) node)))

  (delete!
   [node]
    (.deleteNode (node-service) node))

  (add-aspect!
   [node aspect props]
   (.addAspect (node-service)
               node
               (m/qname aspect)
               (zipmap (map m/qname (keys props))
                       (vals props))))  

  (del-aspect!
   [node aspect]
    (.removeAspect (node-service) node (m/qname aspect)))

  (property
   [node prop]
    (.getProperty (node-service) node (m/qname prop)))

  (set-properties!
   [node & prop-defs]
   {:pre [(or (nil? prop-defs) (even? (count prop-defs)))]}
    (let [prop-map (defs2map prop-defs)
          qnamed-map (zipmap (map m/qname (keys prop-map))
                            (vals prop-map))]
     (.setProperties (node-service) node qnamed-map)))

  (type-qname
    [node]
    (m/qname (.getType (node-service) node)))

  (set-type!
   [type node]
    (.setType (node-service) node (m/qname type))))
