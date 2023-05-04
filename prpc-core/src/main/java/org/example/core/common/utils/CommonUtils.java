package org.example.core.common.utils;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public class CommonUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyList(List list) {
        if(list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isNotEmptyList(List list) {
        return !isEmptyList(list);
    }
}
