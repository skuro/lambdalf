;
; Copyright (C) 2011,2012 Carlo Sciolla, Peter Monks
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

(ns alfresco
;  (:require [swank.swank])
  (:use [clojure.tools.nrepl.server :only (start-server stop-server)]))

; Hold a reference to the Swank server
; Note: swank has been discarded by its author and is not compatible with Clojure 1.5
;(def ^:dynamic *swank-server* (atom nil))

; Hold a reference to the NREPL server
(def ^:dynamic *nrepl-server* (atom nil))

;(defn stop-swank []
;  (swank.swank/stop-server))

(defn stop-nrepl
  "Stops the NREPL server. Waits by default 5s, provide a negative timeout to wait indefinitely."
  ([] (stop-nrepl @*nrepl-server*))
  ([server] (stop-nrepl server 5000))
  ([server timeout]
    (if (> 0 timeout)
      (await (stop-server server)))
      (await-for timeout (stop-server server))))

;(defn- switch-swank
;  "Starts up the swank server. Server shutdown is currently not supported."
;  [current]
;  (when current (stop-swank))
;  (swank.swank/start-repl))

(defn- switch-nrepl
  "Starts up the NREPL server. Server shutdown is currently not supported."
  [current]
  (when current (stop-nrepl current))
  (start-server :port 7888))

;(defn start-swank []
;  (swap! *swank-server* switch-swank))

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
