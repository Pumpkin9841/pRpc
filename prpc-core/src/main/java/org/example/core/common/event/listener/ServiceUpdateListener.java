package org.example.core.common.event.listener;

import io.netty.channel.ChannelFuture;
import org.example.core.client.ConnectionHandler;
import org.example.core.common.ChannelFutureWrapper;
import org.example.core.common.cache.CommonClientCache;
import org.example.core.common.event.PRpcListener;
import org.example.core.common.event.PRpcUpdateEvent;
import org.example.core.common.event.data.URLChangeWrapper;
import org.example.core.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public class ServiceUpdateListener implements PRpcListener<PRpcUpdateEvent> {

    private static final Logger log = LoggerFactory.getLogger(ServiceUpdateListener.class);

    @Override
    public void callBack(Object t) {
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(urlChangeWrapper.getServiceName());

        if(CommonUtils.isEmptyList(channelFutureWrappers)) {
            log.error("[ServiceUpdateListener] channelFutureWrappers is empty");
            return;
        } else {
            List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
            Set<String> finalUrl = new HashSet<>();
            List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
            for (ChannelFutureWrapper channelFutureWrapper : finalChannelFutureWrappers) {
                String oldServerAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
                //如果旧的url没有，说明已经被移除了
                if(!matchProviderUrl.contains(oldServerAddress)) {
                    continue;
                } else {
                    finalChannelFutureWrappers.add(channelFutureWrapper);
                    finalUrl.add(oldServerAddress);
                }
            }
            //此时的旧url已经被移除了，开始检查是否有新的url
            ArrayList<ChannelFutureWrapper> newChannelFutureWrapper = new ArrayList<>();
            for (String newProviderUrl : matchProviderUrl) {
                if(!finalUrl.contains(newProviderUrl)) {
                    ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                    String host = newProviderUrl.split(":")[0];
                    Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                    channelFutureWrapper.setPort(port);
                    channelFutureWrapper.setHost(host);
                    ChannelFuture channelFuture = null;
                    try {
                        channelFuture = ConnectionHandler.createChannelFuture(host, port);
                        channelFutureWrapper.setChannelFuture(channelFuture);
                        newChannelFutureWrapper.add(channelFutureWrapper);
                        finalUrl.add(newProviderUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            finalChannelFutureWrappers.addAll(newChannelFutureWrapper);
            //最终更新服务
            CommonClientCache.CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureWrappers);
        }

    }
}
