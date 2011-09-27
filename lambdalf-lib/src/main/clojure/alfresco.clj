(ns alfresco
  (:require [swank.swank]))

(def *swank-server* (atom nil))

(defn- switch
  "Starts up the swank server. Server shutdown is currently not supported."
  [current]
  (swank.swank/start-repl))

(defn start-swank []
  (swap! *swank-server* switch))

(gen-class :name alfresco.interop.ClojureInit
           :prefix "ci-"
           :methods [[setNamespaces [java.util.List] void]])

(defn load-ns
  "loads a given ns provided its dotted String representation"
  [^String ns]
  (let [s (.replaceAll ns "\\." "/")]
    (load (str "/" s))))

(defn ci-setNamespaces
  "Bootstraps the given namespaces"
  [this ns-list]
  (apply load-ns ns-list))
