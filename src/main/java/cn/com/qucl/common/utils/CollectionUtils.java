package cn.com.qucl.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class CollectionUtils extends org.springframework.util.CollectionUtils {
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static void addAll(Collection<Object> collection, Iterator<Object> iterator) {
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }

    public static void addAll(Collection<Object> collection, Object[] objects) {
        Collections.addAll(collection, objects);
    }
}
