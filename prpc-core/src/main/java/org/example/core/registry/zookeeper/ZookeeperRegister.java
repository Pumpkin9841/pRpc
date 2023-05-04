package org.example.core.registry.zookeeper;

import lombok.Data;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.example.core.common.event.PRpcListenerLoader;
import org.example.core.common.event.PRpcUpdateEvent;
import org.example.core.common.event.data.URLChangeWrapper;
import org.example.core.registry.RegistryService;
import org.example.core.registry.URL;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
public class ZookeeperRegister extends AbstractRegister implements RegistryService{

    private AbstractZookeeperClient zkClient;

    private String ROOT = "/prpc";

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer" + url.getApplicationName() + ":" + url.getParameters().get("host") + ":";
    }

    @Override
    public void register(URL url) {
        if(!this.zkClient.exitsNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlStr(url);
        if(!zkClient.exitsNode(getProviderPath(url))) {
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(url));
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void subscribe(URL url) {
        if(!this.zkClient.exitsNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if(!zkClient.exitsNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        super.subscribe(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }



    @Override
    public void doAfterSubscribe(URL url) {
        //监听是否有新的服务注册
        String newServerNodePath = ROOT + "/" + url.getServiceName() + "/provider";
        watchChildNodeData(newServerNodePath);
    }

    private void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
                String path = watchedEvent.getPath();
                List<String> childrenDataList = zkClient.getChildrenData(path);
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                PRpcUpdateEvent pRpcUpdateEvent = new PRpcUpdateEvent(urlChangeWrapper);
                PRpcListenerLoader.sendEvent(pRpcUpdateEvent);
                watchChildNodeData(path);
            }
        });
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nowDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        return nowDataList;
    }
}
