package org.example.core.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import org.example.core.common.RpcDecoder;
import org.example.core.common.RpcEncoder;
import org.example.core.common.RpcInvocation;
import org.example.core.common.RpcProtocol;
import org.example.core.common.cache.CommonClientCache;
import org.example.core.common.config.ClientConfig;
import org.example.core.proxy.jdk.JDKProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private NioEventLoopGroup clientGroup;

    private ClientConfig clientConfig;

    public RpcReference startClientApplication() throws InterruptedException {
        clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new ClientHander());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        logger.info("=======服务启动=========");
        this.startClient(channelFuture);
        RpcReference rpcReference = new RpcReference(new JDKProxyFactory());
        return rpcReference;
    }

    public void startClient(ChannelFuture channelFuture) {
        Thread thread = new Thread(new AsyncSendJob(channelFuture));
        thread.start();
    }

    class AsyncSendJob implements Runnable{

        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            try{
                //阻塞模式
                RpcInvocation data = CommonClientCache.SEND_QUEUE.take();
                String jsonData = JSON.toJSONString(data);
                RpcProtocol protocol = new RpcProtocol(jsonData.getBytes());
                channelFuture.channel().writeAndFlush(protocol);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
