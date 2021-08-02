package org.apache.flink.java;

import org.apache.flink.api.java.tuple.Tuple2;

/**
 * CompositeKey
 */
public class CompositeKey extends Tuple2<String, String> {
    public CompositeKey() {
        super();
    }

    public CompositeKey(String f0, String f1) {
        super(f0, f1);
    }
}

