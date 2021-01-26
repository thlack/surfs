
(ns ^:no-doc thlack.surfs.messages.components.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.messages.spec :as message]))

;;; [:message]

(s/def ::message.props
  (s/keys :opt-un [::message/thread_ts ::message/mrkdwn]))

(defn message-props?
  [props]
  (if (map? props)
    (-> props
        (select-keys [:thread_ts :mrkdwn])
        (seq)
        (some?))
    false))

(s/def ::message.child (s/or :block ::blocks.spec/block :text ::message/text))

(s/def ::message.children
  (s/with-gen
    (s/* ::message.child)
    #(s/gen ::message/blocks)))
