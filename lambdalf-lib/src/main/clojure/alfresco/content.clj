;
; Copyright (C) 2011,2012 Carlo Sciolla
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
  (let [is (is node)]
    (map char (take-while #(not= -1 %) (repeatedly #(.read is))))))

(extend-protocol Writer

  String
  (write!
   [^String src node]
    (let [noderef node
         w (.getWriter (content-service) noderef ContentModel/PROP_CONTENT true)]
     (.putContent w (ByteArrayInputStream. (.getBytes src "UTF-8")))))
  
  File
  (write!
   [^File src node]
    (let [noderef node
         w (.getWriter (content-service) noderef ContentModel/PROP_CONTENT true)]
     (.putContent w src))))
