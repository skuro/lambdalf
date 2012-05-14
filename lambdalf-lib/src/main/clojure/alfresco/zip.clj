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
 
(ns alfresco.zip
  (:require [clojure.zip :as z]
            [alfresco.model :as m]
            [alfresco.nodes :as n]))

(defn branch?
  "Verifies if the current location can have children.
   While cm:content can have children, this is currently limited to cm:folder"
  [node]
  (m/qname-isa? "cm:folder" (n/type-qname node)))

(defn make-node
  "Associates the given children with the given parent node"
  [node children]
  (apply n/create-child-assoc node children)
  node)

(defn repo-zip
  "Creates a zipper with the given node as root"
  [root]
  (let [children #(seq (n/children %))]
    (z/zipper branch? children make-node root)))
