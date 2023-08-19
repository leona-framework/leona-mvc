package org.lyora.leona.mvc.components.containers;

import org.lyora.leona.core.commons.streams.LINQ;
import org.lyora.leona.core.commons.streams.LINQStream;

import java.util.ArrayList;

public class AddOnlyArrayList<T> extends ArrayList<T> implements AddOnlyList<T> {
    @Override
    public LINQStream<T> stream() {
        return LINQ.stream(this);
    }
}
