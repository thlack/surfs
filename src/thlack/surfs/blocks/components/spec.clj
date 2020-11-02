(ns ^:no-doc thlack.surfs.blocks.components.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def :block/props (s/keys :opt-un [::strings.spec/block_id]))

;;; [:actions]

(s/def :actions/children
  (s/with-gen
    (s/+ :actions/element)
    #(s/gen :actions/elements)))

;;; [:section]

(s/def :section/child (s/or :text   :section/text
                            :fields ::blocks.spec/fields
                            :accessory :section/accessory))

;;; [:context]

(s/def :context/children
  (s/with-gen
    (s/+ :context/element)
    #(s/gen :context/elements)))

;;; [:header]

(deftext :header-child/text (s/or :string ::strings.spec/string :text :header/text) 150)

;;; [:image]

(deftext :image-child/title (s/or :string ::strings.spec/string :text :image/title) 2000)

(s/def :image/props (s/merge :block/props (s/keys :req-un [:image/image_url :image/alt_text])))

;;; [:input]

(deftext :input-child/label (s/or :string ::strings.spec/string :text :input/label) 2000)

(s/def :input/child
  (s/or :hint    ::comp.spec/plain-text
        :element :input/element))

(s/def :input/children
  (s/with-gen
    (s/+ :input/child)
    #(gen/fmap
      (fn [input]
        (vals (select-keys input [:hint :element])))
      (s/gen ::blocks.spec/input))))

(s/def :input/props (s/merge :block/props (s/keys :opt-un [:input/dispatch_action :input/optional])))
