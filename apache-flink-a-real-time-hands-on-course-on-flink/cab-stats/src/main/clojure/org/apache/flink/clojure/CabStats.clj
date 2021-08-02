(ns org.apache.flink.clojure.CabStats
  (:import (org.apache.flink.api.common.functions MapFunction GroupReduceFunction FilterFunction)
           (org.apache.flink.api.java ExecutionEnvironment)
           (org.apache.flink.java CabInfoTuple))
  (:require [clojure.string :as str])
  (:gen-class))


(deftype tokenizer [] MapFunction
  (map [this value]
    ; cab id, cab number plate, cab type, cab driver name, ongoing trip/not, pickup location, destination, passenger count
    (let [[f0 f1 f2 f3 f4 f5 f6 f7] (str/split value #",")
          pas-count (try (Long/parseLong f7)
                         (catch Exception e 0))]
      (CabInfoTuple. f0 f1 f2 f3 f4 f5 f6 pas-count 1))))
(deftype idle-cab-filter [] FilterFunction
  (filter [this value] (= "yes" (.getField value 4))))
(deftype pas-count-reducer [] GroupReduceFunction
  (reduce [this values collector]
    (let [result (reduce (fn [memo value]
                           (.setField memo (+ (.getField memo 7) (.getField value 7)) 7)
                           (.setField memo (+ (.getField memo 8) (.getField value 8)) 8)
                           memo) values)]
      (.setField result (quot (.getField result 7) (.getField result 8)) 7)
      (.collect collector result))))


(def flink-env (ExecutionEnvironment/getExecutionEnvironment))
(def ds (-> (.readTextFile flink-env "/cabs.txt")
            (.map (tokenizer.))
            (.returns CabInfoTuple)
            (.filter (idle-cab-filter.))))
(def most-popular-destination (-> ds
                                  (.groupBy (int-array [6]))
                                  (.sum 8)
                                  (.maxBy (int-array [8]))))
(def avg-pas-per-pickup-loc (-> ds
                                (.groupBy (int-array [5]))
                                (.reduceGroup (pas-count-reducer.))
                                (.returns CabInfoTuple)))
(def avg-trips-per-driver (-> ds
                              (.groupBy (int-array [3]))
                              (.reduceGroup (pas-count-reducer.))
                              (.returns CabInfoTuple)))


(defn -main [& args]
  (.print most-popular-destination)
  (.print avg-pas-per-pickup-loc)
  (.print avg-trips-per-driver)
)

