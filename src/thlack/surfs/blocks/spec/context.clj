(ns thlack.surfs.blocks.spec.context
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.elements.spec :as elements.spec]))

(s/def ::type #{:context})

(s/def ::element (s/or :image ::elements.spec/image :text ::comp.spec/text))

(s/def ::elements (s/coll-of ::element :into [] :min-count 1 :max-count 10 :gen-max 5))
