(defproject cab-stats "0.1.0-SNAPSHOT"
  :description "Apache Flink | A Real Time & Hands-On course on Flink - Assignment #1"
  :url "https://github.com/sergemedvedev/udemy/apache-flink-a-real-time-hands-on-course-on-flink/cab-stats"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.apache.flink/flink-clients_2.12 "1.13.1"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resource"]
  :aot  [org.apache.flink.clojure.CabStats]
  :main org.apache.flink.clojure.CabStats)
