(ns alfresco.zip
  (:require [clojure.zip :as z]
             [alfresco.model :as m]
             [alfresco.nodes :as n]))

(defn branch?
  "Verifies if the current location can have children.
   While cm:content can have children, this is currently limited to cm:folder"
  [node]
  (m/qname-isa? "cm:folder" (n/type-qname node)))

(defn make-node
  "Associates the given children with the given parent node"
  [node children]
  (apply n/create-child-assoc node children)
  node)

(defn repo-zip
  "Creates a zipper with the given node as root"
  [root]
  (let [children #(seq (n/children %))]
    (z/zipper branch? children make-node root)))
