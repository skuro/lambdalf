(ns alfresco.search

  (:require [alfresco.core :as c]
            [alfresco.nodes :as n]
            [alfresco.auth :as a])

  (:import [org.alfresco.service.cmr.repository StoreRef]
           [org.alfresco.service.cmr.search SearchService]
           [alfresco.nodes SimpleNode]))

(defonce *search-service* (.getSearchService c/*alfresco-services*))

(defn query
  "Returns all the results of a search"
  ([q]
     (query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
            SearchService/LANGUAGE_LUCENE q))
  ([store lang q]
     (with-open [rs (.query *search-service* store lang q)]
       (doall (map #(n/SimpleNode. %) (.getNodeRefs rs))))))
