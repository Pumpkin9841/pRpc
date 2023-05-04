package org.example.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;

    private String host;

    private String port;
}
