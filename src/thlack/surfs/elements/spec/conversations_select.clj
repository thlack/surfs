(ns thlack.surfs.elements.spec.conversations-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:conversations_select})

(s/def ::initial_conversation ::strings.spec/id)

(s/def ::default_to_current_conversation boolean?)
