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
  (:require [clojure.tools.nrepl.server :as nrepl]))

; Hold a reference to the NREPL server
(def ^:private *nrepl-server* (atom nil))

(defn nrepl-running?
  "Is the nREPL server running?"
  ([] (nrepl-running? @*nrepl-server*))
  ([nrepl-server]
    (not (nil? nrepl-server))))

(defn stop-nrepl!
  "Stops the nREPL server. Returns nil."
  ([] (stop-nrepl! @*nrepl-server*))
  ([nrepl-server]
    (if (nrepl-running?)
      (nrepl/stop-server nrepl-server))
    (reset! *nrepl-server* nil)
    nil))

(defn- restart-nrepl!
  "Restarts (stops if necessary, then starts) an nREPL server on the given port, returning the server object.
   Intended to be called by clojure.core/swap!."
  ([nrepl-server] (restart-nrepl! nrepl-server 7888))
  ([nrepl-server port]
    (if (nrepl-running? nrepl-server)
      (stop-nrepl! nrepl-server))
    (nrepl/start-server :port port)))

(defn start-nrepl!
  "Starts up an nREPL server, returning the port it's running on."
  ([] (start-nrepl! 7888))
  ([port]
    (swap! *nrepl-server* restart-nrepl! port)
    port))

; Other Clojure gobbledygook
(gen-class :name    alfresco.interop.ClojureInit
           :prefix  "ci-"
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
