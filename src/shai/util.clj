(ns shai.util
  (:require [clojure.java.shell :as shell]))

(set! *warn-on-reflection* true)

(def sh (partial shell/sh "bash" "-c"))
