
# Lambdalf -- Clojure support for Alfresco

----

This is a substantial fork of Carlo Sciolla's [lambdalf](http://github.com/skuro/lambdalf) project.  The primary difference
is that this version replaces Maven with Leiningen as the build tool, thereby substantially simplifying the project.

----

This library adds [Clojure](http://www.clojure.org/) support to the open source
[Alfresco Content Management System](http://www.alfresco.com/). Specifically, it:

 * adds an idiomatic Clojure wrapper around (an increasing %age of) the [Alfresco Java API](http://wiki.alfresco.com/wiki/Java_Foundation_API)
 * provides support for implementing Alfresco extension points in Clojure, including
   [behaviours](https://github.com/pmonks/lambdalf/blob/master/src/clojure/alfresco/behave.clj), and
   web scripts (via [clojure-webscripts ](https://github.com/skuro/spring-webscripts-addon-clojure))
 * adds NREPL and SWANK servers to the Alfresco system (disabled by default - requires administor rights to enable), allowing for
   productive REPL-style experimentation and development in Alfresco
 * packages all of this, along with the Clojure runtime, into an [Alfresco Module Package](http://wiki.alfresco.com/wiki/AMP_Files)
   that 3rd party code can depend on (thereby avoiding conflicts between different Clojure extensions)

## Developing with lambdalf

lambdalf is (NOT YET!) available as a Maven artifact from [Clojars](https://clojars.org/org.clojars.pmonks/lambdalf).
Plonk the following in your project.clj :plugins, `lein deps` and you should be good to go:

```clojure
[org.clojars.pmonks/lambdalf "#.#.#"]
```

The latest version is:

[![version](https://clojars.org/org.clojars.pmonks/lambdalf/latest-version.svg)](https://clojars.org/org.clojars.pmonks/lambdalf)

Here's some sample code from an NREPL session connected to a running Alfresco repository. Note that there are
[better ways](https://github.com/pmonks/lambdalf/blob/master/src/clojure/alfresco/nodes.clj#L65) to get a handle to the Company
Home `nodeRef`, and that you don't need to close any `ResultSet` after you query!

```
    user> (require '[alfresco.auth :as a])
    nil
    user> (require '[alfresco.search :as s])
    nil
    user> (require '[alfresco.nodes :as n])
    nil
    user> (def company-home
              (a/as-admin
               (first
                (s/query "PATH:\"/*\" AND TYPE:\"cm:folder\""))))
    #'user/company-home
    user> (clojure.pprint/pprint
       (a/as-admin
        (n/properties company-home)))
    {"sys:store-identifier" "SpacesStore",
     "cm:modifier" "System",
     "cm:title" "Company Home",
     "cm:description" "The company root space",
     "sys:store-protocol" "workspace",
     "app:icon" "space-icon-default",
     "sys:node-dbid" 13,
     "cm:created" #<Date Sun Sep 04 15:11:18 CEST 2011>,
     "sys:node-uuid" "43356014-0428-4e86-9490-e78a6c0c48ef",
     "cm:modified" #<Date Sun Sep 04 15:11:18 CEST 2011>,
     "cm:name" "Company Home",
     "cm:creator" "System"}
    nil
```

## Packaging
Due to the way Alfresco's AMP module mechanism works, lambdalf is shipped as an AMP file (####TODO: add link to release,
once available) in addition to the clojars artifacts (which are intended for compilation only).  It is this AMP artifact that
should be deployed to a running Alfresco server, prior to deployment of your own AMP.

Your code should also be packaged as an AMP (via the [lein amp](https://github.com/pmonks/lein-amp) plugin), and must include
a module dependency on lambdalf in order to prevent an Alfresco user from inadvertently deploying your code without first deploying
lambdalf. In the near future the lein-amp plugin should [handle this automatically](https://github.com/pmonks/lein-amp/issues/2).

## Developer Information

[GitHub project](https://github.com/pmonks/lambdalf)

[Bug Tracker](https://github.com/pmonks/lambdalf/issues)

## License

Copyright © 2011-2014 Carlo Sciolla
This fork has been developed by Peter Monks (pmonks@gmail.com).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
