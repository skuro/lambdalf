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
 
(ns alfresco.search
  (:require [alfresco.core :as c]
            [alfresco.auth :as a])

  (:import [org.alfresco.service.cmr.repository StoreRef]
           [org.alfresco.service.cmr.search SearchService]))

(defn ^SearchService search-service
  []
  (.getSearchService (c/alfresco-services)))

(defn query
  "Returns all the results of a search"
  ([q]
     (query StoreRef/STORE_REF_WORKSPACE_SPACESSTORE
            SearchService/LANGUAGE_LUCENE q))
  ([store lang q]
     (with-open [rs (.query (search-service) store lang q)]
       (.getNodeRefs rs))))
