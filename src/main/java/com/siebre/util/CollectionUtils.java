package com.siebre.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * List集合工具类
 */
public class CollectionUtils {
	
    public static List toList(Object obj) {
        if (obj instanceof List)
            return (List) obj;
        else if (obj instanceof Object[])
            return Arrays.asList((Object[]) obj);
        else if (obj instanceof Collection)
            return new ArrayList((Collection) obj);
        else if (obj instanceof Map)
            return new ArrayList(((Map) obj).values());
        else if (obj == null)
            return Collections.EMPTY_LIST;
        else {
            List list = new ArrayList();
            list.add(obj);
            return list;
        }
    }

}
