package org.example.core.common.utils;

import java.awt.geom.IllegalPathStateException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public class CommonUtils {

    /**
     * 获取本机ipv4地址
     * @return
     */
    public static String getIpAddress() {
        try {
            //获取所有的网络接口（网卡）
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            //初始化1个 InetAddress 对象，用于存储找到的IPv4地址
            InetAddress ip = null;
            //遍历所有的网卡
            while(allNetInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                //判断该网络接口是否为 回环地址（loopback，即本地回环地址 127.0.0.1）、虚拟接口或者处于未激活状态。如果满足这些条件之一，则跳过
                if(networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    //遍历该网卡下的所有ip地址
                    while(addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        //如果找到的ip地址不为null并且为ipv4地址，则返回改地址的字符串形式
                        if(ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ip地址获取失败" + e.toString());
        }
        //没有找到合适的ipv4地址，则返回空串
        return "";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyList(List list) {
        if(list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isNotEmptyList(List list) {
        return !isEmptyList(list);
    }
}
