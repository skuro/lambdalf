(ns it.sk.alfresco.nodes
  (:require [it.sk.alfresco.core :as c]
            [it.sk.alfresco.model :as m]))

(defonce *node-service* (.getNodeService c/*alfresco-services*))

(defprotocol Node
  "Clojure friendly interface for an Alfresco node"
  (aspect? [this aspect] "True if aspect is applied to the the current node")

  ;; TODO: access binary content)

(defrecord SimpleNode
  [ref props aspects]

  Node
  (aspect? [_ aspect] (.hasAspect *node-service* ref (m/qname aspect))))
