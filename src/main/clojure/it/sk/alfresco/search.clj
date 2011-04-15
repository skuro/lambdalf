(ns it.sk.alfresco.search
	(:require [it.sk.alfresco.core :as c])
	(:import [it.sk.alfresco.nodes Node SimpleNode]))

(defonce *search-service* (.getSearchService c/*alfresco-services*))

; TODO: broken, what's bind actually?
(defn query
	"Search in the Alfresco repository"
	([q]
		(query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE StoreRef/LANGUAGE_LUCENE q))
	([store lang q]
		(with-open [rs (.query *search-service* store lang q)]
			(doall (map #(bind (SimpleNode.) %) (.getNoderefs rs))))))
