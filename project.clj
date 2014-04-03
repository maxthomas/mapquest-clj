(defproject mapquest-clj "0.1.0-SNAPSHOT"
  :description "Dead simple clojure wrapper around the MapQuest Open Geocoding API."
  :url "https://github.com/maxthomas/mapquest-clj"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :min-lein-version "2.0.0"
  :global-vars {*warn-on-reflection* true}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.3.1"]]
  :main ^:skip-aot mapquest_clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
