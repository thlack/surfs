(ns ^:no-doc thlack.surfs.composition.spec
  "This namespace contains specs for what Slack refers to as
   composition objects"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def :plain_text/type #{:plain_text})

(s/def :mrkdwn/type #{:mrkdwn})

(s/def :text/text ::strings.spec/string)

(s/def :text/emoji boolean?)

(s/def :text/verbatim boolean?)

(s/def ::plain-text (s/keys :req-un [:plain_text/type :text/text] :opt-un [:text/emoji]))

(s/def ::mrkdwn (s/keys :req-un [:mrkdwn/type :text/text] :opt-un [:text/verbatim]))

(s/def ::text (s/or :plain_text ::plain-text :mrkdwn ::mrkdwn))

(deftext :confirm/title ::plain-text 100)

(deftext :confirm/text ::text 300)

(deftext :confirm/confirm ::plain-text 30)

(deftext :confirm/deny ::plain-text 30)

(s/def :confirm/style #{:primary :danger})

(s/def ::confirm (s/keys :req-un [:confirm/title :confirm/text :confirm/confirm :confirm/deny] :opt-un [:confirm/style]))

(s/def :conversation-filter/include (s/coll-of #{:im :mpim :private :public} :distinct true :into [] :gen-max 4 :min-count 1))

(s/def :conversation-filter/exclude_external_shared_channels boolean?)

(s/def :conversation-filter/exclude_bot_users boolean?)

(s/def :conversation/filter (s/keys :opt-un [:conversation-filter/include :conversation-filter/exclude_external_shared_channels :conversation-filter/exclude_bot_users]))

(s/def :dispatch-action-config/trigger_actions_on (s/coll-of #{:on_enter_pressed :on_character_entered} :min-count 1 :distinct true :gen-max 2 :into []))

(s/def ::dispatch_action_config (s/keys :req-un [:dispatch-action-config/trigger_actions_on]))

(deftext :option/text ::plain-text 75)

(deftext :option/value ::strings.spec/string 75)

(deftext :option/description ::plain-text 75)

(s/def ::option (s/keys :req-un [:option/text :option/value] :opt-un [:option/description]))

(deftext :option-group/label ::plain-text 75)

(s/def :option-group/options (s/coll-of ::option :min-count 1 :max-count 100 :into [] :gen-max 10))

(s/def ::option-group (s/keys :req-un [:option-group/label :option-group/options]))
