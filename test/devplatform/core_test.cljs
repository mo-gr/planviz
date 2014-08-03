(ns devplatform.core-test
  (:require-macros [cemerick.cljs.test :refer (is deftest)])
  (:require [cemerick.cljs.test :as test]
            [devplatform.core :as dp]))

(deftest first-test
         (is (= (dp/add 1 2) 3)))