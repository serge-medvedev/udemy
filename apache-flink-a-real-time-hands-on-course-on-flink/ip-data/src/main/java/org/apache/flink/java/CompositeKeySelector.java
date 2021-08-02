package org.apache.flink.java;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.java.CompositeKey;

/**
 * CompositeKeySelector
 */
public interface CompositeKeySelector<IN> extends KeySelector<IN, CompositeKey> {}

