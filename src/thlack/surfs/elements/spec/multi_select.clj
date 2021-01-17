(ns ^:no-doc thlack.surfs.elements.spec.multi-select
  (:require [clojure.spec.alpha :as s]))

(s/def ::max_selected_items pos-int?)
