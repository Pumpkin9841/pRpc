package org.example.core.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.core.common.constants.RpcConstants;

import java.io.Serializable;


/**
 * @Author zhoufan
 * @create 2023/4/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcProtocol implements Serializable {

    /**
     * 魔法数
     * 主要是在服务通讯的时候做一个安全检测，确认当前请求的协议是否合法
     */
    private short magicNumber = RpcConstants.MAGIC_NUMBER;

    /**
     * 协议传输的核心数据的长度
     * 这里将长度单独拎出来设置有个好处，当服务端的接收能力有限，可以对该字段进行赋值。
     * 当读取到的网络数据包中的contentLength字段已经超过预期值的话，就不会去读取content字段内容
     */
    private int contentLength;

    /**
     * 对应RpcInvocation类的字节数组
     * 核心的传输数据，主要是请求的服务名称，请求服务的方法名称，请求参数内容
     */
    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }
}
