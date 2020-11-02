(ns thlack.surfs.composition.components-test
  (:require  [thlack.surfs.composition.components :as co]
             [thlack.surfs.test-utils :refer [defcheck]]))

(def num-tests 100)

(defcheck text `co/text num-tests)

(defcheck plain-text `co/plain-text num-tests)

(defcheck markdown `co/markdown num-tests)

(defcheck confirm `co/confirm num-tests)

(defcheck option `co/option num-tests)

(defcheck option-group `co/option-group num-tests)
