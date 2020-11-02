(ns thlack.surfs.composition.components
  "A hiccup-like interface for creating views in Slack applications via blocks.
   https://api.slack.com/reference/block-kit/blocks"
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.props :as props]
            [thlack.surfs.props.spec :as props.spec]
            [thlack.surfs.validation :refer [validated]]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.composition.components.spec]
            [thlack.surfs.strings.spec :as strings.spec]))

(defn- create-text
  [props-or-string]
  (if (string? props-or-string)
    {:type :plain_text :text props-or-string}
    props-or-string))

(defn text
  "An object containing some text, formatted either as plain_text or using mrkdwn.
   
   If a map of props is given, it will be used to construct a text object.
   Otherwise a string literal is assumed and a default plain text object will be created.
   
   Component usage:
   
   ```clojure
   [:text \"Hello\"]
   
   [:text {:type :mrkdwn :verbatim false :text \"# Hello\"}]
   
   [:text {:type :plain_text :emoji true :text \"Hello\"}]
   ```"
  [props]
  (-> props
      (create-text)
      (validated ::comp.spec/text)))

(s/fdef text
  :args (s/cat :props ::props.spec/text)
  :ret  ::comp.spec/text)

(defn plain-text
  "Explicitly creates a plain text object.
   
   Component usage:
   
   ```clojure
   [:plain-text \"Hello\"]
   
   [:plain-text \"Goodbye\" false]
   
   [:plain-text {:text \"Greetings\" :emoji false}]
   ```"
  ([txt emoji?]
   (-> txt
       (create-text)
       (assoc :type :plain_text)
       (assoc :emoji emoji?)
       (validated ::comp.spec/plain-text)))
  ([text]
   (plain-text text (get text :emoji true))))

(s/fdef plain-text
  :args (s/cat :txt ::props.spec/text :emoji? (s/? boolean?))
  :ret  ::comp.spec/plain-text)

(defn markdown
  "Explicitly creates a markdown text object.
   
   Component usage:
   
   ```clojure
   [:markdown \"# Hello\"]

   [:markdown \"# Goodbye\" true]
   
   [:markdown {:text \"# Greetings\" :verbatim true}]
   ```"
  ([txt verbatim?]
   (-> txt
       (create-text)
       (assoc :type :mrkdwn)
       (assoc :verbatim verbatim?)
       (validated ::comp.spec/mrkdwn)))
  ([text]
   (markdown text (get text :verbatim false))))

(s/fdef markdown
  :args (s/cat :txt ::props.spec/text :verbatim? (s/? boolean?))
  :ret  ::comp.spec/mrkdwn)

(defn with-text
  "Updates the given keys to conform to a text element. Will run the given keys through
   the (text) function if they are present in the set of props."
  [props ks]
  (reduce-kv
   (fn [m k v]
     (if (and (some? (ks k)))
       (assoc m k (text v))
       (assoc m k v)))
   {}
   props))

(defn confirm
  "An object that defines a dialog that provides a confirmation step to any interactive element. 
   
   Component usage:
   
   ```clojure
   [:confirm {:confirm \"Ok!\" :deny \"Nah!\" :title \"This is a title!\"}
    [:text \"Are you sure?\"]]

   [:confirm {:confirm \"Ok!\" :deny \"Nah!\" :title \"This is a title!\"} \"Are you sure?\"]
   ```"
  [props txt]
  (-> props
      (props/with-plain-text #{:confirm :deny :title})
      (assoc :text (text txt))
      (assoc :style (get props :style :primary))
      (validated ::comp.spec/confirm)))

(s/fdef confirm
  :args (s/cat :props :confirm/props :txt (strings.spec/with-max-gen ::props.spec/text 300))
  :ret ::comp.spec/confirm)

(defn option
  "An object that represents a single selectable item in a select menu, multi-select menu,
   checkbox group, radio button group, or overflow menu.
   
   Component usage:
   
   ```clojure
   [:option {:value \"1\"} \"Label\"]
   
   [:option {:value \"1\"} {:type :plain_text :text \"Label\"}]
   
   [:option {:value \"1\" :description \"Oh hello\"} \"Label\"]
   ```
   
   Options used in elements supporting initial_option(s), also supported a :selected?
   property."
  [props txt]
  (-> props
      (props/with-plain-text #{:description})
      (assoc :text (text txt))
      (validated ::comp.spec/option)))

(s/fdef option
  :args (s/cat :props :option/props :txt (strings.spec/with-max-gen ::props.spec/plain-text 75))
  :ret ::comp.spec/option)

(s/def :option-group/children
  (s/with-gen
    (s/+ ::comp.spec/option)
    #(s/gen :option-group/options)))

(defn option-group
  "Provides a way to group options in a select menu or multi-select menu.
   
   Component usage:
   
   ```clojure
   [:option-group
    [:label \"Pizza Toppings\"]
    [:option {:value \"1\"} \"Mushrooms\"]
    [:option {:value \"2\"} \"Pepperoni\"]]
   ```"
  [label & children]
  (validated {:label   label
              :options (props/flatten-children children)} ::comp.spec/option-group))

(s/fdef option-group
  :args (s/cat :label :option-group/label :options :option-group/children)
  :ret ::comp.spec/option-group)
