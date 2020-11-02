(ns thlack.surfs.views.components-test
  (:require [thlack.surfs.views.components :as co]
            [thlack.surfs.test-utils :refer [defcheck]]))

(defcheck home `co/home 5)

(defcheck modal `co/modal 5)
