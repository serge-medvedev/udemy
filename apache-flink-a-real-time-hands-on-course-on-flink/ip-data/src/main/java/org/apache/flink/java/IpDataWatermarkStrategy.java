package org.apache.flink.java;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.clojure.types.IpDataRecord;

/**
 * IpDataWatermarkStrategy
 */
public interface IpDataWatermarkStrategy extends WatermarkStrategy<IpDataRecord> {
    static WatermarkStrategy<IpDataRecord> forBoundedOutOfOrderness(java.time.Duration maxOutOfOrderness) {
        return WatermarkStrategy.<IpDataRecord>forBoundedOutOfOrderness(maxOutOfOrderness);
    }
}

