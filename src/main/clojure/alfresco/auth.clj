(ns alfresco.auth
  (:import [org.alfresco.repo.security.authentication
            AuthenticationUtil
            AuthenticationUtil$RunAsWork]))

(defonce *admin* (AuthenticationUtil/getAdminUserName))

(defmacro run-as
  "Runs the provided form while impersonating the given user"
  [user f]
  `(let [work# (reify AuthenticationUtil$RunAsWork
                     (~'doWork [~'this]
                               ~f))]
     (AuthenticationUtil/runAs work# ~user)))

(defmacro as-admin
  "Runs the provided form as admin"
  [f]
  `(run-as *admin* ~f))

(defn whoami
  "Returns the currently valid user name"
  []
  (AuthenticationUtil/getRunAsUser))
