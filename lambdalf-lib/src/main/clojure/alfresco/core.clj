(ns alfresco.core
    (:import (it.sk.alfresco.clojure ContextHelper)
             (org.alfresco.service ServiceRegistry)))

(defn get-bean
  "Yields the instance of a spring managed bean"
  [bean]
  (. (ContextHelper/getApplicationContext) getBean bean))

(defn alfresco-services
  []
  (get-bean ServiceRegistry/SERVICE_REGISTRY))
