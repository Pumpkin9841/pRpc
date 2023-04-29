package org.example.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.common.RpcEncoder;
import org.example.core.common.config.ServerConfig;
import org.example.core.common.RpcDecoder;

import static org.example.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Author zhoufan
 * @create 2023/4/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    private ServerConfig serverConfig;

    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //禁止Nagle算法
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                //启用TCP保活机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new ServerHandler());
                    }
                });
        serverBootstrap.bind(serverConfig.getPort()).sync();
    }

    public void registyService(Object serviceBean){
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if(classes.length == 0) {
            throw new RuntimeException("注册服务失败，服务必须实现接口");
        }
        if(classes.length > 1) {
            throw new RuntimeException("注册服务失败，服务只能实现一个接口");
        }
        String serviceName = classes[0].getName();
        PROVIDER_CLASS_MAP.put(serviceName, serviceBean);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        ServerConfig config = new ServerConfig();
        config.setPort(9090);
        server.setServerConfig(config);
        server.registyService(new DataServiceImpl());
        server.startApplication();

    }

}
