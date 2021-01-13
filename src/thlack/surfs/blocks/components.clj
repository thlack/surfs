(ns thlack.surfs.blocks.components
  "A hiccup-like interface for creating views in Slack applications via blocks.
   https://api.slack.com/reference/block-kit/blocks"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.blocks.components.spec :as bc.spec]
            [thlack.surfs.composition.components :as comp]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.elements.components :as elements]
            [thlack.surfs.props :as props]
            [thlack.surfs.validation :refer [validated]]))

(defn actions
  "A block that is used to hold interactive elements.
   
   Component usage:
   
   ```clojure
   [:actions {:block_id \"B123\"}
    [:radio-buttons {:action_id \"A123\"}
     [:option {:value \"1\"} \"Pepperoni\"]
     [:option {:value \"2\" :selected? true} \"Pineapple\"]
     [:option {:value \"3\"} \"Mushrooms\"]]
    [:channels-select {:action_id \"A456\" :initial_channel \"C123\"}
     [:placeholder \"Select channel\"]]]
   ```
   
   Without props:
   
   ```clojure
   [:actions
    [:radio-buttons {:action_id \"A123\"}
     [:option {:value \"1\"} \"Pepperoni\"]
     [:option {:value \"2\" :selected? true} \"Pineapple\"]
     [:option {:value \"3\"} \"Mushrooms\"]]
    [:channels-select {:action_id \"A456\" :initial_channel \"C123\"}
     [:placeholder \"Select channel\"]]]
   ```"
  [& args]
  (let [[props & children] (props/parse-args args :block/props*)]
    (-> props
        (assoc :elements (props/flatten-children children) :type :actions)
        (validated ::blocks.spec/actions))))

(s/fdef actions
  :args (s/alt :props-and-children (s/cat :props :block/props :children :actions/children)
               :children (s/cat :children :actions/children))
  :ret ::blocks.spec/actions)

(defn fields
  "Not technically an element provided by Slack, but this component
   is useful for adding semantic value to section blocks.
   
   Component usage:
   
   ```clojure
   [:fields
    [:markdown \"# Field 1\"]
    [:plain-text \"Field 2\"]]
   ```"
  [& texts]
  {:fields
   (props/flatten-children texts)})

(s/fdef fields
  :args (s/cat :texts (s/+ ::comp.spec/text))
  :ret  ::blocks.spec/fields)

(defn- with-section-child
  "Conform a value to a spec describing a section child that may be
   one of several values"
  [props x]
  (if-not (some? x)
    props
    (let [child   (if (and (seq x) (not (map? x))) (first x) x)
          [tag _] (s/conform :section/child child)]
      (cond-> props
        (= :text tag) (assoc :text child)
        (= :accessory tag) (assoc :accessory child)
        (= :fields tag) (merge child)))))

(defn section
  "Can be used as a simple text block, or in combination with multiple text fields. Can contain
   a single accessory block element.
   
   The section block is unique in how it handles text and fields. When
   rendering a section block with 3 arguments - that is a props map and 2 children,
   some care must be taken. See the fdef for the section function to see the permutation of
   arguments supported.
   
   Component usage:
   
   ```clojure
   [:section {:block_id \"B123\"}
    [:text \"This is an important action\"]
    [:datepicker {:action_id \"A123\" :initial_date \"2020-11-30\"}
     [:placeholder \"The date\"]
     [:confirm {:confirm \"Ok!\" :deny \"Nah!\" :title \"You sure?!?!?\"}
      [:text \"This is irreversible!\"]]]]
   ```

   Without props:
   
   ```clojure
   [:section
    [:text \"This is an important action\"]
    [:datepicker {:action_id \"A123\" :initial_date \"2020-11-30\"}
     [:placeholder \"The date\"]
     [:confirm {:confirm \"Ok!\" :deny \"Nah!\" :title \"You sure?!?!?\"}
      [:text \"This is irreversible!\"]]]]
   ```"
  [& args]
  (let [[props & children] (props/parse-args args :block/props*)]
    (reduce with-section-child (assoc props :type :section) children)))

(s/fdef section
  :args (s/alt :props-and-text   (s/cat :props :block/props
                                        :text :section/text)
               :props-and-fields (s/cat :props :block/props
                                        :fields ::blocks.spec/fields)
               :props-and-text-and-accessory (s/cat :props :block/props
                                                    :text :section/text
                                                    :accessory :section/accessory)
               :props-and-accessory-and-text (s/cat :props :block/props
                                                    :accessory :section/accessory
                                                    :text :section/text)
               :props-and-fields-and-accessory (s/cat :props :block/props
                                                      :fields ::blocks.spec/fields
                                                      :accessory :section/accessory)
               :props-and-accessory-and-fields (s/cat :props :block/props
                                                      :accessory :section/accessory
                                                      :fields ::blocks.spec/fields)
               :props-and-text-and-fields (s/cat :props :block/props
                                                 :text :section/text
                                                 :fields ::blocks.spec/fields)
               :props-and-fields-and-text (s/cat :props :block/props
                                                 :fields ::blocks.spec/fields
                                                 :text :section/text)

               :text   (s/cat :text :section/text)
               :fields (s/cat :fields ::blocks.spec/fields)
               :text-and-accessory (s/cat :text :section/text
                                          :accessory :section/accessory)
               :accessory-and-text (s/cat :accessory :section/accessory
                                          :text :section/text)
               :fields-and-accessory (s/cat :fields ::blocks.spec/fields
                                            :accessory :section/accessory)
               :accessory-and-fields (s/cat :accessory :section/accessory
                                            :fields ::blocks.spec/fields)
               :text-and-fields (s/cat :text :section/text
                                       :fields ::blocks.spec/fields)
               :fields-and-text (s/cat :fields ::blocks.spec/fields
                                       :text :section/text)
               :all (s/cat :props :block/props
                           :text :section/text
                           :fields ::blocks.spec/fields
                           :accessory :section/accessory))
  :ret ::blocks.spec/section)

(defn context
  "Displays message context, which can include both images and text.
   
   Component usage:
   
   ```clojure
   [:context {:block_id \"B123\"}
    [:image {:alt_text \"It's Bill\" :image_url \"http://www.fillmurray.com/200/300\"}]
    [:text \"This is some text\"]]
   ```
   
   Without props:
   
   ```clojure
   [:context
    [:image {:alt_text \"It's Bill\" :image_url \"http://www.fillmurray.com/200/300\"}]
    [:text \"This is some text\"]]
   ```"
  [& args]
  (let [[props & children] (props/parse-args args :block/props*)]
    (-> props
        (assoc :elements (props/flatten-children children) :type :context)
        (validated ::blocks.spec/context))))

(s/fdef context
  :args (s/alt :props-and-children (s/cat :props :block/props :children :context/children)
               :children (s/cat :children :context/children))
  :ret ::blocks.spec/context)

(defn divider
  "A content divider. Functions much like HTML's <hr> element.
   
   Component usage:
   
   ```clojure
   [:divider]
   ```"
  ([props]
   (assoc props :type :divider))
  ([]
   (divider {})))

(s/fdef divider
  :args (s/cat :props (s/? :block/props))
  :ret ::blocks.spec/divider)

(defn header
  "A plain-text block that displays in a larger, bold font.
   
   Component usage:
   
   ```clojure
   [:header {:block_id \"B123\"} \"Hello\"]
   ```
   
   Without props:
   
   ```clojure
   [:header \"Hello\"]
   ```"
  ([props text]
   (-> props
       (assoc :type :header)
       (assoc :text (comp/text text))
       (validated ::blocks.spec/header)))
  ([text]
   (header {} text)))

(s/fdef header
  :args (s/alt :props-and-children (s/cat :props :block/props :text :header-child/text)
               :children (s/cat :text :header-child/text))
  :ret  ::blocks.spec/header)

(defn image
  "A simple image block.
   
   Component usage:
   
   ```clojure
   [:image {:image_url \"http://www.fillmurray.com/200/300\"
            :alt_text \"It's Bill\"
            :block_id \"B123\"}
    [:title \"Wowzers!\"]]
   ```"
  ([props title]
   (-> props
       (elements/img)
       (cond->
        (some? title) (assoc :title (comp/text title)))
       (validated ::blocks.spec/image)))
  ([props]
   (image props nil)))

(s/fdef image
  :args (s/cat :props :image/props :title (s/? :image-child/title))
  :ret ::blocks.spec/image)

(defn input
  "A block that collects information from users. In order to distinguish
   between hint and label children, the label child (or a child that evaluates to a plain text element) MUST be the first child
   included in the component.
   
   Component usage:
   
   ```clojure
   [:input {:block_id \"B123\" :dispatch_action false :optional false}
    [:label \"Some input\"]
    [:hint \"Do something radical\"]
    [:plain-text-input {:action_id \"A123\"
                        :initial_value \"hello\"}
     [:placeholder \"Greeting\"]]]
   ```
   
   Without props:
   
   ```clojure
   [:input
    [:label \"Some input\"]
    [:hint \"Do something radical\"]
    [:plain-text-input {:action_id \"A123\"
                        :initial_value \"hello\"}
     [:placeholder \"Greeting\"]]]
   ```"
  [& args]
  (let [[props label & children] (props/parse-args args bc.spec/input-props?)]
    (-> (assoc props :type :input)
        (assoc :label (comp/text label))
        (props/with-children children :input/child)
        (validated ::blocks.spec/input))))

(s/fdef input
  :args (s/alt :props-and-children (s/cat :props :input/props :label :input-child/label :children :input/children)
               :children (s/cat :label :input-child/label :children :input/children))
  :ret  ::blocks.spec/input)
