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
    (exec impl action node)))

(defn act-addParameterDefinitions
  "Passes the ball to the concrete Action"
  [this ^List params]
  (map #(.add params %) (needs-params (.concrete this))))
