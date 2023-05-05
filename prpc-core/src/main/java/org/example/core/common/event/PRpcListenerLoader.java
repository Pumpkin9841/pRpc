package org.example.core.common.event;

import org.example.core.common.event.listener.ServiceUpdateListener;
import org.example.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
public class PRpcListenerLoader {
    private static List<PRpcListener> pRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool =  Executors.newFixedThreadPool(2);

    public static void registerListener(PRpcListener pRpcListener) {
        pRpcListenerList.add(pRpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    /**
     * 获取接口上的泛型T
     * @param o
     * @return
     */
    public static Class<?> getIntegerfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if(type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendEvent(PRpcEvent pRpcEvent) {
        if(CommonUtils.isEmptyList(pRpcListenerList)) {
            return;
        }
        for (PRpcListener pRpcListener : pRpcListenerList) {
            Class<?> type = getIntegerfaceT(pRpcListener);
            if(type.equals(pRpcEvent.getClass())) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pRpcListener.callBack(pRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
