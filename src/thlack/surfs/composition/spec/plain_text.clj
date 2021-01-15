(ns thlack.surfs.composition.spec.plain-text
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.text :as text]))

(s/def ::type #{:plain_text})

(s/def ::plain-text (s/keys :req-un [::type ::text/text] :opt-un [::text/emoji]))
