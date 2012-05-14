(ns alfresco.transact
  (:require [alfresco.core :as c])
  (:import [org.alfresco.repo.transaction RetryingTransactionHelper$RetryingTransactionCallback]
           [javax.transaction Status]))

(defn transaction-service
  []
  (.getTransactionService (c/alfresco-services)))

(defmacro in-tx [& body]
  `(let [cb# (reify RetryingTransactionHelper$RetryingTransactionCallback
               (~'execute [~'this] ~@body))]
     (-> (transaction-service)
         (.getRetryingTransactionHelper)
         (.doInTransaction cb#))))
