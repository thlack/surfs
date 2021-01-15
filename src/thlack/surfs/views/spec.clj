(ns ^:no-doc thlack.surfs.views.spec
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.views.spec.modal :as modal]
            [thlack.surfs.views.spec.home :as home]))

(s/def ::blocks (s/coll-of ::blocks.spec/block :into [] :max-count 100 :min-count 1 :gen-max 3))

(s/def ::private_metadata (strings.spec/with-max-gen
                            ::strings.spec/string
                            3000))

(s/def ::callback_id (strings.spec/with-max-gen
                       ::strings.spec/string
                       255))

(s/def ::external_id ::strings.spec/string)

(s/def ::home (s/keys :req-un [::home/type ::blocks] :opt-un [::private_metadata ::callback_id ::external_id]))

(s/def ::modal (s/keys :req-un [::modal/type ::blocks ::modal/title] :opt-un [::modal/close ::modal/submit ::private_metadata ::callback_id ::modal/clear_on_close ::modal/notify_on_close ::external_id]))
