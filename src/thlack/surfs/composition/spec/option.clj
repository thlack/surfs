(ns ^:no-doc thlack.surfs.composition.spec.option
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec.plain-text :as plain-text]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(deftext ::text ::plain-text/plain-text 75)

(deftext ::value ::strings.spec/string 75)

(deftext ::description ::plain-text/plain-text 75)

(s/def ::option (s/keys :req-un [::text ::value] :opt-un [::description]))
