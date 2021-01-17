(ns ^:no-doc thlack.surfs.elements.spec.multi-channels-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:multi_channels_select})

(s/def ::initial_channels (s/coll-of ::strings.spec/id :into [] :gen-max 10 :min-count 1))
