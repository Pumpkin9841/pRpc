package org.example.core.common;

import lombok.Data;

/**
 * @Author zhoufan
 * @create 2023/4/28
 */
@Data
public class RpcInvocation {
    /**
     * 请求的目标方法
     */
    private String targetMethod;

    /**
     * 请求的目标服务名称
     */
    private String targetServiceName;

    /**
     * 请求的参数
     */
    private Object[] args;

    /**
     * 用于记录发出的请求，待数据返回的时候通过uuid进行匹配请求的线程，并且返回给调用线程
     */
    private String uuid;

    /**
     * 接口响应的数据塞入这个字段中，（如果是异步调用或者void类型，这里为空）
     */
    private Object response;

}
