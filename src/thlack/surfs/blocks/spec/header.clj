(ns thlack.surfs.blocks.spec.header
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :refer [deftext]]))

(s/def ::type #{:header})

(deftext ::text ::comp.spec/plain-text 150)
