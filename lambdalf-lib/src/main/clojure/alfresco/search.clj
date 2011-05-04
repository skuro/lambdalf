(ns alfresco.search

  (:require [alfresco.core :as c]
            [alfresco.auth :as a])

  (:import [org.alfresco.service.cmr.repository StoreRef]
           [org.alfresco.service.cmr.search SearchService]
           [alfresco.core SimpleNode]))

(defn search-service
  []
  (.getSearchService (c/alfresco-services)))

(defn query
  "Returns all the results of a search"
  ([q]
     (query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
            SearchService/LANGUAGE_LUCENE q))
  ([store lang q]
     (with-open [rs (.query (search-service) store lang q)]
       (doall (map c/j2c (.getNodeRefs rs))))))
