(ns ^:no-doc thlack.surfs.props.spec
  "Contains specs for convenience props included via thlack.surfs"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::selected? boolean?)

(s/def ::disable_emoji_for (s/coll-of keyword? :kind set?))

(s/def ::text (s/or :string ::strings.spec/string :map ::comp.spec/text))

(s/def ::plain-text (s/or :string ::strings.spec/string :map ::comp.spec/plain-text))
