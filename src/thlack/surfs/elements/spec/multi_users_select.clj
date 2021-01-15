(ns thlack.surfs.elements.spec.multi-users-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:multi_users_select})

(s/def ::initial_users (s/coll-of ::strings.spec/id :into [] :gen-max 10 :min-count 1))
