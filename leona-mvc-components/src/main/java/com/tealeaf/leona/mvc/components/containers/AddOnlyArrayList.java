package com.tealeaf.leona.mvc.components.containers;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import com.tealeaf.leona.mvc.components.streams.LINQStream;

import java.util.ArrayList;

public class AddOnlyArrayList<T> extends ArrayList<T> implements AddOnlyList<T> {
    @Override
    public LINQStream<T> stream() {
        return LINQ.stream(this);
    }
}
