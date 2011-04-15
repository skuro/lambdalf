(ns it.sk.alfresco.auth)

; TODO: broken
(defmacro run-as
  "Runs the provided form while impersonating the given user"
  [user f]
  (let [work (reify AuthenticationUtil$RunAsWork (doWork f))]
    `(.runAs AuthenticationUtil ~work ~user)))