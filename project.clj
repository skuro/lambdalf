;
; Copyright © 2011-2014 Carlo Sciolla
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
; 
; Contributors:
;    Carlo Sciolla - initial implementation
;    Peter Monks   - contributor

(defproject org.clojars.lambdalf/lambdalf "2.0.0-SNAPSHOT"
  :title            "lambdalf"
  :description      "Lambdalf -- Clojure support for Alfresco"
  :url              "https://github.com/lambdalf/lambdalf"
  :license          { :name "Apache License, Version 2.0"
                      :url "http://www.apache.org/licenses/LICENSE-2.0" }
  :min-lein-version "2.4.0"
  :repositories [
                  ["alfresco.public" "https://artifacts.alfresco.com/nexus/content/groups/public/"]
                ]
  :dependencies [
                  [org.clojure/clojure     "1.6.0"]
                  [org.clojure/tools.nrepl "0.2.3"]
                ]
  :profiles {:dev      { :plugins [[lein-amp "0.1.0"]] }
             :uberjar  { :aot :all }
             :provided { :dependencies [
                                         [org.alfresco/alfresco-core                            "4.2.f"         :scope "runtime"]
                                         [org.alfresco/alfresco-data-model                      "4.2.f"         :scope "runtime"]
                                         [org.alfresco/alfresco-mbeans                          "4.2.f"         :scope "runtime"]
                                         [org.alfresco/alfresco-remote-api                      "4.2.f"         :scope "runtime"]
                                         [org.alfresco/alfresco-repository                      "4.2.f"         :scope "runtime"]
                                         [org.springframework/spring-context                    "3.0.5.RELEASE" :scope "runtime"]
                                         [org.springframework/spring-beans                      "3.0.5.RELEASE" :scope "runtime"]
                                         [org.springframework.extensions.surf/spring-webscripts "1.2.0"         :scope "runtime"]
                                       ] }
            }
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :resource-paths    ["src/resource"]
  :javac-target      "1.6"
  )
