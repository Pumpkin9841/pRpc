package org.example.core.client;


import org.example.core.proxy.ProxyFactory;

public class RpcReference {

    private ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }
}
