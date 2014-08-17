(ns devplatform.core
  (:require [clojure.string :refer [split trim blank? replace]]))

(enable-console-print!)
(def doc window/document)
(def planInput (.getElementById doc "planInput"))
(def renderInput (.getElementById doc "render"))
(def d3 window/d3)

(def shift-color ["red" "green" "blue" "yellow"])

(def WIDTH 40)
(def HEIGHT 160)

(defn plotPlan [plan]
  (let [jsPlan (clj->js plan)
        dataSet (-> d3
                    (.select "#canvas")
                    (.selectAll "rect")
                    (.data jsPlan))
        enterSet (.enter dataSet)
        exitSet (.exit dataSet)]
    (-> enterSet
        (.append "rect")
        (.attr #js {:width WIDTH :height 1})
        (.style "fill" (fn [d] (nth shift-color (.-shift d) 0)))
        (.text #(.-name %)))
    (-> dataSet
        (.transition)
        (.attr #js {:height HEIGHT}))
    (-> exitSet
        (.remove))))

(defn extractPlan [inputString]
  (->> (split inputString "plan")
       (map trim)
       (remove blank?)
       (map #(replace % #"[\(\)]" ""))
       (map #(split % ","))
       (map #(zipmap [:day :shift :name] %))))

(defn updatePlan [e]
  (println e)
  (let [v (.-value planInput)
        p (extractPlan v)]
    (plotPlan p)))

(defn setup! []
  (.addEventListener renderInput "click" updatePlan))

#_(setup!)

(plotPlan [{:day 1 :shift 1 :name Bob}] )