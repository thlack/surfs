(ns thlack.surfs.test-utils
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer [deftest is]]
            [clojure.spec.test.alpha :as st]
            [expound.alpha :as expound]
            [thlack.surfs :as surfs]))

(set! s/*explain-out* expound/printer)

(defn check
  [sym num-tests]
  (let [check-result (st/check sym {:clojure.spec.test.check/opts {:num-tests num-tests}})
        result (-> check-result
                   first
                   :clojure.spec.test.check/ret
                   :result)]
    (when-not (true? result)
      (expound/explain-result check-result))
    result))

(defmacro defcheck
  [name sym num-tests]
  `(deftest ~name
     (is (true? (check ~sym ~num-tests)))))

(defmacro defrendertest
  [name & children]
  (let [components (vec (butlast children))
        assert-fn  (last children)]
    `(deftest ~name
       (~assert-fn (apply surfs/render ~components)))))
