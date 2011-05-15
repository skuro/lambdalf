(ns alfresco.actions
  (:require [alfresco.core :as c])
  (:import [org.alfresco.service.cmr.action Action]
           [org.alfresco.service.cmr.repository NodeRef]))

(gen-class :name alfresco.actions.Action
           :extends  org.alfresco.repo.action.executer.ActionExecuterAbstractBase
           :prefix "act-"
           :constructors {[String String] []} ; ns + fn name
           :init init)

(defn act-init
  [^String namespace ^String func]
  (require (symbol namespace))
  [[] (symbol (str namespace "/" func))])

(defn act-execute-impl
  [this action node]
  (let [func (.state this)]
    (func action (c/j2c node))))
