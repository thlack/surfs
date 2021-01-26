(ns ^:no-doc thlack.surfs.elements.spec.channels-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:channels_select})

(s/def ::initial_channel ::strings.spec/id)
