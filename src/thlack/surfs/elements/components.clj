(ns thlack.surfs.elements.components
  "A hiccup-like interface for creating views in Slack applications via blocks.
   https://api.slack.com/reference/block-kit/blocks"
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :as walk]
            [thlack.surfs.props :as props]
            [thlack.surfs.validation :refer [validated]]
            [thlack.surfs.elements.spec :as elements.spec]
            [thlack.surfs.elements.components.spec]
            [thlack.surfs.composition.components :as comp]
            [thlack.surfs.composition.spec]))

;;; options and option-groups

(defn- assoc-initial-options
  "Look for options that have a key of :selected? set to true. Options marked as selected
   will be used to create initial_option(s)."
  [{:keys [type options option_groups] :as element :or {options [] option_groups []}}]
  (if-let [selected (seq (filter :selected? (concat options (mapcat :options option_groups))))]
    (condp some [type]
      #{:static_select :external_select :radio_buttons}
      (assoc element :initial_option (first selected))
      #{:checkboxes :multi_static_select :multi_external_select}
      (assoc element :initial_options selected))
    element))

(defn- conform-options
  "Removers userland keys such as :selected? so Slack doesn't
   error on us for unknown keys."
  [option-element]
  (walk/postwalk
   (fn [x]
     (if (map? x)
       (dissoc x :selected?)
       x))
   option-element))

(defn- force-selected
  "Forces all options to be selected. This is the case for options provided
   to external selects."
  [option-element]
  (cond-> option-element
    (contains? option-element :options)
    (update :options (partial map #(assoc % :selected? true)))
    (contains? option-element :option_groups)
    (update :option_groups (partial map #(force-selected %)))
    :always
    (identity)))

;;; Elements

(defn button
  "An interactive component that inserts a button. The button can be a trigger for
   anything from opening a simple link to starting a complex workflow.
   
   Component usage:
   
   ```clojure
   [:button {:action_id \"A123\" :value \"1\"} \"Click Me!\"]
   ```"
  [props & children]
  (-> (assoc props :type :button)
      (props/with-children children :button/child)
      (comp/with-text #{:text})
      (validated ::elements.spec/button)))

(s/fdef button
  :args (s/cat :props :button/props :children :button/children)
  :ret ::elements.spec/button)

(defn checkboxes
  "A checkbox group that allows a user to choose multiple items from a list of possible options.
   
   Component usage:
   
   ```clojure
   [:checkboxes {:action_id \"A123\"}
    [:option {:value \"1\"} \"Mushrooms\"]
    [:option {:value \"2\" :selected? true} \"Pepperoni\"]]
   ```"
  [props & children]
  (-> (assoc props :type :checkboxes)
      (props/with-children children :checkboxes/child)
      (assoc-initial-options)
      (validated ::elements.spec/checkboxes)
      (conform-options)))

(s/fdef checkboxes
  :args (s/cat :props :checkboxes/props :children :checkboxes/children)
  :ret ::elements.spec/checkboxes)

(defn datepicker
  "An element which lets users easily select a date from a calendar style UI.
   
   Component usage:
   
   ```clojure
   [:datepicker {:action_id \"A123\" :initial_date \"2020-11-30\"}
    [:placeholder \"The date\"]]
   ```"
  [props & children]
  (-> (assoc props :type :datepicker)
      (props/with-children children :datepicker/child)
      (validated ::elements.spec/datepicker)))

(s/fdef datepicker
  :args (s/cat :props :datepicker/props :children (s/* :datepicker/child))
  :ret ::elements.spec/datepicker)

(defn timepicker
  "An element which allows selection of a time of day.
   
   Component usage:
   
   ```clojure
   [:timepicker {:action_id \"A123\" :initial_time \"12:30\"}
    [:placeholder \"The time\"]]
   ```"
  [props & children]
  (-> (assoc props :type :timepicker)
      (props/with-children children :timepicker/child)
      (validated ::elements.spec/timepicker)))

(s/fdef timepicker
  :args (s/cat :props :timepicker/props :children (s/* :timepicker/child))
  :ret ::elements.spec/timepicker)

(defn img
  "Render function for the image element - not the image layout block. Block kit
   defines both, and this function is named after the img html element to differentiate
   between the two.
   
   An element to insert an image as part of a larger block of content.
   
   Component usage:
   
   ```clojure
   [:img {:image_url \"http://www.fillmurray.com/200/300\" :alt_text \"It's Bill Murray\"}]
   ```"
  [props]
  (-> props
      (assoc :type :image)
      (validated ::elements.spec/image)))

(s/fdef img
  :args (s/cat :props :img/props)
  :ret ::elements.spec/image)

(defn multi-static-select
  "A multi-select menu allows a user to select multiple items from a list of options.
   
   Component usage:
   
   ```clojure
   [:multi-static-select {:action_id \"A123\" :max_selected_items 5}
    [:placeholder \"Pizza Toppings\"]
    [:option {:value \"1\"} \"Mushrooms\"]
    [:option {:value \"2\" :selected? true} \"Pepperoni\"]
    [:option {:value \"3\" :selected? true} \"Cheese\"]]
   ```
   
   Supports option groups as well:
   
   ```clojure
   [:multi-static-select {:action_id \"A123\" :max_selected_items 5}
    [:placeholder \"Pizza Toppings\"]
    [:option-group
     [:label \"Veggies\"]
     [:option {:value \"1\"} \"Mushrooms\"]
     [:option {:value \"2\" :selected? true} \"Peppers\"]]
    [:option-group
     [:label \"Meats\"]
     [:option {:value \"3\"} \"Pepperoni\"]
     [:option {:value \"4\" :selected? true} \"Ham\"]]]
   ```"
  [props & children]
  (-> (assoc props :type :multi_static_select)
      (props/with-children children :multi-static-select/child)
      (assoc-initial-options)
      (validated ::elements.spec/multi-static-select)
      (conform-options)))

(s/fdef multi-static-select
  :args (s/cat :props :multi-select/props :children :multi-static-select/children)
  :ret  ::elements.spec/multi-static-select)

(defn multi-external-select
  "This menu will load its options from an external data source, allowing for a dynamic list of options.
   External selects will treat all options as initial_options, regardless of whether or not the :selected?
   prop is given.
   
   Component usage:
   
   ```clojure
   [:multi-external-select {:action_id \"A123\" :max_selected_items 5 :min_query_length 3}
    [:placeholder \"Pizza Toppings\"]
    [:option {:value \"1\"} \"Pepperoni\"]
    [:option {:value \"2\"} \"Mushrooms\"]]
   ```"
  [props & children]
  (-> (assoc props :type :multi_external_select)
      (props/with-children children :multi-static-select/child)
      (force-selected)
      (assoc-initial-options)
      (dissoc :option_groups :options)
      (validated ::elements.spec/multi-external-select)
      (conform-options)))

(s/fdef multi-external-select
  :args (s/cat :props :multi-external-select/props :children :multi-static-select/children)
  :ret ::elements.spec/multi-external-select)

(defn multi-users-select
  "This multi-select menu will populate its options with a list of Slack users visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:multi-users-select {:action_id \"A123\" :max_selected_items 3 :initial_users [\"U123\" \"U456\"]}
    [:placeholder \"Team captains\"]]
   ```"
  [props & children]
  (-> (assoc props :type :multi_users_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/multi-users-select)))

(s/fdef multi-users-select
  :args (s/cat :props :multi-users-select/props :children :slack-select/children)
  :ret ::elements.spec/multi-users-select)

(defn multi-conversations-select
  "This multi-select menu will populate its options with a list of public and private channels, DMs, and MPIMs visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:multi-conversations-select {:action_id \"A123\"
                                 :max_selected_items 3
                                 :default_to_current_conversation true
                                 :initial_conversations [\"C123\" \"C456\"]
                                 :filter {:include #{:private}
                                          :exclude_bot_users true
                                          :exclude_external_shared_channels true}}
    [:placeholder \"Select conversation\"]]
   ```"
  [props & children]
  (-> (assoc props :type :multi_conversations_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/multi-conversations-select)))

(s/fdef multi-conversations-select
  :args (s/cat :props :multi-conversations-select/props :children :slack-select/children)
  :ret ::elements.spec/multi-conversations-select)

(defn multi-channels-select
  "This multi-select menu will populate its options with a list of public channels visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:multi-channels-select {:action_id \"A123\" :max_selected_items 3 :initial_channels [\"C123\" \"C456\"]}
    [:placeholder \"Select channel\"]]
   ```"
  [props & children]
  (-> (assoc props :type :multi_channels_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/multi-channels-select)))

(s/fdef multi-channels-select
  :args (s/cat :props :multi-channels-select/props :children :slack-select/children)
  :ret ::elements.spec/multi-channels-select)

(defn static-select
  "This is the simplest form of select menu, with a static list of options passed in when defining the element.
   
   Component usage:
   
   ```clojure
   [:static-select {:action_id \"A123\"}
    [:placeholder \"Pizza Toppings\"]
    [:option {:value \"1\"} \"Mushrooms\"]
    [:option {:value \"2\" :selected? true} \"Pepperoni\"]
    [:option {:value \"3\"} \"Cheese\"]]
   ```
   
   Supports option groups as well:
   
   ```clojure
   [:static-select {:action_id \"A123\"}
    [:placeholder \"Pizza Toppings\"]
    [:option-group
     [:label \"Veggies\"]
     [:option {:value \"1\"} \"Mushrooms\"]
     [:option {:value \"2\" :selected? true} \"Peppers\"]]
    [:option-group
     [:label \"Meats\"]
     [:option {:value \"3\"} \"Pepperoni\"]
     [:option {:value \"4\"} \"Ham\"]]]
   ```"
  [props & children]
  (-> (assoc props :type :static_select)
      (props/with-children children :static-select/child)
      (assoc-initial-options)
      (validated ::elements.spec/static-select)
      (conform-options)))

(s/fdef static-select
  :args (s/cat :props :static-select/props :children :static-select/children)
  :ret ::elements.spec/static-select)

(defn external-select
  "This select menu will load its options from an external data source, allowing for a dynamic list of options.
   External selects will treat all options as initial_options, regardless of whether or not the :selected?
   prop is given.
   
   Component usage:
   
   ```clojure
   [:external-select {:action_id \"A123\" :min_query_length 3}
    [:placeholder \"Pizza Toppings\"]
    [:option {:value \"1\"} \"Pepperoni\"]]
   ```"
  [props & children]
  (-> (assoc props :type :external_select)
      (props/with-children children :static-select/child)
      (force-selected)
      (assoc-initial-options)
      (dissoc :options)
      (validated ::elements.spec/external-select)
      (conform-options)))

(s/fdef external-select
  :args (s/cat :props :external-select/props :children :static-select/children)
  :ret  ::elements.spec/external-select)

(defn users-select
  "This select menu will populate its options with a list of Slack users visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:users-select {:action_id \"A123\" :initial_user \"U123\"}
    [:placeholder \"Team captain\"]]
   ```"
  [props & children]
  (-> (assoc props :type :users_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/users-select)))

(s/fdef users-select
  :args (s/cat :props :users-select/props :children :slack-select/children)
  :ret ::elements.spec/users-select)

(defn conversations-select
  "This select menu will populate its options with a list of public and private channels, DMs, and MPIMs visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:conversations-select {:action_id \"A123\"
                           :default_to_current_conversation true
                           :initial_conversation \"C123\"
                           :filter {:include #{:private}
                                    :exclude_bot_users true
                                    :exclude_external_shared_channels true}}
    [:placeholder \"Select conversation\"]]
   ```"
  [props & children]
  (-> (assoc props :type :conversations_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/conversations-select)))

(s/fdef conversations-select
  :args (s/cat :props :conversations-select/props :children :slack-select/children)
  :ret ::elements.spec/conversations-select)

(defn channels-select
  "This select menu will populate its options with a list of public channels visible to the current user in the active workspace.
   
   Component usage:
   
   ```clojure
   [:channels-select {:action_id \"A123\" :initial_channel \"C123\"}
    [:placeholder \"Select channel\"]]
   ```"
  [props & children]
  (-> (assoc props :type :channels_select)
      (props/with-children children :slack-select/child)
      (validated ::elements.spec/channels-select)))

(s/fdef channels-select
  :args (s/cat :props :channels-select/props :children :slack-select/children)
  :ret ::elements.spec/channels-select)

(defn overflow
  "This is like a cross between a button and a select menu - when a user clicks on this overflow button,
   they will be presented with a list of options to choose from.
   
   Component usage:
   
   ```clojure
   [:overflow {:action_id \"A123\"}
    [:option {:value \"1\" :url \"https://google.com\"} \"Google\"]
    [:option {:value \"2\" :url \"https://bing.com\"} \"Bing\"]
    [:option {:value \"3\" :url \"https://duckduckgo.com\"} \"DuckDuckGo\"]]
   ```"
  [props & children]
  (-> (assoc props :type :overflow)
      (props/with-children children :overflow/child)
      (validated ::elements.spec/overflow)))

(s/fdef overflow
  :args (s/cat :props :overflow/props :children :overflow/children)
  :ret ::elements.spec/overflow)

(defn plain-text-input
  "A plain-text input, similar to the HTML <input> tag, creates a field where a user can enter freeform data.
   It can appear as a single-line field or a larger textarea using the multiline flag.
   
   Component usage:
   
   ```clojure
   [:plain-text-input {:action_id \"A123\"
                       :initial_value \"hello\"
                       :multiline true
                       :min_length 1
                       :max_length 100
                       :dispatch_action_config {:trigger_actions_on [:on_enter_pressed]}}
    [:placeholder \"Greeting\"]]
   ```"
  [props & children]
  (-> (assoc props :type :plain_text_input)
      (props/with-children children :plain-text-input/child)
      (validated ::elements.spec/plain-text-input)))

(s/fdef plain-text-input
  :args (s/cat :props :plain-text-input/props :children :plain-text-input/children)
  :ret ::elements.spec/plain-text-input)

(defn radio-buttons
  "A radio button group that allows a user to choose one item from a list of possible options.
   
   Component usage:
   
   ```clojure
   [:radio-buttons {:action_id \"A123\"}
    [:option {:value \"1\"} \"Pepperoni\"]
    [:option {:value \"2\" :selected? true} \"Pineapple\"]
    [:option {:value \"3\"} \"Mushrooms\"]]
   ```"
  [props & children]
  (-> (assoc props :type :radio_buttons)
      (props/with-children children :radio-buttons/child)
      (assoc-initial-options)
      (validated ::elements.spec/radio-buttons)
      (conform-options)))

(s/fdef radio-buttons
  :args (s/cat :props :radio-buttons/props :children :radio-buttons/children)
  :ret ::elements.spec/radio-buttons)
