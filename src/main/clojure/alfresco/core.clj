(ns alfresco.core
    (:require (swank.swank)
              (clojure.main))
    (:import (it.sk.alfresco.clojure ContextHelper)
             (org.alfresco.service ServiceRegistry)))

(defonce *alfresco-services*
    (. (ContextHelper/getApplicationContext) getBean ServiceRegistry/SERVICE_REGISTRY))
