(ns thlack.surfs.repl
  "Repl helpers for surfs. Useful for exploring component specs and
   getting documentation."
  (:require [thlack.surfs.repl.impl :as impl]))

(defn describe
  "Returns raw metadata about a component. Includes function metadata as well
   as the fspec of the component's render function. describe might be considered
   them most \"low level\" repl utility.
   
   Usage:
   
   ```clojure
   (describe :static-select)
   ```"
  [tag]
  (impl/describe tag))

(defn doc
  "Prints the full documentation of a component. This documentation includes component signatures
   as well as usage examples.
   
   Usage:
   
   ```clojure
   (doc :static-select)
   ```"
  [tag]
  (impl/doc tag))

(defn props
  "Returns the spec for a component's props if it has them. Specs leveraging merge
   will have their keywords fully expanded into a valid `keys` spec.
   
   ```clojure
   (props :multi-external-select)
   ```"
  [tag]
  (impl/props tag))
