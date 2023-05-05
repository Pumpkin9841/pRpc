package org.example.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import org.example.core.common.ChannelFutureWrapper;
import org.example.core.common.cache.CommonClientCache;
import org.example.core.common.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * 当注册中心的节点新增或移除或权重变化的时候，这个类主要负责对内存中的url做变更
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
public class ConnectionHandler {
    /**
     * 核心的连接处理器
     * 专门用于负责和服务端构建连接通信
     */
    private static Bootstrap bootstrap;

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    /**
     * 构建单个连接通道
     * 既要处理连接，还要统一将连接进行内存存储管理
     * @param providerServiceName
     * @param providerIp
     * @throws InterruptedException
     */
    public static void connect(String providerServiceName, String providerIp) throws InterruptedException {
        if(bootstrap == null) {
            throw new RuntimeException("bootstrap can not be null");
        }
        //格式错误
        if (!providerIp.contains(":")) {
            return;
        }
        String[] providerAddress = providerIp.split(":");
        String ip = providerAddress[0];
        Integer port = Integer.valueOf(providerAddress[1]);
        //TODO ChannelFuture里面到底是什么
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
        channelFutureWrapper.setChannelFuture(channelFuture);
        channelFutureWrapper.setHost(ip);
        channelFutureWrapper.setPort(port);
        CommonClientCache.SERVER_ADDRESS.add(providerIp);
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerServiceName);
        if(CommonUtils.isEmptyList(channelFutureWrappers)) {
            channelFutureWrappers = new ArrayList<>();
        }
        channelFutureWrappers.add(channelFutureWrapper);
        CommonClientCache.CONNECT_MAP.put(providerServiceName, channelFutureWrappers);
    }

    /**
     * 构建ChannelFuture
     * @param ip
     * @param port
     * @return
     * @throws InterruptedException
     */
    public static ChannelFuture createChannelFuture(String ip, Integer port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        return channelFuture;
    }

    /**
     * 获取ChannelFuture
     * 默认走随机策略
     * @param providerServiceName
     * @return
     */
    public static ChannelFuture getChannelFuture(String providerServiceName) {
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerServiceName);
        if(CommonUtils.isNotEmptyList(channelFutureWrappers)) {
            throw new RuntimeException("no provider exits for " + providerServiceName);
        }
        ChannelFuture channelFuture = channelFutureWrappers.get(new Random().nextInt(channelFutureWrappers.size())).getChannelFuture();
        return channelFuture;
    }
}
