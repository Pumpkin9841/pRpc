package org.example.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Data;

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

    public static ChannelFuture createChannelFuture(String ip, Integer port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        return channelFuture;
    }
}
