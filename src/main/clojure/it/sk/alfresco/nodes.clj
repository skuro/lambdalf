(ns it.sk.alfresco.nodes
	(:require [it.sk.alfresco.core :as c]))

(defonce *node-service* (.getNodeService c/*alfresco-services*))

(defprotocol Node
	(noderef [this] "Gives a NodeRef out of an entity like e.g. a clojure map")
	(props [this] "Returns a map of properties for the given entity")
	(bind [this noderef] "Used to provide a more clojure friendly entity representing an Alfresco node"))

(deftype SimpleNode
	[propmap aspects ref]
	Node
	(noderef [this] ref)
	(props [this] (.propmap this))
	(bind
		[this noderef]
		(do (set! propmap (into {} (.getProperties *node-service* noderef))
			(set! aspects (into #{} (.getAspects *node-service* noderef)))))))