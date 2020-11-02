(ns ^:no-doc thlack.surfs.props
  (:require [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [thlack.surfs.blocks.spec]))

;;; Prop helpers

(defn with-plain-text
  "Adds support for the disable_emoji_for prop. This is for props that represent
   simple plain text strings."
  [{:keys [disable_emoji_for] :as props :or {disable_emoji_for #{}}} text-keys]
  (reduce-kv
   (fn [m k v]
     (if (some? (text-keys k))
       (assoc m k {:type :plain_text :text v :emoji (not (boolean (disable_emoji_for k)))})
       (assoc m k v)))
   {}
   (dissoc props :disable_emoji_for)))

(defn parse-args
  "Used for supporting components that may not require any props."
  [args]
  (let [head (first args)]
    (if (and (> (count args) 1) (map? head))
      (into [head] (rest args))
      (into [{}] args))))

;;; Children

(defmacro conformed
  "Similar to the validated macro, this conforms data to a spec or throws an informative
   exception."
  [x spec]
  `(let [conformed# (s/conform ~spec ~x)]
     (if (s/invalid? conformed#)
       (throw (ex-info (expound/expound-str ~spec ~x)
                       (or (s/explain ~spec ~x) {:explained? false})))
       conformed#)))

(defn- detag
  "Detag data coming in via children. If a spec is provided, the data will be shaped by
   calling clojure.spec.alpha/unform - which may be trading ease for some performance"
  ([data spec]
   (s/unform spec data))
  ([data]
   (if (map-entry? data)
     (val data)
     data)))

(defn assoc-child
  "Associate a child element with a collection of children. This function will be called with
   a 'children' map and a tuple containing a tag and the conformed data."
  [children [tag data]]
  (condp some [tag]
    #{:plain-text}         (assoc children :text (detag data))
    #{:confirm}            (assoc children tag (update data :text detag))
    #{:option}             (update children :options conj data)
    #{:option-group}       (update children :option_groups conj data)
    #{:text}               (assoc children :text (detag data))
    #{:fields}             (merge children (update data :fields #(map second %)))
    #{:accessory}          (assoc children tag (detag data :section/accessory))
    #{:element}            (assoc children tag (detag data :input/element))
    (assoc children tag data)))

(defn flatten-children
  "Handles flattening children of an element or block. If a child is a sequence,
   it will be merged into the set of children. This is what makes it possible to include
   things like map expressions in children - i.e [:option-group (map make-options props)]"
  [coll]
  (->> coll
       (filter some?)
       (reduce
        (fn [children child]
          (into children (if (seq? child) child [child])))
        [])))

(defmacro with-children
  "Include children in a spec-safe way. 'children' here is meant to imply
   nested composition objects or elements, such as a section's accessory or a select element's
   options."
  [props children spec]
  `(if-let [filtered# (seq (flatten-children ~children))]
     (let [children# (conformed filtered# (s/coll-of ~spec :into []))]
       (merge ~props (reduce assoc-child {} (rseq children#))))
     ~props))
