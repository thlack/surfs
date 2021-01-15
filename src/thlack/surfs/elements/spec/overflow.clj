(ns thlack.surfs.elements.spec.overflow
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:overflow})

(s/def ::url (s/and ::strings.spec/url-string (strings.spec/max-len 3000)))

(s/def ::option (s/merge ::comp.spec/option (s/keys :opt-un [::url])))

(s/def ::options (s/coll-of ::option :into [] :min-count 2 :max-count 5 :gen-max 3))
