(ns thlack.surfs
  "A hiccup-like interface for creating views in Slack applications via blocks.
   https://api.slack.com/reference/block-kit/blocks"
  (:require [thlack.surfs.render :as surfs.render]
            [thlack.surfs.props :as props]))

(defn render
  "Render one or more surfs components into a data structure
   fit for use by Slack. A component is a vector that follows a hiccup-like syntax.
   
   ```clojure
   (render
     [:section {:block_id \"B123\"}
       [:text \"Section text\"]
       [:datepicker {:action_id \"A123\" :initial_date \"2020-11-30\"}
         [:placeholder \"The date\"]]]
   
     [:actions {:block_id \"B456\"}
       [:radio-buttons {:action_id \"A456\"}
         [:option {:value \"1\"} \"Pepperoni\"]
         [:option {:value \"2\" :selected? true} \"Pineapple\"]
         [:option {:value \"3\"} \"Mushrooms\"]]
       [:channels-select {:action_id \"A789\" :initial_channel \"C123\"}
         [:placeholder \"Select channel\"]]])
   ```
  
  Custom components can be written as functions, and then used as the head of the vector:
                             
  ```clojure
  (defn fun-text
    [str]
    [:text str])

  (defn custom
    [props & children]
    [:section {:block_id \"B123\"}
      [fun-text (:text props)]
      children])
                             
  (render [custom {:text \"Such fun\"}])
  ```
  Note: The returned data structure must be serialized to json (not included) before being sent to Slack."
  [& components]
  (->> components
       (map surfs.render/render)
       (props/flatten-children)))

(defn- build-defc-body
  [[bindings & body]]
  `(~bindings
    (thlack.surfs/render ~@body)))

(defn- defc'
  "Helper for generating the body of defc"
  [body]
  (let [head (first body)]
    (if (string? head)
      (into [head] (build-defc-body (rest body)))
      (build-defc-body body))))

(defmacro defc
  "```
   (defc name doc-string? render-body)
   ```
   
   Define a reusable surfs component.
   
   Defc creates a function that wraps the body in a call to thlack.surfs/render
   and returns a SINGLE result. This macro is great for defining modals, home tabs, and messages - really
   any kind of slack view you want to encapsulate or reuse.
   
   The generated function will have the same name and semantics as the macro defntion,
   so function specs can be written against it easily.
   
   Components created via defc can also be reused within other components.
   
   Usage:
   
   ```clojure
   (defc my-modal
     [title]
     [:modal {:title title
              :close \"Close\"
              :submit \"Submit\"}
       [:section {:block_id \"B123\"}
         [:text \"Some text\"]]])
   
   ;; Defines function my-modal which can be called
   ;; to return rendered blocks/elements.
   
   (my-modal \"Great Modal\")
   
   ;; Components can be used as custom elements as well.
   
   (defc special-text
     [txt]
     [:text txt])
   
   (render [:context {:block_id \"B123\"}
             [special-text \"So special!\"]])
   ```"
  [name & body]
  `(defn ~name
     ~@(defc' body)))
