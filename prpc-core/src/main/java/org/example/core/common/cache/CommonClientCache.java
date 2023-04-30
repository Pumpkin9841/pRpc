package org.example.core.common.cache;

import org.example.core.common.RpcInvocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CommonClientCache {
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);

    /**
     * key -> uuid
     * Object -> 对应的数据
     * 使用 uuid 来对应每次请求的返回
     */
    public static Map<String, Object> RESP_MAP = new HashMap<>();
}
