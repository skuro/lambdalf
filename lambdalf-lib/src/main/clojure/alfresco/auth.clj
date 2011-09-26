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
  "Runs the provided form while impersonating the given user"
  [user f]
  `(let [work# (reify AuthenticationUtil$RunAsWork
                     (~'doWork [~'this]
                               ~f))]
     (AuthenticationUtil/runAs work# ~user)))

(defmacro run-as-fn
  "Returns a closure which will run with the provided user privileges"
  [user f]
  `(fn []
     (run-as ~user
             ~f)))

(defmacro as-admin
  "Runs the provided form as admin"
  [f]
  `(run-as (admin) ~f))

(defn whoami
  "Returns the currently valid user name"
  []
  (AuthenticationUtil/getRunAsUser))
