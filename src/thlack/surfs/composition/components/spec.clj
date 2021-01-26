(ns ^:no-doc thlack.surfs.composition.components.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.composition.spec.confirm :as confirm]
            [thlack.surfs.composition.spec.option :as option]
            [thlack.surfs.composition.spec.option-group :as option-group]
            [thlack.surfs.props.spec :as props.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

;;; [:confirm]

(deftext :thlack.surfs.composition.components.spec.confirm-props/confirm ::strings.spec/string 30)

(deftext :thlack.surfs.composition.components.spec.confirm-props/deny ::strings.spec/string 30)

(deftext :thlack.surfs.composition.components.spec.confirm-props/title ::strings.spec/string 100)

(s/def ::confirm.props (s/keys :req-un [:thlack.surfs.composition.components.spec.confirm-props/confirm
                                        :thlack.surfs.composition.components.spec.confirm-props/deny
                                        :thlack.surfs.composition.components.spec.confirm-props/title]
                               :opt-un [::confirm/style
                                        ::props.spec/disable_emoji_for]))

;;; [:option]

(deftext :thlack.surfs.composition.components.spec.option-props/description ::strings.spec/string 75)

(s/def ::option.props (s/keys :req-un [::option/value] :opt-un [:thlack.surfs.composition.components.spec.option-props/description
                                                                ::props.spec/disable_emoji_for]))
(s/def ::option-group.children
  (s/with-gen
    (s/+ ::comp.spec/option)
    #(s/gen ::option-group/options)))
