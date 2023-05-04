package org.example.core.common.cache;

import org.example.core.registry.URL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author zhoufan
 * @create 2023/4/25
 */
public class CommonServerCache {
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new HashMap<>();

    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
}
