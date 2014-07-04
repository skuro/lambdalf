(ns lambdalf.webscript.nrepl
  (:require [spring.surf.webscript :as w]
            [alfresco :as a])
  (:import [spring.surf.webscript WebScript]))

(deftype NreplStatusWebScript
  []
  WebScript
  (run [this in out model]
    (w/return model {:status (if (a/nrepl-running?) "Started" "Stopped")})))

(NreplStatusWebScript.)
