(ns thlack.surfs.elements.spec.datepicker
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:datepicker})

(deftext ::placeholder ::comp.spec/plain-text 150)

(s/def ::initial_date ::strings.spec/date-string)
