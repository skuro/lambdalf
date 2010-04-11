(ns it.sk.alfresco
;    (:require (swank.swank))
    (:import (it.sk.alfresco.clojure ContextHelper)
             (org.alfresco.service ServiceRegistry)))

;(defonce *services*
;    (. (ContextHelper/getApplicationContext) getBean ServiceRegistry/SERVICE_REGISTRY))

(defn start-swank []
    (clojure.main/with-bindings
        (swank.swank/start-server "/dev/null" :port 4005 :dont-close true)))