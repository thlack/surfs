(ns ^:no-doc thlack.surfs.elements.spec.external-select
  (:require [clojure.spec.alpha :as s]))

(s/def ::type #{:external_select})

(s/def ::min_query_length pos-int?)
