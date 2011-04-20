(ns alfresco)

(defn start-swank []
  (clojure.main/with-bindings
    (swank.swank/start-server "nul" :encoding "utf-8" :port 4005)))
