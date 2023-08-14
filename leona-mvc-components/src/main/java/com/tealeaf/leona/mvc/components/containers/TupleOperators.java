package com.tealeaf.leona.mvc.components.containers;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import com.tealeaf.leona.mvc.components.streams.LINQStream;

import java.util.Iterator;

public class TupleOperators {
    public static LINQStream<Object> unpack(Tuple<?, ?> packedTuple, boolean unpackRight) {
        return LINQ.stream(new TupleIterator(packedTuple, unpackRight));
    }

    public static LINQStream<Object> unpack(Tuple<?, ?> packedTuple) {
        return unpack(packedTuple, false);
    }


    public static class TupleIterator implements Iterable<Object>, Iterator<Object> {
        private Object standby;
        private boolean rightSided;

        public TupleIterator(Tuple<?, ?> packedTuple, boolean rightSided) {
            standby = rightSided ? packedTuple.item2() : packedTuple.item1();
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
