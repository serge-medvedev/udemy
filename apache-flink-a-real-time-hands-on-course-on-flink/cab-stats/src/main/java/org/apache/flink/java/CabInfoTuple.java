package org.apache.flink.java;

import org.apache.flink.api.java.tuple.Tuple9;

/**
 * CabInfoTuple
 */
public class CabInfoTuple extends Tuple9<String, String, String, String, String, String, String, Long, Long> {
    public CabInfoTuple() {
        super();
    }

    public CabInfoTuple(String f0, String f1, String f2, String f3, String f4, String f5, String f6, Long f7, Long f8) {
        super(f0, f1, f2, f3, f4, f5, f6, f7, f8);
    }
}

