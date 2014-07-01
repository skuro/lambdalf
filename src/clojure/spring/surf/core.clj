(ns spring.surf.core
  (:import [spring.surf.webscript WebScript]))

(defmacro defscript
  "Creates a new Clojure backed web script controller which will execute body:

   (defscript [a b] {:title a :body b})

   Will result in a WebScript that will take :a and :b from the input model
   and puts the resulting map in the output model for an FTL to process.
"
  [params & body]
  `(let [w# (reify WebScript
              (run [~'this ~'in ~'out {:keys [~'a ~'b]}]
                ~@body))]
     (def ~'*webscript* w#)
     w#))
