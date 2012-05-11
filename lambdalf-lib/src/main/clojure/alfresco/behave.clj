(ns alfresco.behave
  (:require [alfresco.core :as c]
            [alfresco.model :as m])
  (:import [org.alfresco.repo.policy JavaBehaviour Behaviour$NotificationFrequency]
           [org.alfresco.repo.node
            NodeServicePolicies$OnAddAspectPolicy]))

;; no static String to use here, unfortunately
(defn policy-component
  []
  (c/get-bean "policyComponent"))

(defmacro on-add-aspect!
  "Registers a clojure behavior that will be called when an aspect with
QName qname. The provided form f must accept two input parameters: a noderef and a qname"
  [qname f]
  `(let [p# (reify NodeServicePolicies$OnAddAspectPolicy
                   (~'onAddAspect [~'this ~'node-in ~'qname-in]
                     (~f ~'node-in (m/qname-str ~'qname-in))))
         b# (JavaBehaviour. p#
                            "onAddAspect"
                            Behaviour$NotificationFrequency/TRANSACTION_COMMIT)]
     (.bindClassBehaviour (policy-component)
                         NodeServicePolicies$OnAddAspectPolicy/QNAME
                         (m/qname ~qname)
                         b#)))
