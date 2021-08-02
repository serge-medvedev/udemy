package org.apache.flink.java;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.java.NamedCounters;

/**
 * IpDataProcessWindowFunction
 */
public abstract class IpDataProcessWindowFunction<IN, W extends Window> extends ProcessWindowFunction<IN, NamedCounters, String, W> {}

