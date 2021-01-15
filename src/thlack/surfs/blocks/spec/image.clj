(ns thlack.surfs.blocks.spec.image
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

(s/def ::type #{:image})

(deftext ::image_url ::strings.spec/url-string 3000)

(deftext ::alt_text ::strings.spec/string 2000)

(deftext ::title ::comp.spec/plain-text 2000)
