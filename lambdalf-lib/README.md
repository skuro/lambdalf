lambdalf -- Clojure magic for Alfresco
======================================

This library is a Clojure adapter to the [Alfresco Foundation API](http://wiki.alfresco.com/wiki/Java_Foundation_API).
Its goal is to provide an idiomatic API to Alfresco developers to leverage Clojure as their programming language.

Sample code
========================

Here's some sample code from a REPL session connected to a running Alfresco repository
(learn [more](https://github.com/skuro/lambdalf)). Noe that there are [better ways](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/nodes.clj#L28)
to get a handle to the Company Home `nodeRef`, and that you don't need to close any `ResultSet` after you query!

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
