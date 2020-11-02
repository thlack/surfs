(ns ^:no-doc thlack.surfs.render
  "The surfs rendering implementation. Handles converting hiccup like tags to
   valid Slack blocks"
  (:require [clojure.walk :as walk]
            [thlack.surfs.blocks.components :as blocks]
            [thlack.surfs.composition.components :as comp]
            [thlack.surfs.elements.components :as elements]
            [thlack.surfs.messages.components :as messages]
            [thlack.surfs.views.components :as views]))

;;; The "tags" map defines the "out of the box" components provided by surfs. Maps
;;; a keyword tag to a render function.

(def tags
  {:home                       views/home
   :modal                      views/modal
   :message                    messages/message
   :actions                    blocks/actions
   :context                    blocks/context
   :divider                    blocks/divider
   :header                     blocks/header
   :image                      blocks/image
   :section                    blocks/section
   :input                      blocks/input
   :fields                     blocks/fields
   :button                     elements/button
   :checkboxes                 elements/checkboxes
   :datepicker                 elements/datepicker
   :timepicker                 elements/timepicker
   :img                        elements/img
   :multi-external-select      elements/multi-external-select
   :multi-users-select         elements/multi-users-select
   :multi-conversations-select elements/multi-conversations-select
   :multi-channels-select      elements/multi-channels-select
   :multi-static-select        elements/multi-static-select
   :static-select              elements/static-select
   :external-select            elements/external-select
   :users-select               elements/users-select
   :conversations-select       elements/conversations-select
   :channels-select            elements/channels-select
   :overflow                   elements/overflow
   :plain-text-input           elements/plain-text-input
   :radio-buttons              elements/radio-buttons
   :plain-text                 comp/plain-text
   :label                      comp/plain-text
   :placeholder                comp/plain-text
   :hint                       comp/plain-text
   :title                      comp/plain-text
   :confirm                    comp/confirm
   :option                     comp/option
   :option-group               comp/option-group
   :markdown                   comp/markdown
   :text                       comp/text})

(defn- render-tag
  [[head & args]]
  (if-let [render-fn (tags head)]
    (apply render-fn args)
    (apply vector head args)))

(defn- expand
  "Expand a custom component. A custom component is a hiccup tag with a function
   in the head position."
  [element]
  (if (and (vector? element) (fn? (first element)))
    (apply (first element) (next element))
    element))

(defn- render'
  [element]
  (cond
    (map-entry? element) element
    (vector? element) (render-tag element)
    :else element))

(defn render
  [component]
  (->> component
       (walk/prewalk expand)
       (walk/postwalk render')))
