(ns ^:no-doc thlack.surfs.composition.components.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.props.spec :as props.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

;;; [:confirm]

(deftext :confirm-props/confirm ::strings.spec/string 30)

(deftext :confirm-props/deny ::strings.spec/string 30)

(deftext :confirm-props/title ::strings.spec/string 100)

(s/def :confirm/props (s/keys :req-un [:confirm-props/confirm :confirm-props/deny :confirm-props/title] :opt-un [:confirm/style ::props.spec/disable_emoji_for]))

;;; [:option]

(deftext :option-props/description ::strings.spec/string 75)

(s/def :option/props (s/keys :req-un [:option/value] :opt-un [:option-props/description ::props.spec/disable_emoji_for]))
