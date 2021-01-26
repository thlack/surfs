(ns ^:no-doc thlack.surfs.elements.spec.image
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:image})

(s/def ::image_url ::strings.spec/url-string)

(s/def ::alt_text ::strings.spec/string)
