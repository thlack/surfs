(ns thlack.surfs.composition.spec.confirm
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.plain-text :as plain-text]
            [thlack.surfs.composition.spec.mrkdwn :as mrkdwn]
            [thlack.surfs.strings.spec :refer [deftext]]))

(s/def ::text* (s/or :plain_text ::plain-text/plain-text :mrkdwn ::mrkdwn/mrkdwn))

(deftext ::title ::plain-text/plain-text 100)

(deftext ::text ::text* 300)

(deftext ::confirm ::plain-text/plain-text 30)

(deftext ::deny ::plain-text/plain-text 30)

(s/def ::style #{:primary :danger})
