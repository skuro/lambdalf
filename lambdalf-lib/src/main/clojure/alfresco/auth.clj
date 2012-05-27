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
 
(ns alfresco.auth
  (:import [org.alfresco.repo.security.authentication
            AuthenticationUtil
            AuthenticationUtil$RunAsWork]))

; TODO memoize the result
(defn admin
  "Returns the current Administrator user name."
  []
  (AuthenticationUtil/getAdminUserName))

(defmacro run-as
  "Runs the provided form while impersonating the given user.
  WARNING: the form you pass to this macro should not return a lazy
  sequence that include calls to repository functions, as when/if they are
  eventually evaluated, the calls to those functions will occur outside the
  scope of the authorisation context (which is probably not what's intended)."
  [user & f]
  `(let [work# (reify AuthenticationUtil$RunAsWork
                     (~'doWork [~'this]
                               ~@f))]
     (AuthenticationUtil/runAs work# ~user)))

(defmacro run-as-fn
  "Returns a closure which will run with the provided user privileges"
  [user & f]
  `(fn []
     (run-as ~user
             ~@f)))

(defmacro as-admin
  "Runs the provided form as admin.
  WARNING: the form you pass to this macro should not return a lazy
  sequence that include calls to repository functions, as when/if they are
  eventually evaluated, the calls to those functions will occur outside the
  scope of the authorisation context (which is probably not what's intended)."
  [& f]
  `(run-as (admin) ~@f))

(defn whoami
  "Returns the currently valid user name"
  []
  (AuthenticationUtil/getRunAsUser))
