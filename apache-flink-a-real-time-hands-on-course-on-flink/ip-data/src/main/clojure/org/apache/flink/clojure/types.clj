(ns org.apache.flink.clojure.types
  (:use [clojure.string :only [join]])
  (:import (org.apache.flink.api.common.functions FilterFunction)
           (org.apache.flink.java StringKeySelector
                                  NamedCounters
                                  SumAndCount
                                  CountingAggregateFunction
                                  AveragingAggregateFunction)))

(deftype IpDataRecord
  [user-id
   network-name
   ^Long user-ip
   user-country
   website
   ^Long time-before-next-click]

  java.lang.Object
  (toString [this] (join ", " [user-id network-name user-ip user-country website time-before-next-click]))

  clojure.lang.ILookup
  (valAt [this k]
    (case k
      :user-id user-id
      :network-name network-name
      :user-ip user-ip
      :user-country user-country
      :website website
      :time-before-next-click time-before-next-click
      nil)))

(deftype CountryFilter [country] FilterFunction
  (filter [this value] (= country (:user-country value))))

(deftype SingleFieldSelector [kw] StringKeySelector
  (getKey [this value] (-> value kw str)))

(deftype IpDataCounter [kw] CountingAggregateFunction
  (createAccumulator [this] (NamedCounters.))
  (merge [this acc1 acc2] (NamedCounters/combine acc1 acc2))
  (add [this value acc] (NamedCounters/add acc (-> value kw str) 1))
  (getResult [this acc] acc))

(deftype AvgTimeSpentCalc [kw] AveragingAggregateFunction
  (createAccumulator [this] (SumAndCount. 0 0))
  (merge [this acc1 acc2] (SumAndCount. (+ (.getField acc1 0) (.getField acc2 0))
                                        (+ (.getField acc1 1) (.getField acc2 1))))
  (add [this value acc] (SumAndCount. (+ (.getField acc 0) (kw value))
                                      (+ 1 (.getField acc 1))))
  (getResult [this acc] (double (/ (.getField acc 0) (.getField acc 1)))))

(gen-class
  :name IpDataProcessWindowFunction
  :extends org.apache.flink.java.IpDataProcessWindowFunction
  :prefix "kpf-"
  :methods [[process [String
                      org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction$Context
                      Iterable
                      org.apache.flink.util.Collector] void]])
(defn kpf-process [this k context elements out]
  (doseq [e elements]
    (.collect out (doto (NamedCounters.)
                        (.put k (if (instance? org.apache.flink.java.NamedCounters e) (.size e) e))))))

