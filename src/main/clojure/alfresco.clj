(ns alfresco)

(defn start-swank []
  (clojure.main/with-bindings
    (swank.swank/start-server "/dev/null" :encoding "utf-8" :port 4005)))
