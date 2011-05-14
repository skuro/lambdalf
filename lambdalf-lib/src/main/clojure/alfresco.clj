(ns alfresco
  (:require [swank.swank]))

(def *swank-server* (atom nil))

(defn- switch
  "Currently only starts up the server. TODO: stop it in case it's already running"
  [current]
  (swank.swank/start-repl))

(defn start-swank
  []
  (swap! *swank-server* switch))
