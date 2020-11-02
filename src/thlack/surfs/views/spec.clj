(ns ^:no-doc thlack.surfs.views.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.blocks.spec :as blocks.spec]))

(s/def :modal/type #{:modal})

(s/def :home/type #{:home})

(deftext :modal/title ::comp.spec/plain-text 24)

(s/def :view/blocks (s/coll-of ::blocks.spec/block :into [] :max-count 100 :min-count 1 :gen-max 3))

(deftext :modal/close ::comp.spec/plain-text 24)

(deftext :modal/submit ::comp.spec/plain-text 24)

(s/def :view/private_metadata (strings.spec/with-max-gen
                                ::strings.spec/string
                                3000))

(s/def :view/callback_id (strings.spec/with-max-gen
                           ::strings.spec/string
                           255))

(s/def :modal/clear_on_close boolean?)

(s/def :modal/notify_on_close boolean?)

(s/def :view/external_id ::strings.spec/string)

(s/def ::home (s/keys :req-un [:home/type :view/blocks] :opt-un [:view/private_metadata :view/callback_id :view/external_id]))

(s/def ::modal (s/keys :req-un [:modal/type :view/blocks :modal/title] :opt-un [:modal/close :modal/submit :view/private_metadata :view/callback_id :modal/clear_on_close :modal/notify_on_close :view/external_id]))
