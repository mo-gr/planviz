(ns devplatform.core
  (:require [clojure.string :refer [split trim blank? replace join lower-case]]))

(enable-console-print!)
(def doc window/document)
(def planInput (.getElementById doc "planInput"))
(def renderInput (.getElementById doc "render"))
(def d3 window/d3)

(def SLOTS 4)
(def WIDTH 60)
(def HEIGHT-HOUR 20)
(def HEIGHT (* 8 HEIGHT-HOUR))
(def DAYS ["Mo" "Di" "Mi" "Do" "Fr" "Sa" "So"])
(def HOURS (map #(mod (+ % 6) 24) (range 24)))

;; apparently those are necessary for advanced compilation :(
(defn day [obj] (int (aget obj "day")))
(defn name [obj] (aget obj "name"))
(defn shift [obj] (int (aget obj "shift")))

(defn shift-color [name]
  (let [toNumber #(-> (window/parseInt (nth (lower-case %1) %2) 36)
                     (/ 36)
                     (* 255)
                     (int))]
    (str "rgb(" (join "," [(toNumber name 0) (toNumber name 1) (toNumber name 2)]) ")")))

(defn calc-x-offset [plan slot-state]
  (let [slot-key [(day plan) (shift plan)]
        slot     (get @slot-state slot-key 0)]
      (swap! slot-state #(assoc % slot-key (inc slot)))
      (+ WIDTH ;; offset for times
         (* slot WIDTH)
         (* WIDTH SLOTS (dec (day plan))))))

(defn plotPlan [plan]
  (let [slot-state (atom {})
        jsPlan (clj->js plan)
        dataSet (-> d3
                    (.select "#canvas")
                    (.selectAll "g")
                    (.data jsPlan (fn [key] (str
                                              (day key)
                                              (shift key)
                                              (name key)))))
        enterSet (.enter dataSet)
        enterGroup (-> enterSet
                     (.append "g"))
        exitSet (.exit dataSet)]
    (-> enterGroup
        (.append "rect")
        (.attr #js {:width WIDTH :height 1 :class "block"})
        (.style "fill" #(shift-color (name %)))
        (.style "stroke-width" 1)
        (.style "stroke" "black"))
    (-> enterGroup
        (.append "text")
        (.style "fill" "white")
        (.attr #js {:y (/ HEIGHT 2) :x 3})
        (.text #(name %)))
    (-> dataSet
        (.transition)
        (.duration 1000)
        (.attr "transform" #(str "translate("
                                 (calc-x-offset % slot-state)
                                 ","
                                 (* HEIGHT-HOUR (- (shift %) 4)) ")"))
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
  (let [v (aget planInput "value")
        p (extractPlan v)]
    (plotPlan p)))

(defn prepareCanvas! []
  (-> d3
      (.select "#canvas")
      (.attr "width" (+ WIDTH (* SLOTS (count DAYS) WIDTH))))
  (-> d3
      (.select "#canvas")
      (.selectAll "text.days")
      (.data (clj->js DAYS))
      (.enter)
      (.append "text")
      (.attr #js {:class "days"})
      (.attr "transform" #(str "translate("
                               (+ 5 WIDTH (* (* SLOTS WIDTH) %2))
                               ",20)"))
      (.text #(str %)))
  (-> d3
      (.select "#canvas")
      (.selectAll "text.hours")
      (.data (clj->js HOURS))
      (.enter)
      (.append "text")
      (.attr #js {:class "dahoursys"})
      (.attr "transform" #(str "translate(" (- WIDTH 20) ", "
                               (+ HEIGHT-HOUR ;; offset legend
                                  (/ HEIGHT-HOUR 2) ;; text to the middle
                                  (* HEIGHT-HOUR (inc %2)))
                               ")"))
      (.text #(str %))))

(defn setup! []
  (prepareCanvas!)
  (.addEventListener renderInput "click" updatePlan))

(setup!)
