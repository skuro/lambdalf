(ns lambdalf.webscript.nrepl
  (:require [spring.surf.webscript :as w]
            [alfresco :as a])
  (:import [spring.surf.webscript WebScript]))

(deftype NreplWebScript
  []
  WebScript
  (run [this in out model]
    (a/start-nrepl)
    (w/return model {:nrepl "OK"
                     :port "7888"})))

(NreplWebScript.)
