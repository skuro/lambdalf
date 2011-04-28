(ns alfresco.core
    (:require (swank.swank)
              (clojure.main))
    (:import (it.sk.alfresco.clojure ContextHelper)
             (org.alfresco.service ServiceRegistry)))

(defrecord SimpleNode
  [id store])

(defprotocol Clojurify
  "Transform Java object into Clojure entities anc vice versa.
   Used to hide any Java to the final user"
  (j2c [this] "Creates a Clojure representation of the given object")
  (c2j [this] "Creates a Java representation of the given clojure entity"))

(defn get-bean
  "Yields the instance of a spring managed bean"
  [bean]
  (. (ContextHelper/getApplicationContext) getBean bean))

(defonce *alfresco-services*
  (get-bean  ServiceRegistry/SERVICE_REGISTRY))
