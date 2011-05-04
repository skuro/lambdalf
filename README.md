Lambdalf -- Alfresco with a Clojure taste
=========================================

Here you can find an ongling effort to build a Clojure adapter around the Alfresco Foundation API. Currently the following features are supported:

- reading/writing/adding [nodes](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/nodes.clj) properties
- [search](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/search.clj)
- setting [behaviours](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/behave.clj) upon `onAddAspect` events
- run your code with the specified [credentials](https://github.com/skuro/lambdalf/blob/master/lambdalf-lib/src/main/clojure/alfresco/auth.clj)

Structure
=========

- [lambdalf-lib](https://github.com/skuro/lambdalf/tree/master/lambdalf-lib) -- a JAR project, contains all the code provided in lambdalf. Use this as a dependency to leverage lambdalf code.

- [lambdalf](https://github.com/skuro/lambdalf/tree/master/lambdalf) -- an AMP project, built from the [Maven AMP plugin](http://code.google.com/p/maven-alfresco-archetypes/). It ships `lambdalf-lib` plus an AMP structure to easily install `lambdalf` on your Alfresco instance.

Samples
=======

More of a show and tell than real products, some [samples](https://github.com/skuro/lambdalf-samples) are provided to verify and explain the feature of lambdalf.

Learn more
==========

- my personal [blog](http://skuro.tk)
- clojure support for spring [web scripts](https://github.com/skuro/spring-webscripts-addon-clojure)
