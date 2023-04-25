package org.example.core.server;

import org.example.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/4/25
 */
public class DataServiceImpl implements DataService {
    @Override
    public String sendData(String body) {
        System.out.println("己收到的参数长度："+body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("pumpkin1");
        arrayList.add("pumpkin2");
        arrayList.add("pumpkin3");
        return arrayList;
    }
}
