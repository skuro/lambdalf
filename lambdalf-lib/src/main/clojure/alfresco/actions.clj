(ns alfresco.actions
  (:require [clojure.string :as str]
            [alfresco.core :as c]
            [alfresco.model :as m])
  (:import [org.alfresco.repo.action ParameterDefinitionImpl]
           [org.alfresco.service.cmr.repository NodeRef]
           [java.util List]))

(defprotocol Action
  "All the abtract methods an Action needs to define in Alfresco"
  (needs-params [this] "Returns a seq of parameters this Action understands")
  (exec [this action node] "Call the concrete action implementation, providing an action descriptor and the actioned upone node"))

(defn param
  "Constructs a Java representation of an action parameter"
  [{:keys [name type mandatory multi label]}]
  (ParameterDefinitionImpl. name
                            (m/qname type)
                            mandatory
                            label
                            multi))

;; Interop: allows the Clojure action to be registered as a Spring bean
(gen-class :name alfresco.actions.ActionRegistrar
           :extends  org.alfresco.repo.action.executer.ActionExecuterAbstractBase
           :prefix "act-"
           :state concrete
           :constructors {[String] []} ; FQN of the concrete Action
           :init clojure-init)         ; base class already has a 'init' method

(defn act-clojure-init
  "Stores an instance of the supplied concrete Action as a state"
  [^String impl]
  [[] (eval (list (symbol (str impl "."))))]) ; dirty reflection hack

(defn act-executeImpl
  "Passes the ball to the concrete Action"
  [this action node]
  (let [impl (.concrete this)]
    (exec impl action (c/j2c node))))

(defn act-addParameterDefinitions
  "Passes the ball to the concrete Action"
  [this ^List params]
  (map #(.add params %) (needs-params (.concrete this))))
