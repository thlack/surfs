(ns ^:no-doc thlack.surfs.elements.spec.timepicker
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:timepicker})

(s/def ::initial_time ::strings.spec/time-string)

(deftext ::placeholder ::comp.spec/plain-text 150)
