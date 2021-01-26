(ns ^:no-doc thlack.surfs.blocks.spec.input
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.elements.spec :as elements.spec]
            [thlack.surfs.strings.spec :refer [deftext]]))

(s/def ::type #{:input})

(deftext ::label ::comp.spec/plain-text 2000)

(s/def ::element
  (s/or :plain-text-input ::elements.spec/plain-text-input
        :checkboxes ::elements.spec/checkboxes
        :radio-buttons ::elements.spec/radio-buttons
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
        :channels-select ::elements.spec/channels-select))

(s/def ::dispatch_action boolean?)

(deftext ::hint ::comp.spec/plain-text 2000)

(s/def ::optional boolean?)
