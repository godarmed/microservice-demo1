package com.godarmed.core.starters.global.utils;

import java.util.Collection;
import java.util.Map;

public class CheckUtils {
    public CheckUtils() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String && obj.equals("")) {
            return true;
        } else if (obj instanceof Map && ((Map)obj).isEmpty()) {
            return true;
        } else if (obj instanceof Collection && ((Collection)obj).size() <= 0) {
            return true;
        } else {
            return obj instanceof IEmptyJudge ? ((IEmptyJudge)obj).checkIsEmpty() : false;
        }
    }
}
