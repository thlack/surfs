(ns ^:no-doc thlack.surfs.strings.spec
  "Contains specs for the various string types leveraged in the Slack block kit"
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.string :as string])
  (:import [java.net URL]
           [java.time LocalDate]
           [java.time.format DateTimeFormatter]))

(s/def ::string (s/and string? (complement string/blank?)))

(defn len-lte?
  [s len]
  (<= (count s) len))

(defn max-len
  "Returns a predicate that checks if string literals, maps, and map entries
   containing text do not exceed a given length"
  [len]
  (fn [s]
    (cond
      (string? s) (len-lte? s len)
      (map? s) (len-lte? (:text s) len)
      (map-entry? s) (len-lte? (:text (val s)) len))))

(defn with-max-gen
  "Create a spec with a generator that ensures text does not exceed a given
   length"
  [spec len]
  (letfn [(truncate [s] (subs s 0 (min (count s) len)))]
    (s/with-gen
      spec
      #(gen/fmap
        (fn [s]
          (cond
            (string? s) (truncate s)
            (map? s) (update s :text truncate)
            (map-entry? s) (update-in s [1 :text] truncate)))
        (s/gen spec)))))

(defmacro deftext
  "Define a text spec that adhers to a maximum length"
  [k spec len]
  `(s/def ~k
     (with-max-gen
       (s/and ~spec (max-len ~len))
       ~len)))

(s/def ::date-string* (s/and ::string (fn [s]
                                        (try
                                          (LocalDate/parse s)
                                          true
                                          (catch Exception _
                                            false)))))

(s/def ::date-string (s/with-gen
                       ::date-string*
                       #(gen/return (-> (LocalDate/now)
                                        (.format (DateTimeFormatter/ofPattern "yyyy-MM-dd"))))))

(s/def ::time-string* #(re-find #"^(?:[0-1][0-9]|2[0-3]):[0-5][0-9]$" %))

(s/def ::time-string (s/with-gen
                       ::time-string*
                       (fn []
                         (let [left (rand-int 24)
                               right (rand-int 60)]
                           (gen/return (format "%02d:%02d" left right))))))

(s/def ::url-string* (s/and ::string (fn [s]
                                       (try
                                         (URL. s)
                                         true
                                         (catch Exception _
                                           false)))))

(s/def ::url-string (s/with-gen
                      ::url-string*
                      #(gen/fmap
                        (fn [uri]
                          (str uri))
                        (s/gen uri?))))

(s/def ::id (s/and ::string (max-len 255)))

(s/def ::action_id ::id)

(s/def ::block_id ::id)
