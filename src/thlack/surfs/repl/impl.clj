(ns ^:no-doc thlack.surfs.repl.impl
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [thlack.surfs.render :as render]))

(defn- get-var
  [tag]
  (render/tags tag))

(defn- get-meta
  [tag]
  (some-> tag
          (get-var)
          (meta)
          (select-keys [:doc :name])))

(defn- get-spec
  [tag]
  (some->> tag
           (get-var)
           (s/get-spec)
           (s/describe)
           (next)
           (apply hash-map)))

(defn- with-spec
  [tag description]
  (assoc description :spec (get-spec tag)))

(defn describe
  [tag]
  (->> tag
       (get-meta)
       (with-spec tag)
       (merge {:tag tag})))

(defn- get-args
  [{{:keys [args]} :spec}]
  args)

(defn- get-cats'
  [args]
  (let [head (first args)]
    (if (= 'alt head)
      (vals (apply hash-map (rest args)))
      (list args))))

(defn- get-cats
  [args]
  (->> args
       (get-cats')
       (map (fn [c]
              (map (fn [x]
                     (if (seq? x)
                       (second x)
                       x)) c)))))

(defn- signature-string
  "Get an arglist based on a function spec."
  [{:keys [tag] :as description}]
  (->> (get-args description)
       (get-cats)
       (map rest)
       (map #(map symbol %))
       (map #(take-nth 2 %))
       (map vec)
       (map #(into [tag] %))
       (pr-str)))

(defn- doc-string
  "Get the doc string for a component description"
  [description]
  (some-> description
          (:doc)
          (string/replace #"^" "   ")
          (string/split-lines)
          (#(string/join (System/lineSeparator) %))))

(defn doc
  [tag]
  (let [description (describe tag)]
    (->> description
         (doc-string)
         (str (signature-string description) (System/lineSeparator))
         (println))))

(defn- expand-prop
  [prop]
  (if (qualified-keyword? prop)
    (s/describe prop)
    prop))

(defn props
  [tag]
  (some->> tag
           (describe)
           (get-args)
           (get-cats)
           (map #(apply hash-map (rest %)))
           (filter #(contains? % :props))
           (first)
           :props
           (s/describe)
           (map expand-prop)))
