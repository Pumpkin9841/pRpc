package org.example.core.proxy;

public interface ProxyFactory {
    <T> T getProxy(final Class clazz) throws Throwable;
}
