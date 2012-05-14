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
 
(ns alfresco.interop
  (:import [org.alfresco.repo.web.scripts.content StreamContent]
           [org.springframework.extensions.webscripts WebScriptRequest WebScriptResponse])
  (:require [alfresco.auth :as a]))

; Allow content to be streamed back bypassing permission checks
; TODO: delete?
(gen-class :name alfresco.interop.StreamContent
           :prefix "stream-"
           :extends org.alfresco.repo.web.scripts.content.StreamContent
           :exposes-methods {execute executeSuper})

(defn stream-execute
  [this ^WebScriptRequest req ^WebScriptResponse res]
  (a/as-admin (.executeSuper this req res)))
