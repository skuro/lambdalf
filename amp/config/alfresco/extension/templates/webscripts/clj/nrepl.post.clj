(ns lambdalf.webscript.nrepl
  (:require [spring.surf.webscript :as w]
            [alfresco :as a])
  (:import [spring.surf.webscript WebScript]))

(deftype NreplStartWebScript
  []
  WebScript
  (run [this in out model]
    (w/return model {:status "started"
                     :port   (a/start-nrepl!)})))

(NreplStartWebScript.)
