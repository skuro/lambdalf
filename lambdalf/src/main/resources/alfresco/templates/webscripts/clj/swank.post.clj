;; ns forms are currently useless

(require '[spring.surf.webscript :as w]
         '[alfresco :as a])

(import '[spring.surf.webscript WebScript])

(deftype SwankWebScript
  []
  WebScript
  (run [this in out model]
       (w/return model {:swank (a/start-swank)})))

(SwankWebScript.)
