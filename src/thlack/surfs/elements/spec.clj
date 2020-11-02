(ns ^:no-doc thlack.surfs.elements.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

;;; Interactive components

(s/def :button/type #{:button})

(deftext :button/text ::comp.spec/plain-text 75)

(s/def :button/action_id ::strings.spec/action_id)

(deftext :button/url ::strings.spec/url-string 3000)

(deftext :button/value ::strings.spec/string 2000)

(s/def :button/style #{:primary :danger})

(s/def ::button (s/keys :req-un [:button/type :button/text ::strings.spec/action_id] :opt-un [:button/url :button/value :button/style ::comp.spec/confirm]))

(s/def :checkboxes/type #{:checkboxes})

(s/def :checkboxes/options (s/coll-of ::comp.spec/option :into [] :min-count 1 :max-count 10))

(s/def :checkboxes/initial_options :checkboxes/options)

(s/def ::checkboxes* (s/keys :req-un [:checkboxes/type ::strings.spec/action_id :checkboxes/options] :opt-un [:checkboxes/initial_options ::comp.spec/confirm]))

(defn valid-initial-options?
  "Initial options MUST be a sample from options. Supports giving a key if
   comparing option groups instead of options"
  ([{:keys [initial_options initial_option] :as element} selector]
   (let [options (set (selector element))]
     (->> (into initial_options [initial_option])
          (filterv some?)
          (every? options))))
  ([element]
   (valid-initial-options? element :options)))

(s/def ::checkboxes (s/with-gen
                      (s/and ::checkboxes* valid-initial-options?)
                      #(gen/fmap
                        (fn [{:keys [options initial_options] :as checkboxes}]
                          (if (some? initial_options)
                            (assoc checkboxes :initial_options (random-sample 0.5 options))
                            checkboxes))
                        (s/gen ::checkboxes*))))

(s/def :datepicker/type #{:datepicker})

(deftext :datepicker/placeholder ::comp.spec/plain-text 150)

(s/def :datepicker/initial_date ::strings.spec/date-string)

(s/def ::datepicker (s/keys :req-un [:datepicker/type ::strings.spec/action_id] :opt-un [:datepicker/placeholder :datepicker/initial_date ::comp.spec/confirm]))

(defn gen-select
  ([spec k]
   (gen/fmap
    (fn [{:keys [options option_groups] :as element}]
      (cond
        (:option_groups element) (assoc element k (random-sample 0.5 option_groups))
        (:options element) (assoc element k (random-sample 0.5 options))))
    (s/gen spec)))
  ([spec]
   (gen-select spec :initial_options)))

(defn valid-select-options?
  [element]
  (if (some? (:option_groups element))
    (valid-initial-options? element (fn [element]
                                      (->> (:option_groups element)
                                           (mapcat :options))))
    (valid-initial-options? element :options)))

(deftext :select/placeholder ::comp.spec/plain-text 150)

(s/def :select/options (s/coll-of ::comp.spec/option :max-count 100 :min-count 1 :into [] :gen-max 5))

(s/def :select/option_groups (s/coll-of ::comp.spec/option-group :max-count 100 :min-count 1 :into [] :gen-max 5))

(s/def :select/initial_option ::comp.spec/option)

(s/def :select/initial_options :select/options)

(s/def :select/response_url_enabled boolean?)

(s/def :external-select/min_query_length pos-int?)

(s/def :conversations-select/default_to_current_conversation boolean?)

(s/def :multi-select/max_selected_items pos-int?)

(s/def :multi-static-select/type #{:multi_static_select})

(s/def ::multi-static-select*
  (s/keys :req-un [:multi-static-select/type :select/placeholder ::strings.spec/action_id (or :select/options :select/option_groups)]
          :opt-un [::comp.spec/confirm :multi-select/max_selected_items :select/initial_options]))

(s/def ::multi-static-select (s/with-gen
                               (s/and ::multi-static-select* valid-select-options?)
                               #(gen-select ::multi-static-select*)))

(s/def :multi-external-select/type #{:multi_external_select})

(s/def ::multi-external-select (s/keys :req-un [:multi-external-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:external-select/min_query_length ::comp.spec/confirm :multi-select/max_selected_items :select/initial_options]))

(s/def :multi-users-select/type #{:multi_users_select})

(s/def :multi-users-select/initial_users (s/coll-of ::strings.spec/id :into [] :gen-max 10 :min-count 1))

(s/def ::multi-users-select (s/keys :req-un [:multi-users-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:multi-users-select/initial_users ::comp.spec/confirm :multi-select/max_selected_items]))

(s/def :multi-conversations-select/type #{:multi_conversations_select})

(s/def :multi-conversations-select/initial_conversations (s/coll-of ::strings.spec/id :into [] :gen-max 5 :min-count 1))

(s/def ::multi-conversations-select (s/keys :req-un [:multi-conversations-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:multi-conversations-select/initial_conversations :conversations-select/default_to_current_conversation ::comp.spec/confirm :multi-select/max_selected_items :conversation/filter]))

(s/def :multi-channels-select/type #{:multi_channels_select})

(s/def :multi-channels-select/initial_channels (s/coll-of ::strings.spec/id :into [] :gen-max 10 :min-count 1))

(s/def ::multi-channels-select (s/keys :req-un [:multi-channels-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:multi-channels-select/initial_channels ::comp.spec/confirm :multi-select/max_selected_items]))

(s/def :overflow/type #{:overflow})

(s/def :overflow/url (s/and ::strings.spec/url-string (strings.spec/max-len 3000)))

(s/def :overflow/option (s/merge ::comp.spec/option (s/keys :opt-un [:overflow/url])))

(s/def :overflow/options (s/coll-of :overflow/option :into [] :min-count 2 :max-count 5 :gen-max 3))

(s/def ::overflow (s/keys :req-un [:overflow/type ::strings.spec/action_id :overflow/options] :opt-un [::comp.spec/confirm]))

(s/def :plain-text-input/type #{:plain_text_input})

(deftext :plain-text-input/placeholder ::comp.spec/plain-text 150)

(s/def :plain-text-input/initial_value ::strings.spec/string)

(s/def :plain-text-input/multiline boolean?)

(s/def :plain-text-input/min_length (s/int-in 1 3001))

(s/def :plain-text-input/max_length pos-int?)

(s/def ::plain-text-input* (s/keys :req-un [:plain-text-input/type ::strings.spec/action_id] :opt-un [:plain-text-input/placeholder :plain-text-input/initial_value :plain-text-input/multiline :plain-text-input/min_length :plain-text-input/max_length ::comp.spec/dispatch_action_config]))

(defn max-gte-min?
  [{:keys [min_length max_length] :or {min_length 0 max_length 1}}]
  (<= min_length max_length))

(s/def ::plain-text-input (s/with-gen
                            (s/and ::plain-text-input* max-gte-min?)
                            #(gen/fmap
                              (fn [{:keys [max_length min_length] :as element}]
                                (if (and max_length min_length (> min_length max_length))
                                  (assoc element :min_length (- max_length 1))
                                  element))
                              (s/gen ::plain-text-input*))))

(s/def :radio-buttons/type #{:radio_buttons})

(s/def :radio-buttons/options (s/coll-of ::comp.spec/option :into [] :min-count 1 :max-count 10 :gen-max 5))

(s/def :radio-buttons/initial_option ::comp.spec/option)

(s/def ::radio-buttons* (s/keys :req-un [:radio-buttons/type ::strings.spec/action_id :radio-buttons/options] :opt-un [:radio-buttons/initial_option ::comp.spec/confirm]))

(s/def ::radio-buttons (s/with-gen
                         (s/and ::radio-buttons* valid-initial-options?)
                         #(gen/fmap
                           (fn [element]
                             (if (:initial_option element)
                               (assoc element :initial_option (rand-nth (:options element)))
                               element))
                           (s/gen ::radio-buttons*))))

(s/def :static-select/type #{:static_select})

(s/def ::static-select*
  (s/keys :req-un [:static-select/type :select/placeholder ::strings.spec/action_id (or :select/options :select/option_groups)]
          :opt-un [:select/initial_option ::comp.spec/confirm]))

(s/def ::static-select (s/with-gen
                         (s/and ::static-select* valid-select-options?)
                         #(gen-select ::static-select*)))

(s/def :external-select/type #{:external_select})

(s/def ::external-select (s/keys :req-un [:external-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:select/initial_option :external-select/min_query_length ::comp.spec/confirm]))

(s/def :users-select/type #{:users_select})

(s/def :users-select/initial_user ::strings.spec/id)

(s/def ::users-select (s/keys :req-un [:users-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:users-select/initial_user ::comp.spec/confirm]))

(s/def :conversations-select/type #{:conversations_select})

(s/def :conversations-select/initial_conversation ::strings.spec/id)

(s/def ::conversations-select (s/keys :req-un [:conversations-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:conversations-select/initial_conversation :conversations-select/default_to_current_conversation ::comp.spec/confirm :select/response_url_enabled :conversation/filter]))

(s/def :channels-select/type #{:channels_select})

(s/def :channels-select/initial_channel ::strings.spec/id)

(s/def ::channels-select (s/keys :req-un [:channels-select/type :select/placeholder ::strings.spec/action_id] :opt-un [:channels-select/initial_channel ::comp.spec/confirm :select/response_url_enabled]))

(s/def :timepicker/type #{:timepicker})

(s/def :timepicker/initial_time ::strings.spec/time-string)

(deftext :timepicker/placeholder ::comp.spec/plain-text 150)

(s/def ::timepicker (s/keys :req-un [:timepicker/type ::strings.spec/action_id] :opt-un [:timepicker/placeholder :timepicker/initial_time ::comp.spec/confirm]))

(s/def :image/type #{:image})

(s/def :image/image_url ::strings.spec/url-string)

(s/def :image/alt_text ::strings.spec/string)

(s/def ::image (s/keys :req-un [:image/type :image/image_url :image/alt_text]))
