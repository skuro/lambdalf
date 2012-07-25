;
; Copyright (C) 2012 Peter Monks
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

(ns alfresco.test.transact
  (:use [clojure.test]
        [clojure.tools.nrepl]
        [alfresco.test])
  (:require [clj-http.client :as http]))

(defn init-fixture [f]
  (start-nrepl)
  (let [c (client (connect :port 7888) 1000)]
    (message c {:op :eval :code (code (require '[alfresco.auth :as a]))})
    (message c {:op :eval :code (code (require '[alfresco.nodes :as n]))})
    (message c {:op :eval :code (code (require '[alfresco.transact :as t]))})
    (f)))

(use-fixtures :once init-fixture)

(defftest transact-ftests
  (do
    (are [result f expr] (= result (f (repl-eval client (code expr))))

         ; Run a simple clojure expression within an Alfresco txn
         2 repl-value ((t/in-ro-tx-as (a/admin) (+ 1 1)))


         ; Get the name of Company Home within an Alfresco txn
         "Company Home" repl-value (t/in-ro-tx-as (a/admin)
                                    (n/property (n/company-home)
                                                :cm/name))

         ; Grab the first Share site and validate that it is indeed a Share site
         true repl-value (t/in-ro-tx-as (a/admin) (n/site? (first (n/children (n/sites-home)))))
    )))
