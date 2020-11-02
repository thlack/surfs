(ns ^:no-doc thlack.surfs.views.components.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.props.spec :as props.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

;;; Override private_metadata spec so it accomodates any value. All
;;; private_metadata will be passed through (pr-str) so Clojure data structures
;;; can be used easily

(s/def ::private_metadata
  (s/with-gen
    any?
    #(gen/return {:meta? true :data? true})))

(s/def :view/props (s/keys :opt-un [::private_metadata :view/callback_id :view/external_id]))

(s/def :view/child (s/or :block ::blocks.spec/block))

(s/def :view/children
  (s/with-gen
    (s/+ :view/child)
    #(s/gen :view/blocks)))

;;; [:modal]

(deftext :modal-props/string ::strings.spec/string 24)

(s/def :modal-props/title :modal-props/string)

(s/def :modal-props/close :modal-props/string)

(s/def :modal-props/submit :modal-props/string)

(s/def :modal/props (s/merge :view/props (s/keys :req-un [:modal-props/title] :opt-un [:modal-props/close :modal-props/submit :modal/clear_on_close :modal/notify_on_close ::props.spec/disable_emoji_for])))
