(ns thlack.surfs.elements.spec.radio-buttons
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]))

(s/def ::type #{:radio_buttons})

(s/def ::options (s/coll-of ::comp.spec/option :into [] :min-count 1 :max-count 10 :gen-max 5))

(s/def ::initial_option ::comp.spec/option)
