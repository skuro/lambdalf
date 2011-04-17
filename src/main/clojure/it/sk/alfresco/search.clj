(ns it.sk.alfresco.search

  (:require [it.sk.alfresco.core :as c]
            [it.sk.alfresco.nodes :as n])

  (:import [org.alfresco.service.cmr.repository StoreRef]
           [org.alfresco.service.cmr.search SearchService]
           [it.sk.alfresco.nodes SimpleNode]))

(defonce *search-service* (.getSearchService c/*alfresco-services*))

(defn query
	"Search in the Alfresco repository"
	([q]
           (query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
                  SearchService/LANGUAGE_LUCENE q))
	([store lang q]
           (with-open [rs (.query *search-service* store lang q)]
             (doall (map #(n/SimpleNode. %) (.getNodeRefs rs))))))
