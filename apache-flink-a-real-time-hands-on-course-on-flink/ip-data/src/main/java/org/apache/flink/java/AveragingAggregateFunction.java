package org.apache.flink.java;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.clojure.types.IpDataRecord;
import org.apache.flink.java.SumAndCount;

/**
 * AveragingAggregateFunction
 */
public interface AveragingAggregateFunction extends AggregateFunction<IpDataRecord, SumAndCount, Double> {}

