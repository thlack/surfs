(ns thlack.surfs.views.components
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.props :as props]
            [thlack.surfs.validation :refer [validated]]
            [thlack.surfs.views.spec :as views.spec]
            [thlack.surfs.views.components.spec]))

(defn- with-private-metadata
  "Supports private_metadata as Clojure data structures. If a private_metadata
   prop is given, it will have (pr-str) applied to it."
  [{:keys [private_metadata] :as props}]
  (if (some? private_metadata)
    (assoc props :private_metadata (pr-str private_metadata))
    props))

(defn home
  "Define a home tab.
   
   Component usage:
   
   ```clojure
   [:home {:private_metadata {:cool? true}}
    [:section {:block_id \"B123\"}
     [:text \"Some text\"]]]
   ```
   
   Without props:
   
   ```clojure
   [:home
    [:section {:block_id \"B123\"}
     [:text \"Some text\"]]
   ```"
  [& args]
  (let [[props & blocks] (props/parse-args args)]
    (-> (assoc props :type :home)
        (with-private-metadata)
        (assoc :blocks (props/flatten-children blocks))
        (validated ::views.spec/home))))

(s/fdef home
  :args (s/alt :props-and-blocks (s/cat :props :view/props :children :view/children)
               :blocks (s/cat :children :view/children))
  :ret ::views.spec/home)

(defn modal
  "Define a modal.
   
   Component usage:
   
   ```clojure
   [:modal {:private_metadata {:cool? true}
            :title \"Cool Modal!\"
            :close \"Nah!\"
            :submit \"Yah!\"}
   [:section {:block_id \"B123\"}
    [:text \"Some text\"]]]
   ```"
  [props & blocks]
  (-> (assoc props :type :modal)
      (with-private-metadata)
      (props/with-plain-text #{:title :close :submit})
      (assoc :blocks (props/flatten-children blocks))
      (validated ::views.spec/modal)))

(s/fdef modal
  :args (s/cat :props :modal/props :children :view/children)
  :ret ::views.spec/modal)
