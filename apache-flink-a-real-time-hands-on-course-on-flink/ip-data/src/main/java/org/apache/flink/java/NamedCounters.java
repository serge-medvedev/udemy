package org.apache.flink.java;

import java.util.HashMap;

/**
 * NamedCounters
 */
public class NamedCounters extends HashMap<String, Long> {
    public static NamedCounters combine(NamedCounters lhs, NamedCounters rhs) {
        lhs.forEach((k, v) -> rhs.merge(k, v, Long::sum));

        return rhs;
    }

    public static NamedCounters add(NamedCounters m, String k, Long v) {
        m.merge(k, v, Long::sum);

        return m;
    }
}

