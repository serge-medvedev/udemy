package org.apache.flink.java;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.clojure.types.IpDataRecord;
import org.apache.flink.java.NamedCounters;

/**
 * CountingAggregateFunction
 */
public interface CountingAggregateFunction extends AggregateFunction<IpDataRecord, NamedCounters, NamedCounters> {}

