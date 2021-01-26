(ns ^:no-doc thlack.surfs.blocks.spec.actions
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.elements.spec :as elements.spec]))

(s/def ::type #{:actions})

(s/def ::element
  (s/or :button ::elements.spec/button
        :checkboxes ::elements.spec/checkboxes
        :plain-text-input ::elements.spec/plain-text-input
        :radio-buttons ::elements.spec/radio-buttons
        :overflow ::elements.spec/overflow
        :datepicker ::elements.spec/datepicker
        :timepicker ::elements.spec/timepicker
        :static-select ::elements.spec/static-select
        :external-select ::elements.spec/external-select
        :users-select ::elements.spec/users-select
        :conversations-select ::elements.spec/conversations-select
        :channels-select ::elements.spec/channels-select))

(s/def ::elements (s/coll-of ::element :into [] :min-count 1 :max-count 5 :gen-max 5))
