(ns ^:no-doc thlack.surfs.composition.spec.dispatch-action-config
  (:require [clojure.spec.alpha :as s]))

(s/def ::trigger_actions_on (s/coll-of #{:on_enter_pressed :on_character_entered} :min-count 1 :distinct true :gen-max 2 :into []))
