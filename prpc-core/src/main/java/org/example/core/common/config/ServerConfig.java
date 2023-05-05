package org.example.core.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhoufan
 * @create 2023/4/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerConfig {

    /**
     * 服务器监听端口
     */
    private Integer port;

    private String registerAddr;

    private String applicationName;

}
