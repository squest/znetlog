(defproject znetlog "0.1.0"
  :repl-options {:init-ns znetlog.repl}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.3.1"]
                 [lib-noir "0.8.6"]
                 [http-kit "2.1.19"]
                 [com.cemerick/url "0.1.1"]
                 [noir-exception "0.2.2"]
                 [environ "1.0.0"]
                 [clj-time "0.8.0"]
                 [im.chit/cronj "1.4.2"]
                 [com.taoensso/timbre "3.3.0"]
                 [prone "0.6.0"]
                 [enlive "1.1.5"]
                 [com.ashafa/clutch "0.4.0-RC1"]
                 [zenedu.squest/questdb "0.2.1"]
								 [com.stuartsierra/component "0.2.2"]
                 [expectations "2.0.9"]]
  
  :ring {:handler znetlog.handler/app,
         :init znetlog.handler/init,
         :destroy znetlog.handler/destroy}
  :profiles {:uberjar {:aot :all},
             :production
             {:ring
              {:open-browser? false, :stacktraces? false, :auto-reload? false}},
             :dev
             {:dependencies [[ring-mock "0.1.5"]
                             [ring/ring-devel "1.3.1"]
                             [pjstadig/humane-test-output "0.6.0"]],
              :injections [(require 'pjstadig.humane-test-output)
                           (pjstadig.humane-test-output/activate!)],
              :env {:dev true}}}
  :url "http://example.com/FIXME"
  :main znetlog.core
  :jvm-opts ["-server"]
  :plugins [[lein-ring "0.8.11"]
            [codox "0.8.10"]
            [lein-expectations "0.0.8"]
            [lein-environ "1.0.0"]
            [lein-autoexpect "1.2.2"]]
  :description "FIXME: write description"
  :min-lein-version "2.0.0")
