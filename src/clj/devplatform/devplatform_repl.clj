(ns devplatform.devplatform-repl
  (:require [cemerick.piggieback :refer [cljs-repl]]
            [cljs.repl.browser :refer [repl-env]]))

(defn cljs-repl! [] (cljs-repl :repl-env (repl-env :port 9000)))
