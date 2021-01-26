(ns ^:no-doc thlack.surfs.composition.spec.mrkdwn
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.text :as text]))

(s/def ::type #{:mrkdwn})

(s/def ::mrkdwn (s/keys :req-un [::type ::text/text] :opt-un [::text/verbatim]))
