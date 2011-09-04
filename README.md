Lambdalf -- Alfresco with a Clojure taste
=========================================

Here you can find an ongling effort to build a Clojure adapter around the Alfresco Foundation API. Currently the following features are supported:

- reading/writing/adding [nodes](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/nodes.clj) properties
- [search](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/search.clj)
- setting [behaviours](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/behave.clj) upon `onAddAspect` events
- run your code with the specified [credentials](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/auth.clj)
- run clojure webscripts using the spring webscript [clojure addon](https://github.com/skuro/spring-webscripts-addon-clojure)

Structure
=========

- [lambdalf-lib](https://github.com/skuro/lambdalf/tree/master/lambdalf-lib) -- a JAR project, contains all the real code provided in lambdalf. Use this as a dependency to leverage lambdalf code.

- [lambdalf](https://github.com/skuro/lambdalf/tree/master/lambdalf)
  -- an AMP project, built from the [Maven AMP plugin](http://code.google.com/p/maven-alfresco-archetypes/). It
  ships `lambdalf-lib` plus an AMP structure to easily install
  `lambdalf` on your Alfresco instance, plus some goodies like a web
  script to start a swank server to open a REPL from Emacs to a
  running Alfresco instane.

How to build it
===============

In order to build this projects yourself, you  need a working Maven
installation (tested with v3.0), and some Alfresco artifacts in your
local repository.
As Alfresco doesn't always ship artifacts in a timely fashion, you can
download the alfresco.war file and extract and install artifacts from
it following [this
guide](http://code.google.com/p/maven-alfresco-archetypes/wiki/MaintainYourRepo).

Samples
=======

More of a show and tell than real products, some
[samples](https://github.com/skuro/lambdalf-samples) are provided to
verify and explain the features of lambdalf.

Learn more
==========

- my personal [blog](http://skuro.tk)
- clojure support for spring [web scripts](https://github.com/skuro/spring-webscripts-addon-clojure) 
