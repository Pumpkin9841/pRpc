package org.example.core.proxy.jdk;

import org.example.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class JDKProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Class clazz) throws Throwable {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new JDKClientInvocationHander(clazz));
    }
}
