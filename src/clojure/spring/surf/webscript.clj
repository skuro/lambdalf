(ns spring.surf.webscript
  (:import [java.io InputStream Writer]))

(defn- k2s
  "Returns a map ensuring that keys are all Strings and not clojure keywords"
  [amap]
  (zipmap (map name (keys amap))
          (vals amap)))

(defn- s2k
  "Returns a map ensuring that keys are all clojure keywords and no Strings"
  [amap]
  (zipmap (map keyword (keys amap))
          (vals amap)))

(defn args
  "Fetches arguments from the input map by name"
  [model]
  (s2k (.get model "args")))

(defn req-body-str
  "Returns the HTTP request body as a String"
  [model]
  (-> model (.get "requestbody") (.getContent)))

(defn template-args
  "Fetches all the template arguments from the webscript UrlModel"
  [model]
  (s2k (.getTemplateArgs (.get model "url"))))

(defn return
  "Updates the view-model with the provided one"
  [model view-model]
  (let [view-model-orig (.get model "model")]
    (.putAll view-model-orig (k2s view-model))
    model))

