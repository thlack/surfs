(ns thlack.surfs.render-test
  "Mostly property based tests for rendering surfs"
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test :refer [deftest is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [thlack.surfs.render :as surfs.render]
            [thlack.surfs.blocks.components.spec :as bc.spec]
            [thlack.surfs.blocks.spec :as blocks.spec]
            [thlack.surfs.elements.components.spec :as ec.spec]
            [thlack.surfs.elements.spec :as elements.spec]
            [thlack.surfs.composition.spec :as comp.spec]
            [thlack.surfs.composition.spec.confirm :as confirm]
            [thlack.surfs.composition.spec.option :as option]
            [thlack.surfs.composition.components.spec :as cc.spec]
            [thlack.surfs.messages.components.spec :as mc.spec]
            [thlack.surfs.messages.spec :as messages.spec]
            [thlack.surfs.test-utils :refer [render]]
            [thlack.surfs.views.components.spec :as vc.spec]
            [thlack.surfs.views.spec :as views.spec]))

(def iterations 10)

;;; Composition elements

(defspec text
  iterations
  (prop/for-all [props (s/gen ::comp.spec/text)]
                (render ::comp.spec/text [:text props])))

(defspec confirm
  iterations
  (prop/for-all [props (s/gen ::cc.spec/confirm.props)]
                (let [text (gen/generate (s/gen ::confirm/text))]
                  (render ::comp.spec/confirm
                          [:confirm props
                           [:text text]]))))

(defspec option
  iterations
  (prop/for-all [props (s/gen ::cc.spec/option.props)]
                (let [text (gen/generate (s/gen ::option/text))]
                  (render ::comp.spec/option [:option props text]))))

(defn with-description
  [option]
  (if (some? (:description option))
    (update option :description :text)
    option))

(defn make-options
  "Converts generated options into a map of option tags"
  [options]
  (when options
    (map #(vector :option (with-description %) (:text %)) options)))

(defn make-option-groups
  [option-groups]
  (when option-groups
    (map (fn [{:keys [label options]}]
           [:option-group
            [:label label]
            (make-options options)]) option-groups)))

(defn make-confirm
  "Converts an optional confirm generated from specs into a
   confirm tag"
  [confirm]
  (when confirm
    [:confirm
     (-> (select-keys confirm [:confirm :deny :title])
         (update :confirm :text)
         (update :deny :text)
         (update :title :text))
     [:text (:text confirm)]]))

(defspec option-group
  iterations
  (prop/for-all [props (s/gen ::comp.spec/option-group)]
                (let [{label   :label
                       options :options} props]
                  (render ::comp.spec/option-group
                          [:option-group
                           [:label label]
                           (make-options options)]))))

;;; Elements

(defspec button
  iterations
  (prop/for-all [props (s/gen ::elements.spec/button)]
                (let [{text    :text
                       confirm :confirm} props]
                  (render ::elements.spec/button
                          [:button props
                           [:text text]
                           (make-confirm confirm)]))))

(defspec checkboxes
  iterations
  (prop/for-all [props   (s/gen ::ec.spec/checkboxes.props)
                 element (s/gen ::elements.spec/checkboxes)]
                (let [{options :options
                       confirm :confirm} element]
                  (render ::elements.spec/checkboxes
                          [:checkboxes props
                           (make-options options)
                           (make-confirm confirm)]))))

(defspec datepicker
  iterations
  (prop/for-all [props (s/gen ::ec.spec/datepicker.props)
                 element (s/gen ::elements.spec/datepicker)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/datepicker
                          [:datepicker props
                           (when placeholder
                             [:placeholder placeholder])
                           (make-confirm confirm)]))))

(defspec timepicker
  iterations
  (prop/for-all [props (s/gen ::ec.spec/timepicker.props)
                 element (s/gen ::elements.spec/timepicker)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/timepicker
                          [:timepicker props
                           (when placeholder
                             [:placeholder placeholder])
                           (make-confirm confirm)]))))

(defspec image-element
  iterations
  (prop/for-all [props (s/gen ::elements.spec/image)]
                (render ::elements.spec/image [:img props])))

(defspec multi-static-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/multi-select.props)
                 element (s/gen ::elements.spec/multi-static-select)]
                (let [{placeholder   :placeholder
                       options       :options
                       option-groups :option-groups
                       confirm       :confirm} element]
                  (render ::elements.spec/multi-static-select
                          [:multi-static-select props
                           [:placeholder placeholder]
                           (make-options options)
                           (make-option-groups option-groups)
                           (make-confirm confirm)]))))

(defspec multi-external-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/multi-external-select.props)
                 element (s/gen ::elements.spec/multi-external-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/multi-external-select
                          [:multi-external-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec multi-users-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/multi-users-select.props)
                 element (s/gen ::elements.spec/multi-users-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/multi-users-select
                          [:multi-users-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec multi-conversations-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/multi-conversations-select.props)
                 element (s/gen ::elements.spec/multi-conversations-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/multi-conversations-select
                          [:multi-conversations-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec multi-channels-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/multi-channels-select.props)
                 element (s/gen ::elements.spec/multi-channels-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/multi-channels-select
                          [:multi-channels-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec static-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/static-select.props)
                 element (s/gen ::elements.spec/static-select)]
                (let [{placeholder   :placeholder
                       options       :options
                       option-groups :option-groups
                       confirm       :confirm} element]
                  (render ::elements.spec/static-select
                          [:static-select props
                           [:placeholder placeholder]
                           (make-options options)
                           (make-option-groups option-groups)
                           (make-confirm confirm)]))))

(defspec external-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/external-select.props)
                 element (s/gen ::elements.spec/external-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/external-select
                          [:external-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec users-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/users-select.props)
                 element (s/gen ::elements.spec/users-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/users-select
                          [:users-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec conversations-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/conversations-select.props)
                 element (s/gen ::elements.spec/conversations-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/conversations-select
                          [:conversations-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec channels-select
  iterations
  (prop/for-all [props (s/gen ::ec.spec/channels-select.props)
                 element (s/gen ::elements.spec/channels-select)]
                (let [{placeholder :placeholder
                       confirm     :confirm} element]
                  (render ::elements.spec/channels-select
                          [:channels-select props
                           [:placeholder placeholder]
                           (make-confirm confirm)]))))

(defspec overflow
  iterations
  (prop/for-all [element (s/gen ::elements.spec/overflow)]
                (render ::elements.spec/overflow
                        [:overflow {:action_id (:action_id element)}
                         (make-options (:options element))
                         (make-confirm (:confirm element))])))

(defspec plain-text-input
  iterations
  (prop/for-all [props (s/gen ::ec.spec/plain-text-input.props)
                 element (s/gen ::elements.spec/plain-text-input)]
                (render ::elements.spec/plain-text-input
                        [:plain-text-input props
                         (when (:placeholder element)
                           [:placeholder (:placeholder element)])])))

(defspec radio-buttons
  iterations
  (prop/for-all [props (s/gen ::ec.spec/radio-buttons.props)
                 element (s/gen ::elements.spec/radio-buttons)]
                (let [{options :options
                       confirm :confirm} element]
                  (render ::elements.spec/radio-buttons
                          [:radio-buttons props
                           (make-options options)
                           (make-confirm confirm)]))))

(defspec actions
  iterations
  (prop/for-all [props (s/gen ::bc.spec/block.props)
                 block (s/gen ::blocks.spec/actions)]
                (render ::blocks.spec/actions
                        (apply vector :actions
                               props
                               (:elements block)))))

(defn make-fields
  [fields]
  [:fields
   (map #(vector :text %) fields)])

(defspec section
  iterations
  (prop/for-all [props (s/gen ::bc.spec/block.props)
                 element (s/gen ::blocks.spec/section)]
                (let [{text   :text
                       fields :fields
                       accessory :accessory} element]
                  (render ::blocks.spec/section
                          (cond
                            (and text fields accessory) [:section props [:text text] (make-fields fields) accessory]
                            (and text fields) [:section props [:text text] (make-fields fields)]
                            (and text accessory) [:section props [:text text] accessory]
                            (and fields accessory) [:section props (make-fields fields) accessory]
                            (and text) [:section props [:text text]]
                            (and fields) [:section props (make-fields fields)])))))

(defspec context
  iterations
  (prop/for-all [block (s/gen ::blocks.spec/context)
                 props (s/gen ::bc.spec/block.props)]
                (render ::blocks.spec/context
                        [:context props
                         (map
                          (fn [{:keys [type]
                                :as   elem-props}]
                            (if (= :image type)
                              [type elem-props]
                              [:text elem-props]))
                          (:elements block))])))

(deftest divider
  (is (= {:type     :divider
          :block_id "B123"}
         (surfs.render/render [:divider {:block_id "B123"}])))
  (is (= {:type :divider}
         (surfs.render/render [:divider]))))

(defspec header
  iterations
  (prop/for-all [block (s/gen ::blocks.spec/header)
                 props (s/gen ::bc.spec/block.props)]
                (render ::blocks.spec/header
                        [:header props
                         [:text (:text block)]])))

(defspec image
  iterations
  (prop/for-all [element (s/gen ::blocks.spec/image)
                 props (s/gen ::bc.spec/image.props)]
                (render ::blocks.spec/image
                        [:image props
                         (when (:title element)
                           [:title (:title element)])])))

(defspec input
  iterations
  (prop/for-all [elem (s/gen ::blocks.spec/input)
                 props (s/gen ::bc.spec/input.props)]
                (let [{hint    :hint
                       label   :label
                       element :element} elem]
      ;;; To avoid generating arbitrary hiccup, any generated
      ;;; element will be appended as-is via props
                  (render ::blocks.spec/input
                          [:input props
                           [:label label]
                           element
                           (when hint
                             [:hint hint])]))))

;;; Messages

(defspec message
  5
  (prop/for-all [props (s/gen ::mc.spec/message.props)
                 text (s/gen ::messages.spec/text)
                 blocks (s/gen ::mc.spec/message.children)]
                (render ::messages.spec/message
                        (cond
                          (and text blocks) (apply vector :message props text blocks)
                          (some? text) [:message props text]
                          (seq blocks) (apply vector :message props blocks)))))

;;; Views

(defspec home
  1
  (prop/for-all [props (s/gen ::vc.spec/view.props)
                 blocks (s/gen ::vc.spec/view.children)]
                (render ::views.spec/home
                        (apply vector :home props blocks))))

(defspec modal
  1
  (prop/for-all [props (s/gen ::vc.spec/modal.props)
                 blocks (s/gen ::vc.spec/view.children)]
                (render ::views.spec/modal
                        (apply vector :modal props blocks))))
