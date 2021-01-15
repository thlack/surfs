(ns thlack.surfs.composition.spec.option-group
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.plain-text :as plain-text]
            [thlack.surfs.composition.spec.option :as option]
            [thlack.surfs.strings.spec :refer [deftext]]))

(deftext ::label ::plain-text/plain-text 75)

(s/def ::options (s/coll-of ::option/option :min-count 1 :max-count 100 :into [] :gen-max 10))
