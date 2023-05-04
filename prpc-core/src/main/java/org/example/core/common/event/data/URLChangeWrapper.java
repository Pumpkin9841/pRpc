package org.example.core.common.event.data;

import lombok.Data;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
public class URLChangeWrapper {
    private String serviceName;

    private List<String> providerUrl;
}
