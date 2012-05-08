(ns alfresco
  (:require [swank.swank]))
(use '[clojure.tools.nrepl.server :only (start-server stop-server)])

; Starts the Swank server
(def ^:dynamic *swank-server* (atom nil))

(defn- switch-swank
  "Starts up the swank server. Server shutdown is currently not supported."
  [current]
  (swank.swank/start-repl))

(defn start-swank []
  (swap! *swank-server* switch-swank))

; Starts the NREPL server
(def ^:dynamic *nrepl-server* (atom nil))

(defn- switch-nrepl
  "Starts up the NREPL server. Server shutdown is currently not supported."
  [current]
  (start-server :port 7888))

(defn start-nrepl []
  (swap! *nrepl-server* switch-nrepl))

; Other Clojure gobbledygook
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
