package com.sylvona.leona.mvc.components.containers;

import com.sylvona.leona.core.commons.containers.Quadruple;
import com.sylvona.leona.core.commons.containers.Triple;
import com.sylvona.leona.core.commons.containers.Tuple;

public class Containers {
    public static <T1, T2> Tuple<T1, T2> of(T1 item1, T2 item2) {
        return new Tuple<>(item1, item2);
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 item1, T2 item2, T3 item3) {
        return new Triple<>(item1, item2, item3);
    }

    public static <T1, T2, T3, T4> Quadruple<T1, T2, T3, T4> of(T1 item1, T2 item2, T3 item3, T4 item4) {
        return new Quadruple<>(item1, item2, item3, item4);
    }
}
