(ns devplatform.server
  (:require [devplatform.core :refer [plotPlan prepareCanvas!]]
            [cljs.nodejs :as node]))

(node/require "d3")
(def jsdom (node/require "jsdom"))

(.env jsdom #js {:html "<html><body><svg xmlns=\"http://www.w3.org/2000/svg\" id='canvas' width='600' height='800'></svg></body></html>"
                          :done (fn [err, win]
                                  (let [doc (aget win "document")
                                        svg (.querySelector doc "#canvas")]
                                    (prepareCanvas! doc)
                                    (plotPlan doc [{:day 1 :shift 6 :name "Bob"}
                                                   {:day 1 :shift 6 :name "Alice"}
                                                   {:day 2 :shift 14 :name "Bob"}
                                                   {:day 2 :shift 14 :name "Alice"}])
                                    (println "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                                    (println "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">")
                                    (println (aget svg "outerHTML"))))}) 

