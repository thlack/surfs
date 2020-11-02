(ns thlack.surfs.blocks.components-test
  (:require [thlack.surfs.blocks.components :as co]
            [thlack.surfs.test-utils :refer [defcheck]]))

(def num-tests 10)

(defcheck actions `co/actions num-tests)

(defcheck fields `co/fields num-tests)

(defcheck section `co/section num-tests)

(defcheck context `co/context num-tests)

(defcheck divider `co/divider num-tests)

(defcheck header `co/header num-tests)

(defcheck image `co/image num-tests)

(defcheck input `co/input num-tests)
