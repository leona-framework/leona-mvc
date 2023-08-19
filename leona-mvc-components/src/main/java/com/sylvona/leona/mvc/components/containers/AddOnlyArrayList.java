package com.sylvona.leona.mvc.components.containers;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.core.commons.streams.LINQStream;

import java.util.ArrayList;

public class AddOnlyArrayList<T> extends ArrayList<T> implements AddOnlyList<T> {
    @Override
    public LINQStream<T> stream() {
        return LINQ.stream(this);
    }
}
