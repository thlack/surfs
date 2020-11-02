(ns ^:no-doc thlack.surfs.validation
  "Handles validation for components"
  (:require [clojure.spec.alpha :as s]
            [expound.alpha :as expound]))

(defmacro validated
  "Returns a data structure that is validated against a spec or throws an informative
   exception."
  [x spec]
  `(if (s/valid? ~spec ~x)
     ~x
     (let [explain-data# (s/explain-data ~spec ~x)]
       (throw (ex-info (expound/expound-str ~spec ~x)
                       explain-data#)))))
