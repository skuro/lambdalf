(ns alfresco
  (:require [swank.swank]))

(def *swank-server* (atom nil))

(defn- switch
  "Starts up the swank server. Server shutdown is currently not supported."
  [current]
  (swank.swank/start-repl))

(defn start-swank []
  (swap! *swank-server* switch))
