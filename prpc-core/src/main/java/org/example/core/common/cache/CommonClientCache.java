package org.example.core.common.cache;

import org.example.core.common.ChannelFutureWrapper;
import org.example.core.common.RpcInvocation;
import org.example.core.common.config.ClientConfig;
import org.example.core.registry.URL;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommonClientCache {
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);

    /**
     * key -> uuid
     * Object -> 对应的数据
     * 使用 uuid 来对应每次请求的返回
     */
    public static Map<String, Object> RESP_MAP = new HashMap<>();

    public static ClientConfig CLIENT_CONFIG;


    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    /**
     * provider服务名称 -> 该服务有哪些集群URL
     */
    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();

    public static Set<String> SERVER_ADDRESS = new HashSet<>();

    /**
     * 每次进行远程调用的时候都从这里面去选择服务提供者
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();

}
