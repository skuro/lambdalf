
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
   [web scripts](https://github.com/pmonks/lambdalf/blob/master/src/clojure/spring/surf/webscript.clj)
 * adds an NREPL server to the Alfresco server (disabled by default - requires administrator rights to enable),
   allowing for productive REPL-style experimentation and development within Alfresco
 * packages all of this, along with the Clojure runtime, into an [Alfresco Module Package](http://wiki.alfresco.com/wiki/AMP_Files)
   that 3rd party code can depend on (thereby avoiding conflicts between different Clojure extensions)

## Packaging
Due to the way Alfresco's AMP module mechanism works, lambdalf is shipped as an [AMP file](http://wiki.alfresco.com/wiki/AMP_Files)
in addition to the clojars artifacts (which are used for development only).  It is this AMP artifact that should be deployed to a
running Alfresco server, prior to the deployment of your own AMP.

Your code should also be packaged as an AMP (via the [lein amp plugin](https://github.com/pmonks/lein-amp)), and must include
a module dependency on lambdalf in order to prevent an Alfresco administrator from inadvertently deploying your code without first deploying
lambdalf. In the near future the lein-amp template should [set these dependencies up automatically](https://github.com/mstang/alfresco-amp-template/issues/1).

## Installing lambdalf into Alfresco

Download the latest [AMP file](http://wiki.alfresco.com/wiki/AMP_Files) from the [releases page](https://github.com/pmonks/lambdalf/releases)
(NOT YET AVAILABLE!), then install it just like any other AMP (i.e. using Alfresco's [MMT tool](https://wiki.alfresco.com/wiki/Module_Management_Tool)).

### Opening a REPL

For security reasons (i.e. it opens a massive script injection attack hole!) the NREPL server included in lambdalf is not running by default.
To enable it (keeping in mind that it opens a massive script injection attack hole!) an HTTP POST Web Script is provided at
/alfresco/service/clojure/nrepl. For a default installation of Alfresco on localhost, you can run:

```shell 
    $ curl -u admin:admin -X POST http://localhost:8080/alfresco/service/clojure/nrepl
```

to enable the NREPL server.  The Web Script will respond with the port that the NREPL server is running on (default is 7888).  From there you can
use leiningen's built-in NREPL client to connect to this NREPL:

```shell
    $ lein repl :connect 7888
```

See below for some example expressions to run to validate the installation.

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
Home `nodeRef`.  Note also that unlike Alfresco's native Java API, each `ResultSet` is automatically closed after a search.

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

## Developer Information

[GitHub project](https://github.com/pmonks/lambdalf)

[Bug Tracker](https://github.com/pmonks/lambdalf/issues)

## License

Copyright © 2011-2014 Carlo Sciolla

This fork is maintained by Peter Monks (pmonks@gmail.com).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
