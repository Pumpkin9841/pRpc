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
import org.example.core.common.config.PropertiesBootstrap;
import org.example.core.common.config.ServerConfig;
import org.example.core.common.RpcDecoder;
import org.example.core.common.utils.CommonUtils;
import org.example.core.registry.RegistryService;
import org.example.core.registry.URL;

import static org.example.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;
import static org.example.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

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

    private RegistryService registryService;

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
        this.batchExportUrl();
        serverBootstrap.bind(serverConfig.getPort()).sync();
    }

    private void batchExportUrl() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(URL url : PROVIDER_URL_SET) {
                    registryService.register(url);
                }
            }
        });
        task.start();
    }

    public void initServerConfig() {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.serverConfig = serverConfig;
    }

    public void exportService(Object serviceBean){
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if(classes.length == 0) {
            throw new RuntimeException("注册服务失败，服务必须实现接口");
        }
        if(classes.length > 1) {
            throw new RuntimeException("注册服务失败，服务只能实现一个接口");
        }
        String serviceName = classes[0].getName();
        PROVIDER_CLASS_MAP.put(serviceName, serviceBean);
        URL url = new URL();
        url.setServiceName(classes[0].getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getPort()));
        PROVIDER_URL_SET.add(url);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        server.exportService(new DataServiceImpl());
        server.startApplication();

    }

}
