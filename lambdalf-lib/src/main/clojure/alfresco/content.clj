(ns alfresco.content
  (:require [alfresco.core :as c])
  (:import [org.alfresco.model ContentModel]
           [java.io File ByteArrayInputStream]))

(defn content-service
  []
  (.getContentService (c/alfresco-services)))

(defprotocol Writer
  "Provide a way to write content from an arbitrary source"
  (write! [src node] "Writes the content coming from src into node"))

(defn- is
  "Retrieves an InputStream of the content for the provided node"
  [node]
  (.getContentInputStream (.getReader (content-service) node ContentModel/PROP_CONTENT)))

;; as seen on
;; https://groups.google.com/group/clojure/browse_thread/thread/e5fb47befe8b9199
;; TODO: make sure we're not breaking utf-8 support
(defn read!
  "Returns a lazy seq of the content of the provided node"
  [node]
  (let [noderef (c/c2j node)
        is (is noderef)]
    (map char (take-while #(not= -1 %) (repeatedly #(.read is))))))

(extend-protocol Writer

  String
  (write!
   [^String src node]
   (let [noderef (c/c2j node)
         w (.getWriter (content-service) noderef ContentModel/PROP_CONTENT true)]
     (.putContent w (ByteArrayInputStream. (.getBytes src "UTF-8")))))
  
  File
  (write!
   [^File src node]
   (let [noderef (c/c2j node)
         w (.getWriter (content-service) noderef ContentModel/PROP_CONTENT true)]
     (.putContent w src))))
