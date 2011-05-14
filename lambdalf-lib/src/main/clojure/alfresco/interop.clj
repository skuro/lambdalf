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
