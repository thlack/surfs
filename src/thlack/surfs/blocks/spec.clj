(ns ^:no-doc thlack.surfs.blocks.spec
  "https://api.slack.com/reference/block-kit/block-elements#checkboxes"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]
            [thlack.surfs.elements.spec :as elements.spec]))

(s/def :section/type #{:section})

(deftext :section/text ::comp.spec/text 3000)

(s/def :section/fields (s/coll-of (s/and ::comp.spec/text (strings.spec/max-len 2000)) :into [] :min-count 1 :max-count 10 :gen-max 5))

(s/def ::fields (s/keys :req-un [:section/fields]))

(s/def :section/accessory
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

(s/def ::section (s/or
                  :text   (s/keys :req-un [:section/type :section/text] :opt-un [::strings.spec/block_id :section/accessory :section/fields])
                  :fields (s/keys :req-un [:section/type :section/fields] :opt-un [::strings.spec/block_id :section/accessory :section/text])))

(s/def :context/type #{:context})

(s/def :context/element (s/or :image ::elements.spec/image :text ::comp.spec/text))

(s/def :context/elements (s/coll-of :context/element :into [] :min-count 1 :max-count 10 :gen-max 5))

(s/def ::context (s/keys :req-un [:context/type :context/elements] :opt-un [::strings.spec/block_id]))

(s/def :actions/type #{:actions})

(s/def :actions/element
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

(s/def :actions/elements (s/coll-of :actions/element :into [] :min-count 1 :max-count 5 :gen-max 5))

(s/def ::actions (s/keys :req-un [:actions/type :actions/elements] :opt-un [::strings.spec/block_id]))

(s/def :divider/type #{:divider})

(s/def ::divider (s/keys :req-un [:divider/type] :opt-un [::strings.spec/block_id]))

(s/def :file/type #{:file})

(s/def :file/external_id ::strings.spec/string)

(s/def :file/source #{:remote})

;;; file blocks are not directly instantiable at the moment - the spec is defined here even though no component will
;;; be defined for rendering.

(s/def ::file (s/keys :req-un [:file/type :file/external_id :file/source] :opt-un [::strings.spec/block_id]))

(s/def :header/type #{:header})

(deftext :header/text ::comp.spec/plain-text 150)

(s/def ::header (s/keys :req-un [:header/type :header/text] :opt-un [::strings.spec/block_id]))

(s/def :image/type #{:image})

(deftext :image/image_url ::strings.spec/url-string 3000)

(deftext :image/alt_text ::strings.spec/string 2000)

(deftext :image/title ::comp.spec/plain-text 2000)

(s/def ::image (s/keys :req-un [:image/type :image/image_url :image/alt_text] :opt-un [:image/title ::strings.spec/block_id]))

(s/def :input/type #{:input})

(deftext :input/label ::comp.spec/plain-text 2000)

(s/def :input/element
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

(s/def :input/dispatch_action boolean?)

(deftext :input/hint ::comp.spec/plain-text 2000)

(s/def :input/optional boolean?)

(s/def ::input (s/keys :req-un [:input/type :input/label :input/element] :opt-un [:input/dispatch_action ::strings.spec/block_id :input/hint :input/optional]))

(s/def ::block
  (s/or :actions ::actions
        :context ::context
        :divider ::divider
        :file    ::file
        :header  ::header
        :image   ::image
        :input   ::input
        :section ::section))
