(ns devplatform-figwheel
  (:require [figwheel.client :as fw :include-macros true]))

(fw/watch-and-reload)