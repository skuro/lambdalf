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

(defmacro with-tx
  [bindings & body]
  `(let ~(subvec bindings 0 2)
     (try
       (.begin ~(bindings 0))
       ~@body
       (catch Throwable ~'e
         (if (= Status/STATUS_ACTIVE (.getStatus ~(bindings 0)))
           (.rollback ~(bindings 0)))
         (throw ~'e)))))

(defn create-tx
  ([] (.getUserTransaction (transaction-service)))
  ([ro] (.getUserTransaction (transaction-service) ro))
  ([readOnly ignoreSystemRO] (.getUserTransaction (transaction-service) readOnly ignoreSystemRO)))
