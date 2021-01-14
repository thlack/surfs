# Component Reference

Slack surfaces are composed of blocks, elements and composition objects. Surfs exposes the following components for creating interfaces in Slack applications:

- [Composition objects](#composition-objects)
    - [text](#text)
    - [plain-text](#plain-text)
    - [markdown](#markdown)
    - [confirm](#confirm)
    - [option](#option)
    - [option-group](#option-group)
- [Block elements](#block-elements)
    - [button](#button)
    - [checkboxes](#checkboxes)
    - [datepicker](#datepicker)
    - [img](#img)
    - [multi-static-select](#multi-static-select)
    - [multi-external-select](#multi-external-select)
    - [multi-users-select](#multi-users-select)
    - [multi-conversations-select](#multi-conversations-select)
    - [multi-channels-select](#multi-channels-select)
    - [overflow](#overflow)
    - [plain-text-input](#plain-text-input)
    - [radio-buttons](#radio-buttons)
    - [static-select](#static-select)
    - [external-select](#external-select)
    - [users-select](#users-select)
    - [conversations-select](#conversations-select)
    - [channels-select](#channels-select)
    - [timepicker](#timepicker)
- [Blocks](#blocks)
    - [actions](#actions)
    - [context](#context)
    - [divider](#divider)
    - [header](#header)
    - [image](#image)
    - [input](#input)
    - [section](#section)
- [Views](#views)
    - [modal](#modal)
    - [home](#home)
- [Messages](#messages)
    - [message](#message)
- [Special props](#special-props)
    - [disable_emoji_for](#disable_emoji_for)
    - [selected?](#selected)
    - [private_metadata](#private_metadata)

Instead of exhaustive property documentation, this reference will try
to direct to Slack's documentation while providing examples of usage. In addition
to this, a brief code snippet will be given to explore a component's props
via clojure.spec.

## Composition objects

[Slack documenation for composition objects](https://api.slack.com/reference/block-kit/composition-objects).


### text

Renders to a [text object](https://api.slack.com/reference/block-kit/composition-objects#text).

**Props**:

```clojure
(s/describe :thlack.surfs.props.spec/text)
```

**Usage**:

```clojure
; Input
[:text "Hello!"]

; Output
{:type :plain_text :text "Hello"}
```

The text component supports markdown and plain text explicitly as a simple prop passthrough:

```clojure
; Input
[:text {:type :plain_text :text "Hello" :emoji false}]

; Output

{:type :plain_text :text "Hello" :emoji false}

; Input
[:text {:type :mrkdwn :text "# Hello" :verbatim false}]

; Output
{:type :mrkdwn :text "# Hello" :verbatim false}
```

It may be more convenient to use the more explicit [plain-text](#plain-text) and [markdown](#markdown) components.

### plain-text

Renders to a [text object](https://api.slack.com/reference/block-kit/composition-objects#text).

**Usage**:

```clojure
; Input
[:plain-text "Hello"]

; Output
{:type :plain_text :text "Hello" :emoji true}

; Input
[:plain-text "Goodbye" false]

; Output
{:type :plain_text :text "Goodbye" :emoji false}

; Input
[:plain-text {:text "Greetings" :emoji false}]

; Output
{:type :plain_text :text "Greetings" :emoji false}
```

**Aliases**:

- `[:label]`
- `[:placeholder]`
- `[:hint]`
- `[:title]`


### markdown

Renders to a [text object](https://api.slack.com/reference/block-kit/composition-objects#text).

**Usage**:

```clojure
; Input
[:markdown "# Hello"]

; Output
{:type :mrkdwn :text "# Hello" :verbatim false}

; Input
[:markdown "# Goodbye" true]

; Output
{:type :mrkdwn :text "# Goodbye" :verbatim true}

; Input
[:markdown {:text "# Greetings" :verbatim true}]

; Output
{:type :mrkdwn :text "# Greetings" :verbatim true}
```

### confirm

Renders a [confirmation dialog object](https://api.slack.com/reference/block-kit/composition-objects#confirm).

**Props**:

```clojure
(s/describe :confirm/props)
```

**Children**:

Child | Required | Description
------|----------|------------
[text](#text)| * | Can be a [text](#text) component or a string literal.

**Usage**:

```clojure
; Input
[:confirm {:confirm "Ok!" :deny "Nah!" :title "This is a title!" :style :primary}
  [:text "Are you sure?"]]

[:confirm {:confirm "Ok!" :deny "Nah!" :title "This is a title!" :style :primary} "Are you sure?"]

; Output
{:confirm {:type :plain_text :text "Ok!" :emoji true}
 :deny {:type :plain_text :text "Nah!" :emoji true}
 :title {:type :plain_text :text "This is a title!" :emoji true}
 :text {:type :plain_text :text "Are you sure?"}
 :style :primary}
```

Confirms plain text props - that is `:title`, `:confirm`, and `:deny` - assume a default value of 
`true` for the `:emoji` prop. See docs for the [disable_emoji_for](#disable_emoji_for) prop.

### option

Renders an [option object](https://api.slack.com/reference/block-kit/composition-objects#option).

**Props**

```clojure
(s/describe :option/props)
```

**Children**

Child | Required | Description
------|----------|------------
[text](#text)| * | Can be a [text](#text) component or a string literal.

**Usage**

```clojure
; Input
[:option {:value "1" :description "Oh hello"} "Label"]

; Output
{:value "1",
 :description {:type :plain_text, :text "Oh hello", :emoji true},
 :text {:type :plain_text, :text "Label"}}
```

### option-group

Render an [option group object](https://api.slack.com/reference/block-kit/composition-objects#option_group).

**Children**

Child | Required|Description
------|---------|------
[label](#plain-text) | * |
[option](#option) | 

**Usage**

```clojure
; Input
[:option-group
  [:label "Pizza Toppings"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2"} "Pepperoni"]]

; Output
{:label {:type :plain_text, :text "Pizza Toppings", :emoji true},
 :options
 [{:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}]}
```

## Block elements

[Slack documentation for block elements](https://api.slack.com/reference/block-kit/block-elements).

### button

Renders a [button element](https://api.slack.com/reference/block-kit/block-elements#button).

**Props**:

```clojure
(s/describe :button/props)
```

The `:button` component can safely omit the `:action_id` prop (and thus all props). If `:action_id` is not provided, a uuid string will be generated. This simplifies cases where the button action_id may be ignored - as is the case with buttons that are used for opening urls.

**Children**:

Child | Required|Description
------|---------|------
[text](#text) | * | Provided as a [text](#text) component or a string literal.
[confirm](#confirm) | 


**Usage**:

```clojure
; Input
[:button {:action_id "A123" :value "1"} "Click Me!"]

; Output
{:action_id "A123"
 :value "1"
 :type :button
 :text {:type :plain_text
        :text "Click Me!"}}

; Input
[:button {:action_id "A123" :value "1"}
 [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
  [:text "This is irreversible!"]]
 "Click Me!"]

; Output
{:action_id "A123",
 :value "1",
 :type :button,
 :text {:type :plain_text, :text "Click Me!"},
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary}}

; Input
[:button "Click me"]

; Output
{:type :button,
 :action_id "58113279-334f-42bf-aa25-95b48bf42481",
 :text {:type :plain_text, :text "Click me"}}
```

### checkboxes

Renders a [checkbox group](https://api.slack.com/reference/block-kit/block-elements#checkboxes).

**Props**

```clojure
(s/describe :checkboxes/props)
```

**Children**

Child | Required | Description
------|----------|------------
[option](#option)| * (requires at least one option) | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:checkboxes {:action_id "A123"}
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]]

; Output
{:action_id "A123",
 :type :checkboxes,
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}),
 :initial_options ({:value "2", :text {:type :plain_text, :text "Pepperoni"}})}

; Input
[:checkboxes {:action_id "A123"}
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :type :checkboxes,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}),
 :initial_options ({:value "2", :text {:type :plain_text, :text "Pepperoni"}})}
```

### datepicker

Renders a [date picker element](https://api.slack.com/reference/block-kit/block-elements#datepicker).

**Props**

```clojure
(s/describe :datepicker/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:datepicker {:action_id "A123" :initial_date "2020-11-30"}
  [:placeholder "The date"]]

; Output
{:action_id "A123",
 :initial_date "2020-11-30",
 :type :datepicker,
 :placeholder {:type :plain_text, :text "The date", :emoji true}}

; Input
[:datepicker {:action_id "A123" :initial_date "2020-11-30"}
  [:placeholder "The date"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :initial_date "2020-11-30",
 :type :datepicker,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "The date", :emoji true}}
```

### img

Renders an [image element](https://api.slack.com/reference/block-kit/block-elements#image).

**Props**

```clojure
(s/describe :img/props)
```

**Usage**

```clojure
; Input
[:img {:image_url "http://www.fillmurray.com/200/300" :alt_text "It's Bill Murray"}]

; Output
{:image_url "http://www.fillmurray.com/200/300", :alt_text "It's Bill Murray", :type :image}
```

### multi-static-select

Renders a [static multi select element](https://api.slack.com/reference/block-kit/block-elements#static_multi_select).

**Props**

```clojure
(s/describe :multi-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[option-group](#option-group)| | Required if not using `:option`
[option](#option)| | Required if not using `:option-group`
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:multi-static-select {:action_id "A123" :max_selected_items 5}
  [:placeholder "Pizza Toppings"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:option {:value "3" :selected? true} "Cheese"]]

; Output
{:action_id "A123",
 :max_selected_items 5,
 :type :multi_static_select,
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "3", :text {:type :plain_text, :text "Cheese"}}),
 :placeholder {:type :plain_text, :text "Pizza Toppings", :emoji true},
 :initial_options
 ({:value "2", :text {:type :plain_text, :text "Pepperoni"}} {:value "3", :text {:type :plain_text, :text "Cheese"}})}

; Input
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

; Output
{:action_id "A123",
 :max_selected_items 5,
 :type :multi_static_select,
 :option_groups
 ({:label {:type :plain_text, :text "Veggies", :emoji true},
   :options
   [{:value "1", :text {:type :plain_text, :text "Mushrooms"}}
    {:value "2", :text {:type :plain_text, :text "Peppers"}}]}
  {:label {:type :plain_text, :text "Meats", :emoji true},
   :options
   [{:value "3", :text {:type :plain_text, :text "Pepperoni"}}
    {:value "4", :text {:type :plain_text, :text "Ham"}}]}),
   :placeholder {:type :plain_text, :text "Pizza Toppings", :emoji true},
   :initial_options
   ({:value "2", :text {:type :plain_text, :text "Peppers"}} {:value "4", :text {:type :plain_text, :text "Ham"}})}

; Input
[:multi-static-select {:action_id "A123" :max_selected_items 5}
  [:placeholder "Pizza Toppings"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:option {:value "3" :selected? true} "Cheese"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :max_selected_items 5,
 :type :multi_static_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "3", :text {:type :plain_text, :text "Cheese"}}),
 :placeholder {:type :plain_text, :text "Pizza Toppings", :emoji true},
 :initial_options
 ({:value "2", :text {:type :plain_text, :text "Pepperoni"}} {:value "3", :text {:type :plain_text, :text "Cheese"}})}
```

### multi-external-select

Renders an [external multi select element](https://api.slack.com/reference/block-kit/block-elements#external_multi_select).

**Props**

```clojure
(s/describe :multi-external-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[option](#option)| | All option elements are considered `initial_options`
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:multi-external-select {:action_id "A123" :max_selected_items 5 :min_query_length 3}
  [:placeholder "Pizza Toppings"]
  [:option {:value "1"} "Pepperoni"]
  [:option {:value "2"} "Mushrooms"]]

; Output
{:action_id "A123",
 :max_selected_items 5,
 :min_query_length 3,
 :type :multi_external_select,
 :placeholder {:type :plain_text, :text "Pizza Toppings", :emoji true},
 :initial_options
 ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "2", :text {:type :plain_text, :text "Mushrooms"}})}

; Input
[:multi-external-select {:action_id "A123" :max_selected_items 5 :min_query_length 3}
  [:placeholder "Pizza Toppings"]
  [:option {:value "1"} "Pepperoni"]
  [:option {:value "2"} "Mushrooms"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
   [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :max_selected_items 5,
 :min_query_length 3,
 :type :multi_external_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Pizza Toppings", :emoji true},
 :initial_options
 ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "2", :text {:type :plain_text, :text "Mushrooms"}})}
```

### multi-users-select

Renders a [users multi select element](https://api.slack.com/reference/block-kit/block-elements#users_multi_select).

**Props**

```clojure
(s/describe :multi-users-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:multi-users-select {:action_id "A123" :max_selected_items 3 :initial_users ["U123" "U456"]}
  [:placeholder "Team captains"]]

; Output
{:action_id "A123",
 :max_selected_items 3,
 :initial_users ["U123" "U456"],
 :type :multi_users_select,
 :placeholder {:type :plain_text, :text "Team captains", :emoji true}}

; Input
[:multi-users-select {:action_id "A123" :max_selected_items 3 :initial_users ["U123" "U456"]}
  [:placeholder "Team captains"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :max_selected_items 3,
 :initial_users ["U123" "U456"],
 :type :multi_users_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Team captains", :emoji true}}
```

### multi-conversations-select

Renders a [conversation multi select element](https://api.slack.com/reference/block-kit/block-elements#conversation_multi_select).

**Props**

```clojure
(s/describe :multi-conversations-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#placeholder)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:multi-conversations-select {:action_id "A123"
                              :max_selected_items 3
                              :default_to_current_conversation true
                              :initial_conversations ["C123" "C456"]
                              :filter {:include #{:private}
                                       :exclude_bot_users true
                                       :exclude_external_shared_channels true}}
  [:placeholder "Select conversation"]]

; Output
{:action_id "A123",
 :max_selected_items 3,
 :default_to_current_conversation true,
 :initial_conversations ["C123" "C456"],
 :filter {:include #{:private}, :exclude_bot_users true, :exclude_external_shared_channels true},
 :type :multi_conversations_select,
 :placeholder {:type :plain_text, :text "Select conversation", :emoji true}}

; Input
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

; Output
{:action_id "A123",
 :max_selected_items 3,
 :default_to_current_conversation true,
 :initial_conversations ["C123" "C456"],
 :filter {:include #{:private}, :exclude_bot_users true, :exclude_external_shared_channels true},
 :type :multi_conversations_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Select conversation", :emoji true}}
```

### multi-channels-select

Renders a [channel multi select element](https://api.slack.com/reference/block-kit/block-elements#channel_multi_select)

**Props**

```clojure
(s/describe :multi-channels-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:multi-channels-select {:action_id "A123" :max_selected_items 3 :initial_channels ["C123" "C456"]}
  [:placeholder "Select channel"]]

; Output
{:action_id "A123",
 :max_selected_items 3,
 :initial_channels ["C123" "C456"],
 :type :multi_channels_select,
 :placeholder {:type :plain_text, :text "Select channel", :emoji true}}

; Input
[:multi-channels-select {:action_id "A123" :max_selected_items 3 :initial_channels ["C123" "C456"]}
  [:placeholder "Select channel"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :max_selected_items 3,
 :initial_channels ["C123" "C456"],
 :type :multi_channels_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Select channel", :emoji true}}
```

### overflow

Renders an [overflow menu element](https://api.slack.com/reference/block-kit/block-elements#overflow).

**Props**

```clojure
(s/describe :overflow/props)
```

The `:overflow` component can safely omit the `:action_id` prop (and thus all props). If `:action_id` is not provided, a uuid string will be generated. This simplifies cases where the overflow action_id may be ignored - as is the case with overflow menus containing only urls.

**Children**

Child | Required | Description
------|----------|------------
[option](#option)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:overflow {:action_id "A123"}
  [:option {:value "1" :url "https://google.com"} "Google"]
  [:option {:value "2" :url "https://bing.com"} "Bing"]
  [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]]

; Output
{:action_id "A123",
 :type :overflow,
 :options
 ({:value "1", :url "https://google.com", :text {:type :plain_text, :text "Google"}}
  {:value "2", :url "https://bing.com", :text {:type :plain_text, :text "Bing"}}
  {:value "3", :url "https://duckduckgo.com", :text {:type :plain_text, :text "DuckDuckGo"}})}

; Input
[:overflow {:action_id "A123"}
  [:option {:value "1" :url "https://google.com"} "Google"]
  [:option {:value "2" :url "https://bing.com"} "Bing"]
  [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :type :overflow,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :options
 ({:value "1", :url "https://google.com", :text {:type :plain_text, :text "Google"}}
  {:value "2", :url "https://bing.com", :text {:type :plain_text, :text "Bing"}}
  {:value "3", :url "https://duckduckgo.com", :text {:type :plain_text, :text "DuckDuckGo"}})}

; Input
[:overflow
  [:option {:value "1" :url "https://google.com"} "Google"]
  [:option {:value "2" :url "https://bing.com"} "Bing"]
  [:option {:value "3" :url "https://duckduckgo.com"} "DuckDuckGo"]]

; Output
{:type :overflow,
 :action_id "32adf1df-4eff-4cbd-9c76-d60b0fe1a7b8",
 :options
 ({:value "1", :url "https://google.com", :text {:type :plain_text, :text "Google"}}
  {:value "2", :url "https://bing.com", :text {:type :plain_text, :text "Bing"}}
  {:value "3", :url "https://duckduckgo.com", :text {:type :plain_text, :text "DuckDuckGo"}})}
```

### plain-text-input

Renders a [plain-text input element](https://api.slack.com/reference/block-kit/block-elements#input).

**Props**

```clojure
(s/describe :plain-text-input/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 

**Usage**

```clojure
; Input
[:plain-text-input {:action_id "A123"
                    :initial_value "hello"
                    :multiline true
                    :min_length 1
                    :max_length 100
                    :dispatch_action_config {:trigger_actions_on [:on_enter_pressed]}}
  [:placeholder "Greeting"]]

; Output
{:action_id "A123",
 :initial_value "hello",
 :multiline true,
 :min_length 1,
 :max_length 100,
 :dispatch_action_config {:trigger_actions_on [:on_enter_pressed]},
 :type :plain_text_input,
 :placeholder {:type :plain_text, :text "Greeting", :emoji true}}
```

### radio-buttons

Renders a [radio button group element](https://api.slack.com/reference/block-kit/block-elements#radio).

**Props**

```clojure
(s/describe :radio-buttons/props)
```

**Children**

Child | Required | Description
------|----------|------------
[option](#option)| * | 
[confirm](#confirm)| |

**Usage**

```clojure
; Input
[:radio-buttons {:action_id "A123"}
  [:option {:value "1"} "Pepperoni"]
  [:option {:value "2" :selected? true} "Pineapple"]
  [:option {:value "3"} "Mushrooms"]]

; Output
{:action_id "A123",
 :type :radio_buttons,
 :options
 ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "2", :text {:type :plain_text, :text "Pineapple"}}
  {:value "3", :text {:type :plain_text, :text "Mushrooms"}}),
 :initial_option {:value "2", :text {:type :plain_text, :text "Pineapple"}}}

; Input
[:radio-buttons {:action_id "A123"}
  [:option {:value "1"} "Pepperoni"]
  [:option {:value "2" :selected? true} "Pineapple"]
  [:option {:value "3"} "Mushrooms"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :type :radio_buttons,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :options
 ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "2", :text {:type :plain_text, :text "Pineapple"}}
  {:value "3", :text {:type :plain_text, :text "Mushrooms"}}),
 :initial_option {:value "2", :text {:type :plain_text, :text "Pineapple"}}}
```

### static-select

Renders a [static select element](https://api.slack.com/reference/block-kit/block-elements#static_select).

**Props**

```clojure
(s/describe :static-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[option-group](#option-group)| | Required if not using `:option`
[option](#option)| | Required if not using `:option-group`
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:static-select {:action_id "A123"}
  [:placeholder "Pizza Topping"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:option {:value "3"} "Cheese"]]

; Output
{:action_id "A123",
 :type :static_select,
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "3", :text {:type :plain_text, :text "Cheese"}}),
 :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
 :initial_option
 {:value "2", :text {:type :plain_text, :text "Pepperoni"}}}

; Input
[:static-select {:action_id "A123"}
  [:placeholder "Pizza Topping"]
  [:option-group
    [:label "Veggies"]
    [:option {:value "1"} "Mushrooms"]
    [:option {:value "2" :selected? true} "Peppers"]]
  [:option-group
    [:label "Meats"]
    [:option {:value "3"} "Pepperoni"]
    [:option {:value "4"} "Ham"]]]

; Output
{:action_id "A123",
 :type :static_select,
 :option_groups
 ({:label {:type :plain_text, :text "Veggies", :emoji true},
   :options
   [{:value "1", :text {:type :plain_text, :text "Mushrooms"}}
    {:value "2", :text {:type :plain_text, :text "Peppers"}}]}
  {:label {:type :plain_text, :text "Meats", :emoji true},
   :options
   [{:value "3", :text {:type :plain_text, :text "Pepperoni"}}
    {:value "4", :text {:type :plain_text, :text "Ham"}}]}),
   :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
   :initial_option
   {:value "2", :text {:type :plain_text, :text "Peppers"}}}

; Input
[:static-select {:action_id "A123"}
  [:placeholder "Pizza Topping"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:option {:value "3"} "Cheese"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :type :static_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "3", :text {:type :plain_text, :text "Cheese"}}),
 :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
 :initial_option
 {:value "2", :text {:type :plain_text, :text "Pepperoni"}}}
```

### external-select

Renders an [external select element](https://api.slack.com/reference/block-kit/block-elements#external_select).

**Props**

```clojure
(s/describe :external-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[option](#option)| | Option element is considered `initial_option`
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:external-select {:action_id "A123" :min_query_length 3}
  [:placeholder "Pizza Topping"]
  [:option {:value "1"} "Pepperoni"]]

; Output
{:action_id "A123",
 :min_query_length 3,
 :type :external_select,
 :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
 :initial_option
 {:value "1", :text {:type :plain_text, :text "Pepperoni"}}}

; Input
[:external-select {:action_id "A123" :min_query_length 3}
  [:placeholder "Pizza Topping"]
  [:option {:value "1"} "Pepperoni"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
   [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :min_query_length 3,
 :type :external_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
 :initial_option
 {:value "1", :text {:type :plain_text, :text "Pepperoni"}}}
```

### users-select

Renders a [users select element](https://api.slack.com/reference/block-kit/block-elements#users_select).

**Props**

```clojure
(s/describe :users-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:users-select {:action_id "A123" :initial_user "U123"}
  [:placeholder "Team captain"]]

; Output
{:action_id "A123",
 :initial_user "U123",
 :type :users_select,
 :placeholder {:type :plain_text, :text "Team captain", :emoji true}}

; Input
[:users-select {:action_id "A123" :initial_user "U123"}
  [:placeholder "Team captain"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :initial_user "U123",
 :type :users_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Team captain", :emoji true}}
```

### conversations-select

Renders a [conversation select element](https://api.slack.com/reference/block-kit/block-elements#conversation_select).

**Props**

```clojure
(s/describe :conversations-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:conversations-select {:action_id "A123"
                        :default_to_current_conversation true
                        :initial_conversation "C123"
                        :filter {:include #{:private}
                                 :exclude_bot_users true
                                 :exclude_external_shared_channels true}}
  [:placeholder "Select conversation"]]

; Output
{:action_id "A123",
 :default_to_current_conversation true,
 :initial_conversation "C123",
 :filter {:include #{:private}, :exclude_bot_users true, :exclude_external_shared_channels true},
 :type :conversations_select,
 :placeholder {:type :plain_text, :text "Select conversation", :emoji true}}

; Input
[:conversations-select {:action_id "A123"
                        :default_to_current_conversation true
                        :initial_conversation "C123"
                        :filter {:include #{:private}
                                 :exclude_bot_users true
                                 :exclude_external_shared_channels true}}
  [:placeholder "Select conversation"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :default_to_current_conversation true,
 :initial_conversation "C123",
 :filter {:include #{:private}, :exclude_bot_users true, :exclude_external_shared_channels true},
 :type :conversations_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Select conversation", :emoji true}}
```

### channels-select

Renders a [channel select element](https://api.slack.com/reference/block-kit/block-elements#channel_select)

**Props**

```clojure
(s/describe :channels-select/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| * | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:channels-select {:action_id "A123" :initial_channel "C123"]}
  [:placeholder "Select channel"]]

; Output
{:action_id "A123",
 :initial_channel "C123",
 :type :channels_select,
 :placeholder {:type :plain_text, :text "Select channel", :emoji true}}

; Input
[:channels-select {:action_id "A123" :initial_channel "C123"}
  [:placeholder "Select channel"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :initial_channel "C123",
 :type :channels_select,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "Select channel", :emoji true}}
```

### timepicker

Renders a [time picker element](https://api.slack.com/reference/block-kit/block-elements#timepicker).

**Props**

```clojure
(s/describe :timepicker/props)
```

**Children**

Child | Required | Description
------|----------|------------
[placeholder](#plain-text)| | 
[confirm](#confirm)|

**Usage**

```clojure
; Input
[:timepicker {:action_id "A123" :initial_time "12:30"}
  [:placeholder "The time"]]

; Output
{:action_id "A123",
 :initial_time "12:30",
 :type :timepicker,
 :placeholder {:type :plain_text, :text "The time", :emoji true}}

; Input
[:timepicker {:action_id "A123" :initial_time "12:30"}
  [:placeholder "The time"]
  [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
    [:text "This is irreversible!"]]]

; Output
{:action_id "A123",
 :initial_time "12:30",
 :type :timepicker,
 :confirm
 {:confirm {:type :plain_text, :text "Ok!", :emoji true},
  :deny {:type :plain_text, :text "Nah!", :emoji true},
  :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
  :text {:type :plain_text, :text "This is irreversible!"},
  :style :primary},
 :placeholder {:type :plain_text, :text "The time", :emoji true}}
```

## Blocks

[Slack documenation for layout blocks](https://api.slack.com/reference/block-kit/blocks).

### actions

Renders an [actions block](https://api.slack.com/reference/block-kit/blocks#actions).

**Props**

```clojure
(s/describe :block/props)
```

Props for `:actions` are optional.

**Children**

Child | Required | Description
------|----------|------------
elements | * (requires at least one element) | One or more of [button](#button), [radio-buttons](#radio-buttons), [checkboxes](#checkboxes), [overflow](#overflow), [datepicker](#datepicker), [static-select](#static-select), [external-select](#external-select), [users-select](#users-select), [conversations-select](#conversations-select), [channels-select](#channels-select), [timepicker](#timepicker).

**Usage**

```clojure
; Input
[:actions {:block_id "B123"}
  [:radio-buttons {:action_id "A123"}
    [:option {:value "1"} "Pepperoni"]
    [:option {:value "2" :selected? true} "Pineapple"]
    [:option {:value "3"} "Mushrooms"]]
  [:channels-select {:action_id "A456" :initial_channel "C123"}
    [:placeholder "Select channel"]]]

; Output
{:block_id "B123",
 :elements
 [{:action_id "A123",
   :type :radio_buttons,
   :options
   ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
    {:value "2", :text {:type :plain_text, :text "Pineapple"}}
    {:value "3", :text {:type :plain_text, :text "Mushrooms"}}),
   :initial_option {:value "2", :text {:type :plain_text, :text "Pineapple"}}}
  {:action_id "A456",
   :initial_channel "C123",
   :type :channels_select,
   :placeholder {:type :plain_text, :text "Select channel", :emoji true}}],
 :type :actions}

; Input
[:actions
  [:radio-buttons {:action_id "A123"}
    [:option {:value "1"} "Pepperoni"]
    [:option {:value "2" :selected? true} "Pineapple"]
    [:option {:value "3"} "Mushrooms"]]
  [:channels-select {:action_id "A456" :initial_channel "C123"}
    [:placeholder "Select channel"]]]

; Output
{:elements
 [{:action_id "A123",
   :type :radio_buttons,
   :options
   ({:value "1", :text {:type :plain_text, :text "Pepperoni"}}
    {:value "2", :text {:type :plain_text, :text "Pineapple"}}
    {:value "3", :text {:type :plain_text, :text "Mushrooms"}}),
   :initial_option {:value "2", :text {:type :plain_text, :text "Pineapple"}}}
  {:action_id "A456",
   :initial_channel "C123",
   :type :channels_select,
   :placeholder {:type :plain_text, :text "Select channel", :emoji true}}],
 :type :actions}
```

### context

Renders a [context block](https://api.slack.com/reference/block-kit/blocks#context).

**Props**

```clojure
(s/describe :block/props)
```

Props for `:context` are optional.

**Children**

Child | Required | Description
------|----------|------------
elements | * (requires at least one element) | One or more of [img](#img) or [text](#text).

**Usage**

```clojure
; Input
[:context {:block_id "B123"}
  [:image {:alt_text "It's Bill" :image_url "http://www.fillmurray.com/200/300"}]
  [:text "This is some text"]]

; Output
{:block_id "B123",
 :elements
 [{:alt_text "It's Bill", :image_url "http://www.fillmurray.com/200/300", :type :image}
  {:type :plain_text, :text "This is some text"}],
 :type :context}

; Input
[:context
  [:image {:alt_text "It's Bill" :image_url "http://www.fillmurray.com/200/300"}]
  [:text "This is some text"]]

; Output
{:elements
 [{:alt_text "It's Bill", :image_url "http://www.fillmurray.com/200/300", :type :image}
  {:type :plain_text, :text "This is some text"}],
 :type :context}
```

### divider

Renders a [divider block](https://api.slack.com/reference/block-kit/blocks#divider).

**Props**

```clojure
(s/describe :block/props)
```

Props for `:divider` are optional.

**Usage**

```clojure
; Input
[:divider {:block_id "B123"}]

; Output
{:block_id "B123", :type :divider}
```

### header

Renders a [header block](https://api.slack.com/reference/block-kit/blocks#header).

**Props**

```clojure
(s/describe :block/props)
```

Props for `:header` are optional.

**Children**

Child | Required | Description
------|----------|------------
[text](#text)| * | Can be a [text](#text) component or a string literal.

**Usage**

```clojure
; Input
[:header {:block_id "B123"} "Hello"]

; Output
{:block_id "B123", :type :header, :text {:type :plain_text, :text "Hello"}}

; Input
[:header "Hello"]

; Output
{:type :header, :text {:type :plain_text, :text "Hello"}}
```

### image

Renders an [image block](https://api.slack.com/reference/block-kit/blocks#image).

**Props**

```clojure
(s/describe :image/props)
```

**Children**

Child | Required | Description
------|----------|------------
[title](#plain-text)|  | Can be a component that resolves to plain-text or a string literal |

**Usage**

```clojure
; Input
[:image {:image_url "http://www.fillmurray.com/200/300"
         :alt_text "It's Bill"
         :block_id "B123"}
  [:title "Wowzers!"]]

; Output
{:image_url "http://www.fillmurray.com/200/300",
 :alt_text "It's Bill",
 :block_id "B123",
 :type :image,
 :title {:type :plain_text, :text "Wowzers!", :emoji true}}
```

### input

Renders an [input block](https://api.slack.com/reference/block-kit/blocks#input).

**Props**

```clojure
(s/describe :input/props)
```

Props for `:input` are optional.

**Children**

The `input` block contains multiple plain-text children that would make it conceptually difficult to organize them by spec. As such, the `input` block always assumes the first child is a [plain-text](#plain-text) component used for the label. The order of the [hint](#plain-text) and element components does not matter.

Child | Required | Description
------|----------|------------
[label](#plain-text)| *  | Can be a component that resolves to plain-text or a string literal. Must be the first child.
[hint](#plain-text)| | Must explicitly be a component that resolves to plain-text.
element | * | One of [radio-buttons](#radio-buttons), [checkboxes](#checkboxes), [datepicker](#datepicker), [static-select](#static-select), [external-select](#external-select), [users-select](#users-select), [conversations-select](#conversations-select), [channels-select](#channels-select), [multi-static-select](#multi-static-select), [multi-external-select](#multi-external-select), [multi-users-select](#multi-users-select), [multi-conversations-select](#multi-conversations-select), [multi-channels-select](#multi-channels-select), [timepicker](#timepicker).

**Usage**

```clojure
; Input
[:input {:block_id "B123" :dispatch_action false :optional false}
  [:label "Some input"]
  [:hint "Do something radical"]
  [:plain-text-input {:action_id "A123"
                      :initial_value "hello"}
    [:placeholder "Greeting"]]]

; Output
{:block_id "B123",
 :dispatch_action false,
 :optional false,
 :type :input,
 :label {:type :plain_text, :text "Some input", :emoji true},
 :element
 {:action_id "A123",
  :initial_value "hello",
  :type :plain_text_input,
  :placeholder {:type :plain_text, :text "Greeting", :emoji true}},
 :hint {:type :plain_text, :text "Do something radical", :emoji true}}

; Input
[:input
  [:label "Some input"]
  [:hint "Do something radical"]
  [:plain-text-input {:action_id "A123"
                      :initial_value "hello"}
    [:placeholder "Greeting"]]]

; Output
{:type :input,
 :label {:type :plain_text, :text "Some input", :emoji true},
 :element
 {:action_id "A123",
  :initial_value "hello",
  :type :plain_text_input,
  :placeholder {:type :plain_text, :text "Greeting", :emoji true}},
 :hint {:type :plain_text, :text "Do something radical", :emoji true}}
```

### section

Renders a [section block](https://api.slack.com/reference/block-kit/blocks#section).

**Props**

```clojure
(s/describe :block/props)
```

Props for `:section` are optional.

**Children**

Child | Required | Description
------|----------|------------
[text](#text) | * | Text is required if `fields` is absent. |
[fields](#fields) | * | Fields is required if `text` is absent.
accessory| | One of [button](#button), [checkboxes](#checkboxes), [datepicker](#datepicker), [img](#img), [static-select](#static-select), [external-select](#external-select), [users-select](#users-select), [conversations-select](#conversations-select), [channels-select](#channels-select), [multi-static-select](#multi-static-select), [multi-external-select](#multi-external-select), [multi-users-select](#multi-users-select), [multi-conversations-select](#multi-conversations-select), [multi-channels-select](#multi-channels-select), [timepicker](#timepicker), [overflow](#overflow), or [radio-buttons](#radio-buttons).

* Note: `text` and `fields` can be used simultaneously.

**Usage**

```clojure
; Input
[:section {:block_id "B123"}
  [:text "A section"]]

; Output
{:block_id "B123", :type :section, :text {:type :plain_text, :text "A section"}}

; Input
[:section
  [:text "A section"]]

; Output
{:type :section, :text {:type :plain_text, :text "A section"}}

; Input
[:section {:block_id "B123"}
  [:fields
    [:markdown "# Field 1"]
    [:plain-text "Field 2"]]]

; Output
{:block_id "B123",
 :type :section,
 :fields [{:type :mrkdwn, :text "# Field 1", :verbatim false} {:type :plain_text, :text "Field 2", :emoji true}]}

; Input
[:section {:block_id "B123"}
  [:text "A section"]
  [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]]]

; Output
{:block_id "B123",
 :type :section,
 :text {:type :plain_text, :text "A section"},
 :accessory
 {:action_id "A123",
  :initial_date "2020-11-30",
  :type :datepicker,
  :placeholder {:type :plain_text, :text "The date", :emoji true}}}

; Input
[:section {:block_id "B123"}
  [:fields
    [:markdown "# Field 1"]
    [:plain-text "Field 2"]]
  [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]]]

; Output
{:block_id "B123",
 :type :section,
 :fields [{:type :mrkdwn, :text "# Field 1", :verbatim false} {:type :plain_text, :text "Field 2", :emoji true}],
 :accessory
 {:action_id "A123",
  :initial_date "2020-11-30",
  :type :datepicker,
  :placeholder {:type :plain_text, :text "The date", :emoji true}}}

; Input
[:section {:block_id "B123"}
  [:text "This is an important item"]
  [:fields
    [:markdown "# Field 1"]
    [:plain-text "Field 2"]]
  [:datepicker {:action_id "A123" :initial_date "2020-11-30"}
    [:placeholder "The date"]
    [:confirm {:confirm "Ok!" :deny "Nah!" :title "You sure?!?!?"}
      [:text "This is irreversible!"]]]]

; Output
{:block_id "B123",
 :type :section,
 :text {:type :plain_text, :text "This is an important item"},
 :fields [{:type :mrkdwn, :text "# Field 1", :verbatim false} {:type :plain_text, :text "Field 2", :emoji true}],
 :accessory
 {:action_id "A123",
  :initial_date "2020-11-30",
  :type :datepicker,
  :confirm
  {:confirm {:type :plain_text, :text "Ok!", :emoji true},
   :deny {:type :plain_text, :text "Nah!", :emoji true},
   :title {:type :plain_text, :text "You sure?!?!?", :emoji true},
   :text {:type :plain_text, :text "This is irreversible!"},
   :style :primary},
  :placeholder {:type :plain_text, :text "The date", :emoji true}}}
```

## Views

[Slack documentation for views](https://api.slack.com/reference/surfaces/views).

### modal

Renders a [modal surface](https://api.slack.com/surfaces/modals/using).

**Props**

```clojure
(s/describe :modal/props)
```

**Children**

Child | Required | Description
------|----------|------------
blocks | * (requires at least one block) | One or more of [actions](#actions), [context](#context), [divider](#divider), [header](#header), [image](#image), [input](#input), or [section](#section).

**Usage**

```clojure
; Input
[:modal {:private_metadata {:cool? true}
         :title "Cool Modal!"
         :close "Nah!"
         :submit "Yah!"
         :disable_emoji_for #{:title :close :submit}}
  [:section {:block_id "B123"}
    [:text "Some text"]]]

; Output
{:private_metadata "{:cool? true}",
 :title {:type :plain_text, :text "Cool Modal!", :emoji false},
 :close {:type :plain_text, :text "Nah!", :emoji false},
 :submit {:type :plain_text, :text "Yah!", :emoji false},
 :type :modal,
 :blocks [{:block_id "B123", :type :section, :text {:type :plain_text, :text "Some text"}}]}
```

### home

Renders a [home tab surface](https://api.slack.com/surfaces/tabs/using).

**Props**

```clojure
(s/describe :view/props)
```

**Children**

Child | Required | Description
------|----------|------------
blocks | * (requires at least one block) | One or more of [actions](#actions), [context](#context), [divider](#divider), [header](#header), [image](#image), [input](#input), or [section](#section).

**Usage**

```clojure
; Input
[:home {:private_metadata {:cool? true}}
  [:section {:block_id "B123"}
    [:text "Some text"]]]

; Output
{:private_metadata "{:cool? true}",
 :type :home,
 :blocks [{:block_id "B123", :type :section, :text {:type :plain_text, :text "Some text"}}]}
```

## Messages

[Slack documentation for messages](https://api.slack.com/reference/messaging/payload).

### message

**Props**

```clojure
(s/describe :message/props)
```

* Note: Since the message component renders to a flat structure, additional props can be included (this should simplify using messages in their various contexts).

**Children**

Child | Required | Description
------|----------|------------
[text](#text) | * | Can be a text component or a string literal.
blocks | * (requires at least one block if text omitted) | One or more of [actions](#actions), [context](#context), [divider](#divider), [header](#header), [image](#image), [input](#input), or [section](#section).

**Usage**

```clojure
; Input
[:message {:thread_ts "107"} "Text"]

; Output
{:thread_ts "107", :text "Text"}

; Input
[:message "Just text"]

; Output
{:text "Just text"}

; Input
[:message "Fallback" [:divider]]

; Output
{:text "Fallback", :blocks [{:type :divider}]}

; Input
[:message {:thread_ts "107"} [:divider]]

; Output
{:thread_ts "107", :blocks [{:type :divider}]}

; Input
[:message {:thread_ts "107"} "Fallback" [:divider]]

; Output
{:thread_ts "107", :text "Fallback", :blocks [{:type :divider}]}
```

## Special props

### disable_emoji_for

The `disable_emoji_for` prop can be used to override the default value of `true` for plain text `:emoji` props.

Example:

```clojure
[:confirm {:confirm           "Ok!"
           :deny              "Nah!"
           :title             "This is a title!"
           :disable_emoji_for #{:deny}}
  [:text "Are you sure?"]]
```

### selected?

The `selected?` prop is an extension to [option](#option) composition objects. Any component that supports `initial_option` or `initial_options` attributes can take advantage of this prop to simplify generating initial options.

```clojure
; Input
[:static-select {:action_id "A123"}
  [:placeholder "Pizza Topping"]
  [:option {:value "1"} "Mushrooms"]
  [:option {:value "2" :selected? true} "Pepperoni"]
  [:option {:value "3"} "Cheese"]]

; Output
{:action_id "A123",
 :type :static_select,
 :options
 ({:value "1", :text {:type :plain_text, :text "Mushrooms"}}
  {:value "2", :text {:type :plain_text, :text "Pepperoni"}}
  {:value "3", :text {:type :plain_text, :text "Cheese"}}),
 :placeholder {:type :plain_text, :text "Pizza Topping", :emoji true},
 :initial_option
 {:value "2", :text {:type :plain_text, :text "Pepperoni"}}}
```

This should make it easier to avoid repeating oneself while crafting views :)

### private_metadata

Views support a `private_metadata` prop. This is mostly used for persisting state between views. Thanks to Clojure and edn, Surfs can make this feel pretty natural.

In Surfs, the `private_metadata` prop will be serialized to [edn](https://github.com/edn-format/edn) - making it possible to use plain Clojure structures :)

```clojure
; Input
[:home {:private_metadata {:cool? true}}
  [:section {:block_id "B123"}
    [:text "Some text"]]]

; Output
{:private_metadata "{:cool? true}",
 :type :home,
 :blocks [{:block_id "B123", :type :section, :text {:type :plain_text, :text "Some text"}}]}
```
