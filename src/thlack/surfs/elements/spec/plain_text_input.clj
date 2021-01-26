(ns ^:no-doc thlack.surfs.elements.spec.plain-text-input
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:plain_text_input})

(deftext ::placeholder ::comp.spec/plain-text 150)

(s/def ::initial_value ::strings.spec/string)

(s/def ::multiline boolean?)

(s/def ::min_length (s/int-in 1 3001))

(s/def ::max_length pos-int?)
