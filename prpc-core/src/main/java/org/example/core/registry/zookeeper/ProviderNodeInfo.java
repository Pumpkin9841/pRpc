package org.example.core.registry.zookeeper;

import lombok.Data;
import lombok.ToString;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
@ToString
public class ProviderNodeInfo {

    private String serviceName;

    private String address;
}
