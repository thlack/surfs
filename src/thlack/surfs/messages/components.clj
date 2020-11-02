(ns thlack.surfs.messages.components
  (:require [clojure.spec.alpha :as s]
            [thlack.surfs.props :as props]
            [thlack.surfs.validation :refer [validated]]
            [thlack.surfs.messages.spec :as messages.spec]
            [thlack.surfs.messages.components.spec]))

(defn message
  "Define a message. Supports the common message definition defined [here](https://api.slack.com/reference/messaging/payload).
   
   The spec does not require text AND blocks, but text is [HIGHLY recommended](https://api.slack.com/methods/chat.postMessage#text_usage) by
   Slack.
   
   Component usage:
   
   ```clojure
   [:message
    \"Fallback text for great good\"
    [:section {:block_id \"B123\"}
      [:text \"Text one\"]]
    [:section {:block_id \"B456\"}
      [:text \"Text two\"]]]
   ```
   
   With props:
   
   ```clojure
   [:message {:thread_ts \"1049393493.23\"}
     \"Fallback text only\"]
   ```
   
   With just text:
   
   ```clojure
   [:message \"This is a text only message\"]
   ```
   
   With just blocks:
   
   ```clojure
   [:message
    [:section {:block_id \"B123\"}
     [:text \"Text one\"]]
    [:section {:block_id \"B456\"}
     [:text \"Text two\"]]]
   ```"
  [& args]
  (let [[props & children] (props/parse-args args)
        text (some #(if (string? %) % nil) children)
        blocks (filter (complement string?) children)]
    (-> props
        (cond->
         (some? text) (assoc :text text)
         (seq blocks) (assoc :blocks (props/flatten-children blocks)))
        (validated ::messages.spec/message))))

(s/fdef message
  :args (s/alt :text-only             (s/cat :text :message/text)
               :props-text-only       (s/cat :props :message/props
                                             :text :message/text)
               :blocks-only           (s/cat :blocks :message/children)
               :props-blocks-only     (s/cat :props :message/props
                                             :blocks :message/children)
               :blocks-and-text       (s/cat :text :message/text
                                             :blocks :message/children)
               :props-blocks-and-text (s/cat :props :message/props
                                             :text :message/text
                                             :blocks :message/children))
  :ret  ::messages.spec/message)
