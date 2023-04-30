package org.example.core.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.core.common.RpcInvocation;
import org.example.core.common.RpcProtocol;
import org.example.core.common.cache.CommonServerCache;

import java.lang.reflect.Method;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //数据到底这个位置的时候，已经是按照 RpcProtocol 展现的
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        String jsonString = new String(rpcProtocol.getContent(), 0, rpcProtocol.getContentLength());
        RpcInvocation data = JSON.parseObject(jsonString, RpcInvocation.class);

        Object aimObject = CommonServerCache.PROVIDER_CLASS_MAP.get(data.getTargetServiceName());
        Method[] methods = aimObject.getClass().getDeclaredMethods();
        Object result = null;
        for(Method method : methods) {
            if(method.getName().equals(data.getTargetMethod())) {
                if(method.getReturnType().equals(Void.TYPE)) {
                    method.invoke(aimObject, data.getArgs());
                } else {
                    result = method.invoke(aimObject, data.getArgs());
                }
            }
            break;
        }

        data.setResponse(result);
        RpcProtocol respProtocol = new RpcProtocol(JSON.toJSONString(data).getBytes());
        ctx.writeAndFlush(respProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if(channel.isActive()) {
            ctx.close();
        }
    }
}
