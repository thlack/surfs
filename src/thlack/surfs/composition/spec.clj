(ns ^:no-doc thlack.surfs.composition.spec
  "This namespace contains specs for what Slack refers to as
   composition objects"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.plain-text :as plain-text]
            [thlack.surfs.composition.spec.mrkdwn :as mrkdwn]
            [thlack.surfs.composition.spec.confirm :as confirm]
            [thlack.surfs.composition.spec.dispatch-action-config :as dispatch-action-config]
            [thlack.surfs.composition.spec.option :as option]
            [thlack.surfs.composition.spec.option-group :as option-group]))

(s/def ::plain-text ::plain-text/plain-text)

(s/def ::mrkdwn ::mrkdwn/mrkdwn)

(s/def ::text (s/or :plain_text ::plain-text :mrkdwn ::mrkdwn))

(s/def ::confirm (s/keys :req-un [::confirm/title ::confirm/text ::confirm/confirm ::confirm/deny] :opt-un [::confirm/style]))

(s/def ::dispatch_action_config (s/keys :req-un [::dispatch-action-config/trigger_actions_on]))

(s/def ::option ::option/option)

(s/def ::option-group (s/keys :req-un [::option-group/label ::option-group/options]))
