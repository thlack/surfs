(ns ^:no-doc thlack.surfs.elements.components.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.elements.spec :as elements.spec]
            [thlack.surfs.props.spec :as props.spec]
            [thlack.surfs.strings.spec :as strings.spec :refer [deftext]]))

;;; Specs for options that support the userland :selected? option

(s/def ::option (s/merge ::comp.spec/option (s/keys :opt-un [::props.spec/selected?])))

(s/def ::options (s/coll-of ::option :min-count 1 :max-count 100 :into [] :gen-max 10))

(s/def ::option-group (s/keys :req-un [:option-group/label ::options]))

;;; [:button]

(deftext :button-child/text ::props.spec/plain-text 75)

(s/def :button/child
  (s/or :plain-text :button-child/text
        :confirm    ::comp.spec/confirm))

(s/def :button/children
  (s/with-gen
    (s/* :button/child)
    #(gen/tuple (s/gen :button-child/text) (s/gen ::comp.spec/confirm))))

(s/def :button/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:button/url :button/style :button/value]))

;;; [:checkboxes]

(s/def :checkboxes/child
  (s/or :option  ::option
        :confirm ::comp.spec/confirm))

(s/def :checkboxes/children
  (s/with-gen
    (s/* :checkboxes/child)
    #(gen/tuple (s/gen ::option) (s/gen ::option) (s/gen ::comp.spec/confirm))))

(s/def :checkboxes/props (s/keys :req-un [::strings.spec/action_id]))

;;; [:datepicker]

(s/def :datepicker/child
  (s/or :placeholder ::comp.spec/plain-text
        :confirm     ::comp.spec/confirm))

(s/def :datepicker/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:datepicker/initial_date]))

;;; [:timepicker]

(s/def :timepicker/child
  (s/or :placeholder ::comp.spec/plain-text
        :confirm     ::comp.spec/confirm))

(s/def :timepicker/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:timepicker/initial_time]))

;;; [:img]

(s/def :img/props (s/keys :req-un [:image/image_url :image/alt_text]))

(defn gen-option-parent
  "Returns a generator that yields samples
   of elements that contain :option or :option-group children.
   The extra-keys parameter specifies which properties to preserve
   in addition to :options and :option-groups."
  [spec extra-keys]
  (gen/fmap
   (fn [select]
     (-> select
         (select-keys [:options :option-groups])
         (vals)
         (flatten)
         (into
          (filter some? (vals (select-keys select extra-keys))))))
   (s/gen spec)))

;;; [:multi-static-select]

(s/def :multi-static-select/child
  (s/or :placeholder  ::comp.spec/plain-text
        :option       ::option
        :option-group ::option-group
        :confirm      ::comp.spec/confirm))

(s/def :multi-static-select/children
  (s/with-gen
    (s/* :multi-static-select/child)
    #(gen-option-parent ::elements.spec/multi-static-select [:confirm :placeholder])))

(s/def :multi-select/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:multi-select/max_selected_items]))

(s/def :slack-select/child
  (s/or :placeholder ::comp.spec/plain-text
        :confirm     ::comp.spec/confirm))

(s/def :slack-select/children
  (s/with-gen
    (s/* :slack-select/child)
    #(gen/tuple (s/gen ::comp.spec/plain-text) (s/gen ::comp.spec/confirm))))

;;; [:multi-external-select]

(s/def :multi-external-select/props (s/merge :multi-select/props (s/keys :opt-un [:external-select/min_query_length])))

;;; [:multi-users-select]

(s/def :multi-users-select/props (s/merge :multi-select/props (s/keys :opt-un [:multi-users-select/initial_users])))

;;; [:multi-conversations-select]

(s/def :multi-conversations-select/props (s/merge :multi-select/props (s/keys :opt-un [:multi-conversations-select/initial_conversations :conversations-select/default_to_current_conversation :conversation/filter])))

;;; [:multi-channels-select]

(s/def :multi-channels-select/props (s/merge :multi-select/props (s/keys :opt-un [:multi-channels-select/initial_channels])))

;;; [:static-select]

(s/def :static-select/child
  (s/or :confirm      ::comp.spec/confirm
        :placeholder  ::comp.spec/plain-text
        :option       ::option
        :option-group ::option-group))

(s/def :static-select/children
  (s/with-gen
    (s/* :static-select/child)
    #(gen-option-parent ::elements.spec/static-select [:confirm :placeholder])))

(s/def :static-select/props (s/keys :req-un [::strings.spec/action_id]))

;;; [:external-select]

(s/def :external-select/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:external-select/min_query_length]))

;;; [:users-select]

(s/def :users-select/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:users-select/initial_user]))

;;; [:conversations-select]

(s/def :conversations-select/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:conversations-select/initial_conversation :conversations-select/default_to_current_conversation :select/response_url_enabled :conversation/filter]))

;;; [:channels-select]

(s/def :channels-select/props (s/keys :req-un [::strings.spec/action_id] :opt-un [:select/response_url_enabled :channels-select/initial_channel]))

;;; [:overflow]

(s/def :overflow/child
  (s/or :confirm ::comp.spec/confirm
        :option  :overflow/option))

(s/def :overflow/children
  (s/with-gen
    (s/* :overflow/child)
    #(gen-option-parent ::elements.spec/overflow [:confirm])))

(s/def :overflow/props (s/keys :req-un [::strings.spec/action_id]))

;;; [:plain-text-input]

(s/def :plain-text-input/child
  (s/or :placeholder ::comp.spec/plain-text))

(s/def :plain-text-input/children
  (s/with-gen
    (s/* :plain-text-input/child)
    #(gen/fmap
      (fn [props]
        (->> (select-keys props [:placeholder])
             (vals)
             (filter some?)))
      (s/gen ::elements.spec/plain-text-input))))

(s/def :plain-text-input/props* (s/keys :req-un [::strings.spec/action_id] :opt-un [:plain-text-input/initial_value :plain-text-input/multiline :plain-text-input/min_length :plain-text-input/max_length ::comp.spec/dispatch_action_config]))

(s/def :plain-text-input/props (s/with-gen
                                 :plain-text-input/props*
                                 #(gen/fmap
                                   (fn [props]
                                     (select-keys props [:action_id :initial_value :multiline :min_length :max_length :dispatch_action_config]))
                                   (s/gen ::elements.spec/plain-text-input))))

;;; [:radio-buttons]

(s/def :radio-buttons/child
  (s/or :option  ::comp.spec/option
        :confirm ::comp.spec/confirm))

(s/def :radio-buttons/children
  (s/with-gen
    (s/* :radio-buttons/child)
    #(gen-option-parent ::elements.spec/radio-buttons [:confirm])))

(s/def :radio-buttons/props (s/keys :req-un [::strings.spec/action_id]))
