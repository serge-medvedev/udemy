(ns org.apache.flink.clojure.IpData
  (:require [org.apache.flink.clojure.types :refer :all])
  (:import (org.apache.flink.streaming.api.environment StreamExecutionEnvironment)
           (org.apache.flink.streaming.api.windowing.assigners TumblingEventTimeWindows)
           (org.apache.flink.streaming.api.windowing.time Time)
           (org.apache.flink.api.common.functions MapFunction)
           (org.apache.flink.api.common.eventtime SerializableTimestampAssigner)
           (org.apache.flink.streaming.api.functions.sink.filesystem StreamingFileSink)
           (org.apache.flink.core.fs Path)
           (org.apache.flink.api.common.serialization SimpleStringEncoder)
           (org.apache.flink.clojure.types IpDataRecord)
           (org.apache.flink.java NamedCounters IpDataWatermarkStrategy))
  (:use [clojure.string :only [split]])
  (:gen-class))

(defn -main [& args]
  (let [flink-env (StreamExecutionEnvironment/getExecutionEnvironment)
        build-file-sink (fn [p] (-> (StreamingFileSink/forRowFormat (Path. p) (SimpleStringEncoder. "UTF-8"))
                                    .build))
        events (-> (.readTextFile flink-env "/ip-data.txt")
                   (.map (reify MapFunction
                           (map [_ value]
                             (let [[f0 f1 f2 f3 f4 f5] (split value #",")]
                               (->IpDataRecord f0 f1 (Long/parseLong f2) f3 f4 (Long/parseLong f5))))))
                   (.returns IpDataRecord)
                   (.filter (->CountryFilter "US"))
                   (.assignTimestampsAndWatermarks
                     (-> (IpDataWatermarkStrategy/forBoundedOutOfOrderness (java.time.Duration/ofMinutes 10))
                         (.withTimestampAssigner (reify SerializableTimestampAssigner
                                                   (extractTimestamp [_ e _]
                                                     (->> (:time-before-next-click e)
                                                          (* 1000)
                                                          (- (System/currentTimeMillis)))))))))]
    (-> events
        (.windowAll (TumblingEventTimeWindows/of (Time/seconds 10)))
        (.aggregate (->IpDataCounter :website))
        (.addSink (build-file-sink "/ip-data/total-number-of-clicks-per-site"))
        (.setParallelism 1))
    (-> events
        (.windowAll (TumblingEventTimeWindows/of (Time/seconds 10)))
        (.aggregate (->IpDataCounter :website))
        (.map (reify MapFunction
                (map [_ value]
                  (let [m (into {} value)
                        [winnerName winnerScore] (apply max-key val m)
                        [loserName loserScore] (apply min-key val m)]
                    (doto (NamedCounters.)
                          (.put winnerName winnerScore)
                          (.put loserName loserScore))))))
        (.returns NamedCounters)
        (.addSink (build-file-sink "/ip-data/site-popularity-contest"))
        (.setParallelism 1))
    (-> events
        (.keyBy (->SingleFieldSelector :website))
        (.window (TumblingEventTimeWindows/of (Time/seconds 10)))
        (.aggregate (->IpDataCounter :user-id) (IpDataProcessWindowFunction.))
        (.addSink (build-file-sink "/ip-data/unique-site-visitors"))
        (.setParallelism 1))
    (-> events
        (.keyBy (->SingleFieldSelector :website))
        (.window (TumblingEventTimeWindows/of (Time/seconds 10)))
        (.aggregate (->AvgTimeSpentCalc :time-before-next-click) (IpDataProcessWindowFunction.))
        (.addSink (build-file-sink "/ip-data/avg-time-on-site"))
        (.setParallelism 1))

    (.execute flink-env))
)

