package org.lyora.leona.mvc.components.containers;

import org.lyora.leona.core.commons.streams.LINQ;
import org.lyora.leona.core.commons.streams.LINQStream;

import java.util.Iterator;

public class TupleOperators {
    public static LINQStream<Object> unpack(Tuple<?, ?> packedTuple, boolean unpackRight) {
        return LINQ.stream(new TupleIterator(packedTuple, unpackRight));
    }

    public static LINQStream<Object> unpack(Tuple<?, ?> packedTuple) {
        return unpack(packedTuple, false);
    }


    public static class TupleIterator implements Iterable<Object>, Iterator<Object> {
        private final boolean rightSided;
        private Object standby;

        public TupleIterator(Tuple<?, ?> packedTuple, boolean rightSided) {
            standby = packedTuple;
            this.rightSided = rightSided;
        }

        public TupleIterator(Tuple<?, ?> packedTuple) {
            this(packedTuple, false);
        }

        @Override
        public Iterator<Object> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return standby != null;
        }

        @Override
        public Object next() {
            if (standby instanceof Tuple<?, ?> tuple) {
                standby = rightSided ? tuple.item2() : tuple.item1();
                return rightSided ? tuple.item1() : tuple.item2();
            }

            Object value = standby;
            standby = null;
            return value;
        }
    }
}
