(defproject devplatform "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]
                 [figwheel "0.1.3-SNAPSHOT"]]
  :jvm-opts ["-Xmx1G"]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.3-SNAPSHOT"]
            [com.cemerick/clojurescript.test "0.3.1"]]
  :figwheel {
    :http-server-root "public"
    :port 3449
    :css-dirs ["resources/public/css"]}
  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src/devplatform" "src/figwheel"]
              :compiler {
                :output-to  "resources/public/devplatform.js"
                :output-dir "resources/public/out"
                :optimizations :none
                :source-map true}}
             {:id "test"
              :source-paths ["src/devplatform" "test"]
              :notify-command ["phantomjs" :cljs.test/runner "devplatform_test.js"]
              :compiler {
                :output-to "devplatform_test.js"
                :optimizations :whitespace}}]})
