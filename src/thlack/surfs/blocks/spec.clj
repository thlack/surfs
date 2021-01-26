(ns ^:no-doc thlack.surfs.blocks.spec
  "https://api.slack.com/reference/block-kit/block-elements#checkboxes"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.blocks.spec.actions :as actions]
            [thlack.surfs.blocks.spec.context :as context]
            [thlack.surfs.blocks.spec.divider :as divider]
            [thlack.surfs.blocks.spec.file :as file]
            [thlack.surfs.blocks.spec.header :as header]
            [thlack.surfs.blocks.spec.image :as image]
            [thlack.surfs.blocks.spec.input :as input]
            [thlack.surfs.blocks.spec.section :as section]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::fields (s/keys :req-un [::section/fields]))

(s/def ::section (s/or
                  :text   (s/keys :req-un [::section/type ::section/text] :opt-un [::strings.spec/block_id ::section/accessory ::section/fields])
                  :fields (s/keys :req-un [::section/type ::section/fields] :opt-un [::strings.spec/block_id ::section/accessory ::section/text])))

(s/def ::context (s/keys :req-un [::context/type ::context/elements] :opt-un [::strings.spec/block_id]))

(s/def ::actions (s/keys :req-un [::actions/type ::actions/elements] :opt-un [::strings.spec/block_id]))

(s/def ::divider (s/keys :req-un [::divider/type] :opt-un [::strings.spec/block_id]))

;;; file blocks are not directly instantiable at the moment - the spec is defined here even though no component will
;;; be defined for rendering.

(s/def ::file (s/keys :req-un [::file/type ::file/external_id ::file/source] :opt-un [::strings.spec/block_id]))

(s/def ::header (s/keys :req-un [::header/type ::header/text] :opt-un [::strings.spec/block_id]))

(s/def ::image (s/keys :req-un [::image/type ::image/image_url ::image/alt_text] :opt-un [::image/title ::strings.spec/block_id]))

(s/def ::input (s/keys :req-un [::input/type ::input/label ::input/element] :opt-un [::input/dispatch_action ::strings.spec/block_id ::input/hint ::input/optional]))

(s/def ::block
  (s/or :actions ::actions
        :context ::context
        :divider ::divider
        :file    ::file
        :header  ::header
        :image   ::image
        :input   ::input
        :section ::section))
