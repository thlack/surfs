(ns thlack.surfs.composition.spec.text
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::text ::strings.spec/string)

(s/def ::emoji boolean?)

(s/def ::verbatim boolean?)
