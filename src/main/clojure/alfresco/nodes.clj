(ns alfresco.nodes
  (:require [alfresco.core :as c]
            [alfresco.model :as m]))

(defonce *node-service* (.getNodeService c/*alfresco-services*))

;; TODO: access binary content)
(defprotocol Node
  "Clojure friendly interface for an Alfresco node"
  (aspect? [this aspect] "True if aspect is applied to the the current ")
  (properties [this] "Provides all the metadata fields currently held by this node")
  (aspects [this] "Provides all the aspect QNames of this node"))

(defrecord SimpleNode
  [ref]

  Node
  (aspect? [_ aspect] (.hasAspect *node-service* ref (m/qname aspect)))
  (properties [_] (.getProperties *node-service* ref))
  (aspects [_] (.getAspects *node-service* ref)))

;; TODO: create, update, delete, move, list children
