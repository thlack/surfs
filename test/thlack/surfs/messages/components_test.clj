(ns thlack.surfs.messages.components-test
  (:require [thlack.surfs.messages.components :as co]
            [thlack.surfs.test-utils :refer [defcheck]]))

(defcheck message `co/message 5)
