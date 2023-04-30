package org.example.core.proxy.jdk;

import org.example.core.common.RpcInvocation;
import org.example.core.common.cache.CommonClientCache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class JDKClientInvocationHander implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private Class<?> clazz;

    public JDKClientInvocationHander(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(clazz.getName());

        //这里注入uuid，对每一次请求做单独分区
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        CommonClientCache.RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        //这里将请求的参数放到 发送队列 中
        CommonClientCache.SEND_QUEUE.add(rpcInvocation);

        long beginTime = System.currentTimeMillis();

        while(System.currentTimeMillis() - beginTime < 3 * 1000) {
            Object obj = CommonClientCache.RESP_MAP.get(rpcInvocation.getUuid());
            if(obj instanceof RpcInvocation) {
                return ((RpcInvocation)obj).getResponse();
            }
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
