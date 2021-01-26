(ns ^:no-doc thlack.surfs.elements.spec.checkboxes
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]))

(s/def ::type #{:checkboxes})

(s/def ::options (s/coll-of ::comp.spec/option :into [] :min-count 1 :max-count 10))

(s/def ::initial_options ::options)
