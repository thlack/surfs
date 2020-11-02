(ns ^:no-doc thlack.surfs.messages.spec
  "Follows the common structure for ALL message payloads
   documented at https://api.slack.com/reference/messaging/payload"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]
            [thlack.surfs.blocks.spec :as blocks.spec]))

(deftext :message/text ::strings.spec/string 40000)

(s/def :message/blocks (s/coll-of ::blocks.spec/block :into [] :max-count 50 :min-count 1 :gen-max 3))

(s/def :message/thread_ts ::strings.spec/string)

(s/def :message/mrkdwn boolean?)

(s/def ::message-optional-blocks (s/keys :req-un [:message/text] :opt-un [:message/blocks :message/thread_ts :message/mrkdwn]))

(s/def ::message-optional-text (s/keys :req-un [:message/blocks] :opt-un [:message/text :message/thread_ts :message/mrkdwn]))

(s/def ::message
  (s/or :optional-blocks ::message-optional-blocks :optional-text ::message-optional-text))
