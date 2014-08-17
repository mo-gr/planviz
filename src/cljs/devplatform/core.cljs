(ns devplatform.core
  (:require [clojure.string :refer [split trim blank? replace join lower-case]]))

(enable-console-print!)
(def doc window/document)
(def planInput (.getElementById doc "planInput"))
(def renderInput (.getElementById doc "render"))
(def d3 window/d3)

(def WIDTH 60)
(def HEIGHT-HOUR 20)
(def HEIGHT (* 8 HEIGHT-HOUR))

(defn shift-color [name]
  (let [toNumber #(-> (window/parseInt (nth (lower-case %1) %2) 36)
                     (/ 36)
                     (* 255)
                     (int))]
    (str "rgb(" (join "," [(toNumber name 0) (toNumber name 1) (toNumber name 2)]) ")")))

(println (shift-color "Wally"))
(println (shift-color "bob"))

(defn plotPlan [plan]
  (let [jsPlan (clj->js plan)
        dataSet (-> d3
                    (.select "#canvas")
                    (.selectAll "g")
                    (.data jsPlan (fn [key] (println key) (str
                                                            (.-day key)
                                                            (.-shift key)
                                                            (.-name key)))))
        enterSet (.enter dataSet)
        enterGroup (-> enterSet
                     (.append "g")
                     (.attr "transform" #(str "translate(" (* WIDTH (.-day %)) ",0)")))
        exitSet (.exit dataSet)]
    (-> enterGroup
        (.append "rect")
        (.attr #js {:width WIDTH :height 1 :class "block"})
        (.style "fill" #(shift-color (.-name %)))
        (.style "stroke" 1))
    (-> enterGroup
        (.append "text")
        (.style "fill" "white")
        (.attr #js {:y (/ HEIGHT 2) :x 3})
        (.text #(.-name %)))
    (-> dataSet
        (.transition)
        (.duration 1000)
        (.attr "transform" #(str "translate("
                                 (* WIDTH (.-day %))
                                 ","
                                 (* HEIGHT-HOUR (dec (.-shift %))) ")"))
        (.selectAll ".block")
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

(setup!)

#_(plotPlan [{:day 1 :shift 1 :name "Bob"} {:day 1 :shift 2 :name "Betty"}] )