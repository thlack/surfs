(ns ^:no-doc thlack.surfs.blocks.spec.file
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:file})

(s/def ::external_id ::strings.spec/string)

(s/def ::source #{:remote})
