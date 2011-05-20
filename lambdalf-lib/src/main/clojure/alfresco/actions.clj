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

(defrecord Param
  [name type multi label mandatory])

(extend-protocol c/Clojurify
  Param
  (c2j [^Param p]
       (ParameterDefinitionImpl. (:name p)
                                 (m/qname (:type p))
                                 (:mandatory p)
                                 (:label p))))

(defn- translate-params
  "Handles the Clojure / Java impedance mismatch when it comes to action parameters"
  [new-params ^List current-params]
  (let [java-params (map c/c2j new-params)]
    (map #(.add current-params %) java-params)))

;; Interop: allows the Clojure action to be registered as a Spring bean
(gen-class :name alfresco.actions.ActionRegistrar
           :extends  org.alfresco.repo.action.executer.ActionExecuterAbstractBase
           :prefix "act-"
           :constructors {[String] []} ; FQN of the concrete Action
           :init clojure-init) ; base class already has a 'init' method

(defn act-clojure-init
  "Stores an instance of the supplied concrete Action as a state"
  [^String impl]
  (let [concrete (symbol impl)]
    [[] (eval (list (symbol (str concrete "."))))])) ;; dirty reflection hack

(defn act-executeImpl
  "Passes the ball to the concrete Action"
  [this action node]
  (let [impl (.state this)]
    (exec impl action (c/j2c node))))

(defn act-addParameterDefinitions
  "Passes the ball to the concrete Action"
  [this params]
  (println "test"))
