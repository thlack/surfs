(ns thlack.surfs.examples-test
  "Way less cool unit test suite, but it's useful for seeing surfs
   in action :)"
  (:require [clojure.test :refer [is]]
            [thlack.surfs.test-utils :refer [defrendertest]]))

;;; Composition

(defrendertest text
  [:text "Hello"]

  [:text {:type :mrkdwn :verbatim false :text "# Hello"}]

  [:text {:type :plain_text :emoji true :text "Hello"}]

  (fn [[literal markdown plain]]
    (is (= literal {:type :plain_text :text "Hello"}))
    (is (= markdown {:type :mrkdwn :text "# Hello" :verbatim false}))
    (is (= plain {:type :plain_text :text "Hello" :emoji true}))))

(defrendertest plain-text
  [:plain-text "Hello"]

  [:plain-text "Goodbye" false]

  [:plain-text {:text "Greetings" :emoji false}]

  (fn [[text-only with-emoji props]]
    (is (= {:type :plain_text :text "Hello" :emoji true} text-only))
    (is (= {:type :plain_text :text "Goodbye" :emoji false} with-emoji))
    (is (= {:type :plain_text :text "Greetings" :emoji false} props))))

(defrendertest markdown
  [:markdown "# Hello"]

  [:markdown "# Goodbye" true]

  [:markdown {:text "# Greetings" :verbatim true}]

  (fn [[text-only with-verbatim props]]
    (is (= {:type :mrkdwn :text "# Hello" :verbatim false} text-only))
    (is (= {:type :mrkdwn :text "# Goodbye" :verbatim true} with-verbatim))
    (is (= {:type :mrkdwn :text "# Greetings" :verbatim true} props))))

(defrendertest confirm
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "This is a title!"}
   [:text "Are you sure?"]]

  [:confirm {:confirm "Ok!" :deny "Nah!" :title "This is a title!"} "Are you sure?"]

  (fn [[confirm literal-text]]
    (let [expected {:confirm {:type :plain_text :text "Ok!" :emoji true}
                    :deny {:type :plain_text :text "Nah!" :emoji true}
                    :title {:type :plain_text :text "This is a title!" :emoji true}
                    :text {:type :plain_text :text "Are you sure?"}
                    :style :primary}]
      (is (= expected confirm literal-text)))))

(defrendertest option
  [:option {:value "1"} "Label"]

  [:option {:value "1"} {:type :plain_text :text "Label"}]

  [:option {:value "1" :description "Oh hello"} "Label"]

  (fn [[first second third]]
    (let [expected {:value "1" :text {:type :plain_text :text "Label"}}]
      (is (= expected first second))
      (is (= (merge expected {:description {:type :plain_text :text "Oh hello" :emoji true}}) third)))))

(defrendertest option-group
  [:option-group
   [:label "Pizza Toppings"]
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2"} "Pepperoni"]]

  (fn [[group]]
    (is (= {:label {:type :plain_text :text "Pizza Toppings" :emoji true}
            :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                      {:value "2" :text {:type :plain_text :text "Pepperoni"}}]} group))))

;;; Elements

(defn assert-confirm
  "Helper to make asserting on confirms easier"
  [base actual & {:keys [confirm deny title text]}]
  (is (= (merge base {:confirm {:confirm {:type  :plain_text
                                          :text  confirm
                                          :emoji true}
                                :deny    {:type  :plain_text
                                          :text  deny
                                          :emoji true}
                                :title   {:type  :plain_text
                                          :text  title
                                          :emoji true}
                                :text    {:type :plain_text
                                          :text text}
                                :style   :primary}}) actual)))

(defrendertest button
  [:button {:action_id "A123" :value "1"}
   "Click Me!"]

  [:button {:action_id "A123" :value "1"}
   [:text "Click Me!"]]

  [:button {:action_id "A123" :value "1"}
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]
   "Click Me!"]

  [:button "Click Me!"]

  (fn [[literal with-text with-confirm no-props]]
    (let [expected {:action_id "A123"
                    :value     "1"
                    :type      :button
                    :text      {:type :plain_text
                                :text "Click Me!"}}]
      (is (= expected literal with-text))
      (is (= (dissoc expected :value :action_id) (dissoc no-props :action_id)))
      (is (string? (:action_id no-props)))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest checkboxes
  [:checkboxes {:action_id "A123"}
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]]

  [:checkboxes {:action_id "A123"}
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :checkboxes
                    :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                              {:value "2" :text {:type :plain_text :text "Pepperoni"}}]
                    :initial_options [{:value "2" :text {:type :plain_text :text "Pepperoni"}}]}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest datepicker
  [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
   [:placeholder "The date"]]

  [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
   [:placeholder "The date"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :datepicker
                    :initial_date "2020-11-30"
                    :placeholder {:type :plain_text :text "The date" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest timepicker
  [:timepicker {:action_id "A123" :initial_time "12:30"}
   [:placeholder "The time"]]

  [:timepicker {:action_id "A123" :initial_time "12:30"}
   [:placeholder "The time"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :timepicker
                    :initial_time "12:30"
                    :placeholder {:type :plain_text :text "The time" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest img
  [:img {:image_url "http://www.fillmurray.com/200/300" :alt_text "It's Bill Murray"}]

  (fn [[element]]
    (is (= {:type :image :image_url "http://www.fillmurray.com/200/300" :alt_text "It's Bill Murray"} element))))

(defrendertest multi-static-select-options
  [:multi-static-select {:action_id "A123" :max_selected_items 5}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]
   [:option {:value "3" :selected? true} "Cheese"]]

  [:multi-static-select {:action_id "A123" :max_selected_items 5}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]
   [:option {:value "3" :selected? true} "Cheese"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 5
                    :type :multi_static_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                              {:value "2" :text {:type :plain_text :text "Pepperoni"}}
                              {:value "3" :text {:type :plain_text :text "Cheese"}}]
                    :initial_options [{:value "2" :text {:type :plain_text :text "Pepperoni"}}
                                      {:value "3" :text {:type :plain_text :text "Cheese"}}]}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest multi-static-select-option-groups
  [:multi-static-select {:action_id "A123" :max_selected_items 5}
   [:placeholder "Pizza Toppings"]
   [:option-group
    [:label "Veggies"]
    [:option {:value "1"} "Mushrooms"]
    [:option {:value "2" :selected? true} "Peppers"]]
   [:option-group
    [:label "Meats"]
    [:option {:value "3"} "Pepperoni"]
    [:option {:value "4" :selected? true} "Ham"]]]

  [:multi-static-select {:action_id "A123" :max_selected_items 5}
   [:placeholder "Pizza Toppings"]
   [:option-group
    [:label "Veggies"]
    [:option {:value "1"} "Mushrooms"]
    [:option {:value "2" :selected? true} "Peppers"]]
   [:option-group
    [:label "Meats"]
    [:option {:value "3"} "Pepperoni"]
    [:option {:value "4" :selected? true} "Ham"]]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 5
                    :type :multi_static_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :option_groups [{:label {:type :plain_text :text "Veggies" :emoji true}
                                     :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                                               {:value "2" :text {:type :plain_text :text "Peppers"}}]}
                                    {:label {:type :plain_text :text "Meats" :emoji true}
                                     :options [{:value "3" :text {:type :plain_text :text "Pepperoni"}}
                                               {:value "4" :text {:type :plain_text :text "Ham"}}]}]
                    :initial_options [{:value "2" :text {:type :plain_text :text "Peppers"}}
                                      {:value "4" :text {:type :plain_text :text "Ham"}}]}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest multi-external-select
  [:multi-external-select {:action_id "A123" :max_selected_items 5 :min_query_length 3}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Pepperoni"]
   [:option {:value "2"} "Mushrooms"]]

  [:multi-external-select {:action_id "A123" :max_selected_items 5 :min_query_length 3}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Pepperoni"]
   [:option {:value "2"} "Mushrooms"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 5
                    :min_query_length 3
                    :type :multi_external_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :initial_options [{:value "1" :text {:type :plain_text :text "Pepperoni"}}
                                      {:value "2" :text {:type :plain_text :text "Mushrooms"}}]}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest multi-users-select
  [:multi-users-select {:action_id "A123" :max_selected_items 3 :initial_users ["U123" "U456"]}
   [:placeholder "Team captains"]]

  [:multi-users-select {:action_id "A123" :max_selected_items 3 :initial_users ["U123" "U456"]}
   [:placeholder "Team captains"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 3
                    :initial_users ["U123" "U456"]
                    :type :multi_users_select
                    :placeholder {:type :plain_text :text "Team captains" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest multi-conversations-select
  [:multi-conversations-select {:action_id "A123"
                                :max_selected_items 3
                                :default_to_current_conversation true
                                :initial_conversations ["C123" "C456"]
                                :filter {:include #{:private}
                                         :exclude_bot_users true
                                         :exclude_external_shared_channels true}}
   [:placeholder "Select conversation"]]

  [:multi-conversations-select {:action_id "A123"
                                :max_selected_items 3
                                :default_to_current_conversation true
                                :initial_conversations ["C123" "C456"]
                                :filter {:include #{:private}
                                         :exclude_bot_users true
                                         :exclude_external_shared_channels true}}
   [:placeholder "Select conversation"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 3
                    :default_to_current_conversation true
                    :initial_conversations ["C123" "C456"]
                    :filter {:include #{:private}
                             :exclude_bot_users true
                             :exclude_external_shared_channels true}
                    :type :multi_conversations_select
                    :placeholder {:type :plain_text :text "Select conversation" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest multi-channels-select
  [:multi-channels-select {:action_id "A123" :max_selected_items 3 :initial_channels ["C123" "C456"]}
   [:placeholder "Select channel"]]

  [:multi-channels-select {:action_id "A123" :max_selected_items 3 :initial_channels ["C123" "C456"]}
   [:placeholder "Select channel"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :max_selected_items 3
                    :initial_channels ["C123" "C456"]
                    :type :multi_channels_select
                    :placeholder {:type :plain_text :text "Select channel" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest static-select-options
  [:static-select {:action_id "A123"}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]
   [:option {:value "3"} "Cheese"]]

  [:static-select {:action_id "A123"}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Mushrooms"]
   [:option {:value "2" :selected? true} "Pepperoni"]
   [:option {:value "3"} "Cheese"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :static_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                              {:value "2" :text {:type :plain_text :text "Pepperoni"}}
                              {:value "3" :text {:type :plain_text :text "Cheese"}}]
                    :initial_option {:value "2" :text {:type :plain_text :text "Pepperoni"}}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest static-select-option-groups
  [:static-select {:action_id "A123"}
   [:placeholder "Pizza Toppings"]
   [:option-group
    [:label "Veggies"]
    [:option {:value "1"} "Mushrooms"]
    [:option {:value "2" :selected? true} "Peppers"]]
   [:option-group
    [:label "Meats"]
    [:option {:value "3"} "Pepperoni"]
    [:option {:value "4"} "Ham"]]]

  [:static-select {:action_id "A123"}
   [:placeholder "Pizza Toppings"]
   [:option-group
    [:label "Veggies"]
    [:option {:value "1"} "Mushrooms"]
    [:option {:value "2" :selected? true} "Peppers"]]
   [:option-group
    [:label "Meats"]
    [:option {:value "3"} "Pepperoni"]
    [:option {:value "4"} "Ham"]]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :static_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :option_groups [{:label {:type :plain_text :text "Veggies" :emoji true}
                                     :options [{:value "1" :text {:type :plain_text :text "Mushrooms"}}
                                               {:value "2" :text {:type :plain_text :text "Peppers"}}]}
                                    {:label {:type :plain_text :text "Meats" :emoji true}
                                     :options [{:value "3" :text {:type :plain_text :text "Pepperoni"}}
                                               {:value "4" :text {:type :plain_text :text "Ham"}}]}]
                    :initial_option {:value "2" :text {:type :plain_text :text "Peppers"}}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest external-select
  [:external-select {:action_id "A123" :min_query_length 3}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Pepperoni"]]

  [:external-select {:action_id "A123" :min_query_length 3}
   [:placeholder "Pizza Toppings"]
   [:option {:value "1"} "Pepperoni"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :min_query_length 3
                    :type :external_select
                    :placeholder {:type :plain_text :text "Pizza Toppings" :emoji true}
                    :initial_option {:value "1" :text {:type :plain_text :text "Pepperoni"}}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest users-select
  [:users-select {:action_id "A123" :initial_user "U123"}
   [:placeholder "Team captain"]]

  [:users-select {:action_id "A123" :initial_user "U123"}
   [:placeholder "Team captain"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :initial_user "U123"
                    :type :users_select
                    :placeholder {:type :plain_text :text "Team captain" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest conversations-select
  [:conversations-select {:action_id "A123"
                          :default_to_current_conversation true
                          :initial_conversation "C123"
                          :filter {:include #{:private}
                                   :exclude_bot_users true
                                   :exclude_external_shared_channels true}}
   [:placeholder "Select conversation"]]

  [:conversations-select {:action_id "A123"
                          :default_to_current_conversation true
                          :initial_conversation "C123"
                          :filter {:include #{:private}
                                   :exclude_bot_users true
                                   :exclude_external_shared_channels true}}
   [:placeholder "Select conversation"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :default_to_current_conversation true
                    :initial_conversation "C123"
                    :filter {:include #{:private}
                             :exclude_bot_users true
                             :exclude_external_shared_channels true}
                    :type :conversations_select
                    :placeholder {:type :plain_text :text "Select conversation" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest channels-select
  [:channels-select {:action_id "A123" :initial_channel "C123"}
   [:placeholder "Select channel"]]

  [:channels-select {:action_id "A123" :initial_channel "C123"}
   [:placeholder "Select channel"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :initial_channel "C123"
                    :type :channels_select
                    :placeholder {:type :plain_text :text "Select channel" :emoji true}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest overflow
  [:overflow {:action_id "A123"}
   [:option {:value "1" :url "https://google.com"} "Google"]
   [:option {:value "2" :url "https://bing.com"} "Bing"]
   [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]]

  [:overflow
   [:option {:value "1" :url "https://google.com"} "Google"]
   [:option {:value "2" :url "https://bing.com"} "Bing"]
   [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]]

  [:overflow {:action_id "A123"}
   [:option {:value "1" :url "https://google.com"} "Google"]
   [:option {:value "2" :url "https://bing.com"} "Bing"]
   [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first no-props with-confirm]]
    (let [expected {:action_id "A123"
                    :type :overflow
                    :options [{:value "1" :url "https://google.com" :text {:type :plain_text :text "Google"}}
                              {:value "2" :url "https://bing.com" :text {:type :plain_text :text "Bing"}}
                              {:value "3" :url "https://duckduckgo.com" :text {:type :plain_text :text "DuckDuckGo"}}]}]
      (is (= expected first))
      (is (= (dissoc expected :action_id) (dissoc no-props :action_id)))
      (is (string? (:action_id no-props)))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

(defrendertest plain-text-input
  [:plain-text-input {:action_id "A123"
                      :initial_value "hello"
                      :multiline true
                      :min_length 1
                      :max_length 100
                      :dispatch_action_config {:trigger_actions_on [:on_enter_pressed]}}
   [:placeholder "Greeting"]]

  (fn [[element]]
    (let [expected {:action_id "A123"
                    :initial_value "hello"
                    :multiline true
                    :min_length 1
                    :max_length 100
                    :dispatch_action_config {:trigger_actions_on [:on_enter_pressed]}
                    :type :plain_text_input
                    :placeholder {:type :plain_text :text "Greeting" :emoji true}}]
      (is (= expected element)))))

(defrendertest radio-buttons
  [:radio-buttons {:action_id "A123"}
   [:option {:value "1"} "Pepperoni"]
   [:option {:value "2" :selected? true} "Pineapple"]
   [:option {:value "3"} "Mushrooms"]]

  [:radio-buttons {:action_id "A123"}
   [:option {:value "1"} "Pepperoni"]
   [:option {:value "2" :selected? true} "Pineapple"]
   [:option {:value "3"} "Mushrooms"]
   [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

  (fn [[first with-confirm]]
    (let [expected {:action_id "A123"
                    :type :radio_buttons
                    :options [{:value "1" :text {:type :plain_text :text "Pepperoni"}}
                              {:value "2" :text {:type :plain_text :text "Pineapple"}}
                              {:value "3" :text {:type :plain_text :text "Mushrooms"}}]
                    :initial_option
                    {:value "2" :text {:type :plain_text :text "Pineapple"}}}]
      (is (= expected first))
      (assert-confirm expected with-confirm :confirm "Ok!" :deny "Nah!" :title "You sure?!?!?" :text "This is irreversible!"))))

;;; Blocks

(defrendertest actions
  [:actions {:block_id "B123"}
   [:radio-buttons {:action_id "A123"}
    [:option {:value "1"} "Pepperoni"]
    [:option {:value "2" :selected? true} "Pineapple"]
    [:option {:value "3"} "Mushrooms"]]
   [:channels-select {:action_id "A456" :initial_channel "C123"}
    [:placeholder "Select channel"]]]

  [:actions
   [:radio-buttons {:action_id "A123"}
    [:option {:value "1"} "Pepperoni"]
    [:option {:value "2" :selected? true} "Pineapple"]
    [:option {:value "3"} "Mushrooms"]]
   [:channels-select {:action_id "A456" :initial_channel "C123"}
    [:placeholder "Select channel"]]]

  (fn [[block no-props]]
    (let [expected {:block_id "B123"
                    :type :actions
                    :elements
                    [{:action_id "A123"
                      :type :radio_buttons
                      :options [{:value "1" :text {:type :plain_text :text "Pepperoni"}}
                                {:value "2" :text {:type :plain_text :text "Pineapple"}}
                                {:value "3" :text {:type :plain_text :text "Mushrooms"}}]
                      :initial_option
                      {:value "2" :text {:type :plain_text :text "Pineapple"}}}
                     {:action_id "A456"
                      :initial_channel "C123"
                      :type :channels_select
                      :placeholder
                      {:type :plain_text :text "Select channel" :emoji true}}]}]
      (is (= expected block))
      (is (= (dissoc expected :block_id) no-props)))))

(defrendertest fields
  [:fields
   [:plain-text "Hello"]
   [:markdown "# There"]
   [:text {:type :plain_text :text "My friend" :emoji false}]
   [:text {:type :mrkdwn :text "## Pleased to meet you" :verbatim false}]]

  (fn [[fields]]
    (let [expected {:fields
                    [{:type :plain_text, :text "Hello", :emoji true}
                     {:type :mrkdwn, :text "# There", :verbatim false}
                     {:type :plain_text, :text "My friend", :emoji false}
                     {:type :mrkdwn, :text "## Pleased to meet you", :verbatim false}]}]
      (is (= expected fields)))))

(defrendertest section
  [:section {:block_id "B123"}
   [:text "This is an important action"]
   [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]
    [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
     [:text "This is irreversible!"]]]]

  [:section
   [:text "This is an important action"]
   [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]
    [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
     [:text "This is irreversible!"]]]]

  [:section {:block_id "B123"}
   [:text "This is an important action"]
   [:fields
    [:markdown "# Field 1"]
    [:plain-text "Field 2"]]
   [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]
    [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
     [:text "This is irreversible!"]]]]

  [:section {:block_id "B123"}
   [:fields
    [:markdown "# Field 1"]
    [:plain-text "Field 2"]]
   [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]
    [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
     [:text "This is irreversible!"]]]]

  (fn [[block no-props with-fields only-fields]]
    (let [expected {:block_id "B123"
                    :type :section
                    :accessory
                    {:action_id "A123"
                     :initial_date "2020-11-30"
                     :type :datepicker
                     :confirm
                     {:confirm {:type :plain_text, :text "Ok!", :emoji true}
                      :deny {:type :plain_text, :text "Nah!", :emoji true}
                      :title {:type :plain_text, :text "You sure?!?!?", :emoji true}
                      :text {:type :plain_text, :text "This is irreversible!"}
                      :style :primary}
                     :placeholder {:type :plain_text, :text "The date", :emoji true}}
                    :text {:type :plain_text, :text "This is an important action"}}
          expected-with-fields (assoc expected :fields
                                      [{:type :mrkdwn, :text "# Field 1", :verbatim false}
                                       {:type :plain_text, :text "Field 2", :emoji true}])]
      (is (= expected block))
      (is (= (dissoc expected :block_id) no-props))
      (is (= expected-with-fields with-fields))
      (is (= (dissoc expected-with-fields :text) only-fields)))))

(defrendertest context
  [:context {:block_id "B123"}
   [:image {:alt_text "It's Bill" :image_url "http://www.fillmurray.com/200/300"}]
   [:text "This is some text"]]

  [:context
   [:image {:alt_text "It's Bill" :image_url "http://www.fillmurray.com/200/300"}]
   [:text "This is some text"]]

  (fn [[context no-props]]
    (let [expected {:block_id "B123"
                    :type     :context
                    :elements [{:alt_text  "It's Bill"
                                :image_url "http://www.fillmurray.com/200/300"
                                :type      :image}
                               {:type :plain_text
                                :text "This is some text"}]}]
      (is (= expected context))
      (is (= (dissoc expected :block_id) no-props)))))

(defrendertest divider
  [:divider]
  [:divider {:block_id "B123"}]

  (fn [[first second]]
    (is (= {:type :divider} first))
    (is (= {:type :divider :block_id "B123"} second))))

(defrendertest header
  [:header {:block_id "B123"}
   [:text "Hello"]]

  [:header {:block_id "B123"} "Hello"]

  [:header "Hello"]

  (fn [[first second no-props]]
    (let [expected {:block_id "B123"
                    :type :header
                    :text {:type :plain_text :text "Hello"}}]
      (is (= expected first))
      (is (= expected second))
      (is (= (dissoc expected :block_id) no-props)))))

(defrendertest image
  [:image {:image_url "http://www.fillmurray.com/200/300"
           :alt_text "It's Bill"
           :block_id "B123"}
   [:title "Wowzers!"]]

  [:image {:image_url "http://www.fillmurray.com/200/300"
           :alt_text "It's Bill"
           :block_id "B123"}
   "Wowzers!"]

  [:image {:image_url "http://www.fillmurray.com/200/300"
           :alt_text "It's Bill"
           :block_id "B123"}]

  (fn [[first second third]]
    (let [expected {:block_id "B123"
                    :alt_text "It's Bill"
                    :type :image
                    :image_url "http://www.fillmurray.com/200/300"
                    :title {:type :plain_text :text "Wowzers!" :emoji true}}]
      (is (= expected first))
      (is (= (update expected :title dissoc :emoji) second))
      (is (= (dissoc expected :title) third)))))

(defrendertest input
  [:input {:block_id "B123" :dispatch_action false :optional false}
   [:label "Some input"]
   [:hint "Do something radical"]
   [:plain-text-input {:action_id "A123"
                       :initial_value "hello"}
    [:placeholder "Greeting"]]]

  [:input
   [:label "Some input"]
   [:hint "Do something radical"]
   [:plain-text-input {:action_id "A123"
                       :initial_value "hello"}
    [:placeholder "Greeting"]]]

  (fn [[block no-props]]
    (let [expected {:block_id "B123"
                    :dispatch_action false
                    :optional false
                    :type :input
                    :element {:action_id "A123"
                              :initial_value "hello"
                              :type :plain_text_input
                              :placeholder {:type :plain_text :text "Greeting" :emoji true}}
                    :label {:type :plain_text :text "Some input" :emoji true}
                    :hint {:type :plain_text :text "Do something radical" :emoji true}}]
      (is (= expected block))
      (is (= (dissoc expected :block_id :dispatch_action :optional) no-props)))))


;;; Views


(defrendertest home
  [:home {:private_metadata {:cool? true}}
   [:section {:block_id "B123"}
    [:text "Some text"]]]

  [:home
   [:section {:block_id "B123"}
    [:text "Some text"]]]

  (fn [[view no-props]]
    (let [expected {:type :home
                    :private_metadata "{:cool? true}"
                    :blocks [{:block_id "B123"
                              :type :section
                              :text {:type :plain_text :text "Some text"}}]}]
      (is (= expected view))
      (is (= (dissoc expected :private_metadata) no-props)))))

(defrendertest modal
  [:modal {:private_metadata {:cool? true}
           :title "Cool Modal!"
           :close "Nah!"
           :submit "Yah!"}
   [:section {:block_id "B123"}
    [:text "Some text"]]]

  [:modal {:private_metadata {:cool? true}
           :title "Cool Modal!"
           :close "Nah!"
           :submit "Yah!"
           :disable_emoji_for #{:title :close :submit}}
   [:section {:block_id "B123"}
    [:text "Some text"]]]
  (fn [[view no-emoji]]
    (let [expected {:type :modal
                    :private_metadata "{:cool? true}"
                    :title {:type :plain_text :text "Cool Modal!" :emoji true}
                    :close {:type :plain_text :text "Nah!" :emoji true}
                    :submit {:type :plain_text :text "Yah!" :emoji true}
                    :blocks [{:block_id "B123"
                              :type :section
                              :text {:type :plain_text :text "Some text"}}]}]
      (is (= expected view))
      (is (= (-> expected
                 (assoc-in [:title :emoji] false)
                 (assoc-in [:close :emoji] false)
                 (assoc-in [:submit :emoji] false)) no-emoji)))))

;;; Messages

(defrendertest message
  [:message {:thread_ts "107"}
   "Text"]

  [:message "Just text"]

  [:message "Fallback" [:divider]]

  [:message {:thread_ts "107"} [:divider]]

  [:message {:thread_ts "107"} "Fallback" [:divider]]

  (fn [[props-and-text just-text no-props-text-and-blocks props-and-blocks all]]
    (is (= {:thread_ts "107" :text "Text"} props-and-text))
    (is (= {:text "Just text"} just-text))
    (is (= {:text "Fallback" :blocks [{:type :divider}]} no-props-text-and-blocks))
    (is (= {:thread_ts "107" :blocks [{:type :divider}]} props-and-blocks))
    (is (= {:thread_ts "107" :text "Fallback" :blocks [{:type :divider}]} all))))

;;; Custom components 

(defn fun-text
  [str]
  [:text str])

(defn custom
  [props & children]
  [:section {:block_id "B123"}
   [fun-text (:text props)]
   children])

(defrendertest custom-component
  [custom {:text "Hello"}
   [:button {:action_id "A123"}
    [:text "Click Me!"]]]
  (fn [[element]]
    (is (= {:block_id  "B123"
            :type      :section
            :accessory {:action_id "A123"
                        :type      :button
                        :text      {:type :plain_text
                                    :text "Click Me!"}}
            :text      {:type :plain_text
                        :text "Hello"}} element))))
