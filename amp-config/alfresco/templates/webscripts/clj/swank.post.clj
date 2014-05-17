(ns lambdalf.webscript.swank
  (:require [spring.surf.webscript :as w]
            [alfresco :as a])
  (:import [spring.surf.webscript WebScript]))

(deftype SwankWebScript
  []
  WebScript
  (run [this in out model]
    (a/start-swank)
    (w/return model {:swank "OK"
                     :port "4005"})))

(SwankWebScript.)
