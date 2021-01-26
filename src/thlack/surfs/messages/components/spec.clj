
(ns ^:no-doc thlack.surfs.messages.components.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.messages.spec :as message]))

;;; [:message]

(s/def ::message.props
  (s/keys :opt-un [::message/thread_ts ::message/mrkdwn]))

(defn- block?
  [props]
  (if (map? props)
    (-> props
        (select-keys [:type :block_id])
        (seq)
        (some?))
    false))

;;; To account for all possible message properties - we will consider any non-block a valid map of props
;;; for the purpose of parsing message component arguments
(defn message-props?
  [props]
  (if (block? props)
    false
    (map? props)))

(s/def ::message.child (s/or :block ::blocks.spec/block :text ::message/text))

(s/def ::message.children
  (s/with-gen
    (s/* ::message.child)
    #(s/gen ::message/blocks)))
