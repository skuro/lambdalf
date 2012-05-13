; Copyright (c) Peter Monks 2012
;
; LICENSE - TBD (see https://github.com/skuro/lambdalf/issues/21)

(ns alfresco.transactions
  (:require [alfresco.core :as c]
            [alfresco.auth :as a])
  (:import [org.alfresco.repo.transaction
              RetryingTransactionHelper
              RetryingTransactionHelper$RetryingTransactionCallback]))

(defn- retrying-txn-helper
  "The retrying transaction helper service bean."
  []
  (.getRetryingTransactionHelper (c/alfresco-services)))


(defmacro in-tx
  "Runs the given function in an Alfresco transaction, automatically completing it (committing or rolling back), retrying and/or cleaning up as required. To force a rollback, simply throw an exception."
  [f]
  `(let [work# (reify RetryingTransactionHelper$RetryingTransactionCallback
                     (~'execute [~'this] ~f))]
     (.doInTransaction (retrying-txn-helper) work#)))
    
;(defmacro in-ro-tx
;  "Runs the given logic in a R/O transaction, automatically completing it (committing or rolling back), retrying and cleaning up as required."
;  [f]
