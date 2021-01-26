(ns ^:no-doc thlack.surfs.elements.spec.users-select
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.strings.spec :as strings.spec]))

(s/def ::type #{:users_select})

(s/def ::initial_user ::strings.spec/id)
