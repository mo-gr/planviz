(ns devplatform.core
  (:require [clojure.string :refer [split trim blank? replace]]))

(enable-console-print!)

(def doc window/document)
(def planInput (.getElementById doc "planInput"))

(defn pipelog [x] (.log window/console x) x)

(defn extractPlan [inputString]
  (-> inputString
      (split "plan")
      (->> (map trim))
      (->> (remove blank?))
      (->> (map #(replace % #"[\(\)]" "")))
      (->> (map #(split % ",")))
      (->> (map #(zipmap [:day :shift :name] %)))))

(defn updatePlan [e]
  (let [t (.-target e)
        v (.-value t)
        p (extractPlan v)]
    (println p)))

(defn setup! []
  (.addEventListener planInput "change" updatePlan))

#_(setup!)

(extractPlan (.-value planInput))