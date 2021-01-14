(ns ^:no-doc thlack.surfs.validation
  "Handles validation for components"
  (:require [clojure.spec.alpha :as s]))

(defmacro validated
  "Returns a data structure that is validated against a spec or throws an informative
   exception."
  [x spec]
  `(s/assert ~spec ~x))
