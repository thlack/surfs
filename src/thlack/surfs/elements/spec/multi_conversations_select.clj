(ns ^:no-doc thlack.surfs.elements.spec.multi-conversations-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:multi_conversations_select})

(s/def ::initial_conversations (s/coll-of ::strings.spec/id :into [] :gen-max 5 :min-count 1))
