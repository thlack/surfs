(ns thlack.surfs.elements.components-test
  (:require [thlack.surfs.elements.components :as co]
            [thlack.surfs.test-utils :refer [defcheck]]))

(def num-tests 50)

(defcheck button `co/button num-tests)

(defcheck checkboxes `co/checkboxes num-tests)

(defcheck datepicker `co/datepicker num-tests)

(defcheck timepicker `co/timepicker num-tests)

(defcheck img `co/img num-tests)

(defcheck multi-static-select `co/multi-static-select 10)

(defcheck multi-external-select `co/multi-external-select 10)

(defcheck multi-users-select `co/multi-users-select num-tests)

(defcheck multi-conversations-select `co/multi-conversations-select num-tests)

(defcheck multi-channels-select `co/multi-channels-select num-tests)

(defcheck static-select `co/static-select 10)

(defcheck external-select `co/external-select 10)

(defcheck users-select `co/users-select num-tests)

(defcheck conversations-select `co/conversations-select num-tests)

(defcheck channels-select `co/channels-select num-tests)

(defcheck overflow `co/overflow num-tests)

(defcheck plain-text-input `co/plain-text-input num-tests)

(defcheck radio-buttons `co/radio-buttons num-tests)
