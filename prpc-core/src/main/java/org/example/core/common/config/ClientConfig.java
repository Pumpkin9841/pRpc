package org.example.core.common.config;

import lombok.Data;

@Data
public class ClientConfig {

    /**
     * 目标服务器端口
     */
    private Integer port;

    /**
     * 目标服务器ip地址
     */
    private String registerAddr;

    private String applicationName;

    private String proxyType;
}
