package org.example.core.registry.zookeeper;

import org.example.core.registry.RegistryService;
import org.example.core.registry.URL;

import java.util.List;

import static org.example.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static org.example.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public abstract class AbstractRegister implements RegistryService {

    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    /**
     *留给子类扩展
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);
}
