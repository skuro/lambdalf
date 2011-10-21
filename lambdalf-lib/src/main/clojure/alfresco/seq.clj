(ns alfresco.seq
  (:require [alfresco.nodes :as n]
            [alfresco.model :as m]
            [alfresco.auth :as a]))

(defn to-seq
  "Returns a lazy seq of the nodes representing the repository branch
having the given root. Uses the currently authenticated user to realize
the seq." 
  [root]
  ;; store the currently authenticated user, needed by the following closures
  (let [user (a/whoami)
        branch? (fn [x] (a/run-as user
                                 (m/qname-isa? (n/type-qname x)
                                               (m/qname "cm:folder"))))
        children (fn [x] (a/run-as user
                                  (n/children x)))]
    (tree-seq branch? children root)))
