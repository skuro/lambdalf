lambdalf -- Clojure magic for Alfresco
======================================

Lambdalf is a convenient Clojure adapter around Alfresco Repository APIs.
It is implemented as an Alfresco Module Package, grounding on top of
the excellent [Maven AMP Archetype](code.google.com/p/maven-alfresco-archetypes/).

Usage
=====================

You will need [`lambdalf-lib`](https://github.com/skuro/lambdalf/tree/master/lambdalf-lib) already installed in your
local maven repository.

Run a local Jetty server with a deployed Alfresco already provided with lambdalf installed:

    MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=256m" mvn clean integration-test -Pwebapp

This will start up a Clojure spiced version of an Alfresco repository you can connect to at:

    http://localhost:9090/lambdalf-webapp

You can then start a [swank server](https://github.com/technomancy/swank-clojure) by calling the appropriate
[web script](https://github.com/skuro/lambdalf/tree/master/lambdalf/src/main/resources/alfresco/templates/webscripts/clj):

    $ curl -X POST -u admin:admin http://localhost:9090/lambdalf-webapp/service/swank

Currently the web script doesn't allow for choosing a port different from the default one: 4005.

Sample code
=====================

Sample usages are provided in [lambdalf-samples](https://github.com/skuro/lambdalf-samples)