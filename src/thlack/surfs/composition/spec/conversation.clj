(ns ^:no-doc thlack.surfs.composition.spec.conversation
  (:require [clojure.spec.alpha :as s]))

(s/def ::include (s/coll-of #{:im :mpim :private :public} :distinct true :into [] :gen-max 4 :min-count 1))

(s/def ::exclude_external_shared_channels boolean?)

(s/def ::exclude_bot_users boolean?)

(s/def ::filter (s/keys :opt-un [::include ::exclude_external_shared_channels ::exclude_bot_users]))
