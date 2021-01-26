(ns ^:no-doc thlack.surfs.blocks.spec.section
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.elements.spec :as elements.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:section})

(deftext ::text ::comp.spec/text 3000)

(s/def ::fields (s/coll-of (s/and ::comp.spec/text (strings.spec/max-len 2000)) :into [] :min-count 1 :max-count 10 :gen-max 5))

(s/def ::accessory
  (s/or :checkboxes ::elements.spec/checkboxes
        :datepicker ::elements.spec/datepicker
        :timepicker ::elements.spec/timepicker
        :multi-static-select ::elements.spec/multi-static-select
        :multi-external-select ::elements.spec/multi-external-select
        :multi-users-select ::elements.spec/multi-users-select
        :multi-conversations-select ::elements.spec/multi-conversations-select
        :multi-channels-select ::elements.spec/multi-channels-select
        :plain-text-input ::elements.spec/plain-text-input
        :radio-buttons ::elements.spec/radio-buttons
        :static-select ::elements.spec/static-select
        :external-select ::elements.spec/external-select
        :users-select ::elements.spec/users-select
        :conversations-select ::elements.spec/conversations-select
        :channels-select ::elements.spec/channels-select
        :overflow ::elements.spec/overflow
        :button ::elements.spec/button
        :image ::elements.spec/image))
