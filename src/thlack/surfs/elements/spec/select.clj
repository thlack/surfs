(ns ^:no-doc thlack.surfs.elements.spec.select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :refer [deftext]]))

(deftext ::placeholder ::comp.spec/plain-text 150)

(s/def ::options (s/coll-of ::comp.spec/option :max-count 100 :min-count 1 :into [] :gen-max 5))

(s/def ::option_groups (s/coll-of ::comp.spec/option-group :max-count 100 :min-count 1 :into [] :gen-max 5))

(s/def ::initial_option ::comp.spec/option)

(s/def ::initial_options ::options)

(s/def ::response_url_enabled boolean?)
