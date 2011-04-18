(ns alfresco.core
    (:require (swank.swank)
              (clojure.main))
    (:import (it.sk.alfresco.clojure ContextHelper)
             (org.alfresco.service ServiceRegistry)))

(defonce *alfresco-services*
    (. (ContextHelper/getApplicationContext) getBean ServiceRegistry/SERVICE_REGISTRY))

(defn start-swank []
  (clojure.main/with-bindings
    (swank.swank/start-server "nul" :encoding "utf-8" :port 4005)))
