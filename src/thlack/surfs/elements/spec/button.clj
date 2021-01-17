(ns ^:no-doc thlack.surfs.elements.spec.button
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:button})

(deftext ::text ::comp.spec/plain-text 75)

(s/def ::action_id ::strings.spec/action_id)

(deftext ::url ::strings.spec/url-string 3000)

(deftext ::value ::strings.spec/string 2000)

(s/def ::style #{:primary :danger})
