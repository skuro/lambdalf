;
; Copyright (C) 2011,2012 Carlo Sciolla, Peter Monks
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
 
(ns alfresco.transact
  (:require [alfresco.core :as c]
            [alfresco.auth :as a])
  (:import [org.alfresco.repo.transaction RetryingTransactionHelper$RetryingTransactionCallback]
           [javax.transaction Status]))

(defn transaction-service
  "The retrying transaction helper service bean."
  []
  (.getTransactionService (c/alfresco-services)))

(defmacro in-tx
  "Runs the given forms within a read/write Alfresco transaction, automatically completing it (committing or rolling back), retrying and/or cleaning up as required. To force a rollback, simply throw an exception."
  [& body]
  `(let [cb# (reify RetryingTransactionHelper$RetryingTransactionCallback
               (~'execute [~'this] ~@body))]
     (-> (transaction-service)
         (.getRetryingTransactionHelper)
         (.doInTransaction cb# false false))))  ; R/W transaction and does not require a new transaction
         
(defmacro in-ro-tx
  "Runs the given forms within a read/only Alfresco transaction."
  [& body]
  `(let [cb# (reify RetryingTransactionHelper$RetryingTransactionCallback
               (~'execute [~'this] ~@body))]
     (-> (transaction-service)
         (.getRetryingTransactionHelper)
         (.doInTransaction cb# true false))))  ; R/O transaction and does not require a new transaction

(defmacro in-tx-as
  "Runs the given forms as the given user within a read/write Alfresco transaction."
  [user & body]
  `(alfresco.auth/run-as ~user (alfresco.transact/in-tx ~@body)))
  
(defmacro in-ro-tx-as
  "Runs the given forms as the given user within a read/only Alfresco transaction."
  [user & body]
  `(alfresco.auth/run-as ~user (alfresco.transact/in-ro-tx ~@body)))
