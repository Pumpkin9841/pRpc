package org.example.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.core.common.RpcProtocol;
import org.example.core.common.constants.RpcConstants;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/4/28
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议开头的标准长度
     * 魔数(short 2) + 消息长度(int 4) = 6
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= BASE_LENGTH) {
            //防止收到一些体积过大的包，目前限制在1000大小，后期版本这里是可配置模式
            if(byteBuf.readableBytes() > 1000) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            //用于记录读索引位置
            int beginReader;
            while(true) {
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                if(byteBuf.readShort() == RpcConstants.MAGIC_NUMBER) {
                    break;
                } else {
                    //不是魔数开头，说明是非法的客户端发来的数据包
                    channelHandlerContext.close();
                    return;
                }
            }

            int length = byteBuf.readInt();
            //说明剩余的数据包不是完整的，这里需要重置下读索引
            if(byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }

            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol protocol = new RpcProtocol(data);
            list.add(protocol);
        }
    }
}
