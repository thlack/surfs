(ns ^:no-doc thlack.surfs.views.spec.modal
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :refer [deftext]]))

(s/def ::type #{:modal})

(deftext ::title ::comp.spec/plain-text 24)

(deftext ::close ::comp.spec/plain-text 24)

(deftext ::submit ::comp.spec/plain-text 24)

(s/def ::clear_on_close boolean?)

(s/def ::notify_on_close boolean?)
