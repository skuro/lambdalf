(ns lambdalf.webscript.nrepl
  (:require [spring.surf.webscript :as w]
            [alfresco :as a])
  (:import [spring.surf.webscript WebScript]))

(deftype NreplStopWebScript
  []
  WebScript
  (run [this in out model]
    (a/stop-nrepl!)
    (w/return model {:status "stopped"})))

(NreplStopWebScript.)
