package org.example.core.registry.zookeeper;

import lombok.Data;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @Author zhoufan
 * @create 2023/5/4
 */
@Data
public abstract class AbstractZookeeperClient {
    private String zkAddress;

    private int baseSleepTimes;

    private int maxRetryTimes;


    public AbstractZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        //默认3000s
        this.baseSleepTimes = 1000;
        this.maxRetryTimes = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        if(baseSleepTimes == null) {
            this.baseSleepTimes = 1000;
        } else {
            this.baseSleepTimes = baseSleepTimes;
        }

        if(maxRetryTimes == null) {
            this.maxRetryTimes = 3;
        } else {
            this.maxRetryTimes = maxRetryTimes;
        }
    }

    public abstract void updateNodeData(String address, String data);

    public abstract Object getClinet();

    /**
     *拉取节点的数据
     * @param path
     * @return
     */
    public abstract String getNodeData(String path);


    /**
     * 获取指定目录下的节点数据
     * @param path
     * @return
     */
    public abstract List<String> getChildrenData(String path);

    /**
     * 创建持久化类型节点数据信息
     * 建的节点是持久化的（即使ZooKeeper集群重启，节点仍然存在）
     * @param address
     * @param data
     */
    public abstract void createPersistentData(String address, String data);

    /**
     * 创建有序、持久化类型节点信息
     * 顺序：顺序节点在创建时会自动在节点名称后添加一个递增的数字序列。这个序列是基于父节点下所有子节点的创建顺序分配的，确保每个新创建的子节点都具有唯一的名称。顺序节点可用于实现分布式锁、选举等分布式系统的一些功能。
     * 当我们说创建的节点是持久化的顺序节点时，我们指的是这个节点在ZooKeeper中长期存在，且其名称具有一个根据创建顺序自动生成的递增数字序列。
     * @param address
     * @param data
     */
    public abstract void createPersistentWithSeqData(String address, String data);

    /**
     * 创建有序且临时类型节点数据信息
     * @param address
     * @param data
     */
    public abstract void createTemporarySeqData(String address, String data);

    /**
     * 创建临时节点数据类型信息
     * @param address
     * @param data
     */
    public abstract void createTemporaryData(String address, String data);

    /**
     * 设置某个节点的数值
     * @param address
     * @param data
     */
    public abstract void setTemporaryData(String address, String data);

    /**
     * 断开zk的客户端连接
     */
    public abstract void destroy();

    /**
     * 展示节点下边的数据
     * @param address
     * @return
     */
    public abstract List<String> listNode(String address);

    /**
     * 删除节点下边的数据
     * @param address
     * @return
     */
    public abstract boolean deleteNode(String address);

    /**
     * 判断节点是否存在
     * @param address
     * @return
     */
    public abstract boolean exitsNode(String address);

    /**
     * 监听path路径下某个节点的数据变化
     * @param path
     * @param watcher
     */
    public abstract void watchNodeData(String path, Watcher watcher);

    /**
     * 监听子节点下的数据变化
     * @param path
     * @param watcher
     */
    public abstract void watchChildNodeData(String path, Watcher watcher);
}
