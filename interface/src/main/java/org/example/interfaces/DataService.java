package org.example.interfaces;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/4/25
 */
public interface DataService {
    /**
     * 发送数据
     *
     * @param body
     */
    String sendData(String body);

    /**
     * 获取数据
     *
     * @return
     */
    List<String> getList();
}
