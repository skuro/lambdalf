;
; Copyright © 2013,2014 Carlo Sciolla
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

(defproject org.clojars.pmonks/lambdalf-amp "0.1.0-SNAPSHOT"
  :description      "Sample AMP project for testing lein-amp."
  :license          {:name "Apache License, Version 2.0"
                     :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version "2.0.0"
  :javac-target     "1.6"
  :dependencies [
                  [org.clojure/clojure "1.6.0"]
                  [lambdalf/lambdalf   "0.2.0-SNAPSHOT"]
                ]
  :profiles {:dev      { :plugins [[lein-amp "0.1.0-SNAPSHOT"]] }
             :uberjar  { :aot :all }
           }
  :uberjar-merge-with {#"META-INF/services/.*" [slurp str spit]}   ; Awaiting Leiningen 2.3.5 - see https://github.com/technomancy/leiningen/issues/1455
  )
