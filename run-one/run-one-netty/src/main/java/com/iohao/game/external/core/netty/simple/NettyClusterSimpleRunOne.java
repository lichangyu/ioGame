/*
 * ioGame
 * Copyright (C) 2021 - 2023  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.game.external.core.netty.simple;

import com.iohao.game.action.skeleton.core.ActionCommandRegionGlobalCheckKit;
import com.iohao.game.action.skeleton.toy.IoGameBanner;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.bolt.broker.server.BrokerServer;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.core.ExternalServer;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * （集群相关的）集群简单的启动器： 对外服、游戏网关（3个节点）、逻辑服
 * 谐音:拳皇98中的 round one ready go!
 * <pre>
 *     注意：
 *          这个工具只适合单机的开发或本地一体化的开发, 对于分步式不适合。
 *
 * </pre>
 * 集群介绍
 * <pre>
 *     格式： ip:port
 *
 *     -- 生产环境的建议 --
 *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
 *     一个 broker 就是一个节点
 *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
 *     192.168.1.10:30056
 *     192.168.1.11:30056
 *     192.168.1.12:30056
 *
 *     -- 为了方便演示 --
 *     这里配置写死是方便在一台机器上启动集群
 *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
 *     所以这里的配置是：
 *     127.0.0.1:30056
 *     127.0.0.1:30057
 *     127.0.0.1:30058
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-04-28
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class NettyClusterSimpleRunOne {
    static final Logger log = IoGameLoggerFactory.getLoggerCommon();
    final InternalRunOne runOne = new InternalRunOne();

    /** true 在本地启动 broker （游戏网关）集群 */
    boolean runBrokerServerCluster = true;

    /**
     * 简单的快速启动
     * <pre>
     *     快速启动:
     *          对外服
     *          游戏网关集群
     *          逻辑服
     *
     *      注意1：
     *          方法会启动 3 个游戏网关来演示集群，端口分别是：30056、30057、30058
     *
     *      注意2：
     *          因为 broker （游戏网关） 集群是无中心节点的，所以逻辑服可以选择与任意一台网关建立连接，
     *          逻辑服内部会自动的与集群其他节点建立连接
     * </pre>
     */
    public void startup() {
        banner();

        // 启动网关集群（3个节点）
        if (this.runBrokerServerCluster) {
            this.clusterBrokerServer();
        }

        try {
            // 暂停 0.5 秒，让本地网关集群先启动完成
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        // 启动逻辑服、对外服
        this.runOne.startupLogic();

        // 全局重复路由检测工具
        ActionCommandRegionGlobalCheckKit.checkGlobalExistSubCmd();
    }

    /**
     * set 游戏逻辑服列表
     *
     * @param logicServerList 游戏逻辑服列表
     * @return this
     */
    public NettyClusterSimpleRunOne setLogicServerList(List<AbstractBrokerClientStartup> logicServerList) {
        this.runOne.setLogicServerList(logicServerList);
        return this;
    }

    /**
     * 添加游戏对外服
     *
     * @param externalServer 游戏对外服
     * @return this
     */
    public NettyClusterSimpleRunOne setExternalServer(ExternalServer externalServer) {
        this.runOne.setExternalServer(externalServer);
        return this;
    }

    /**
     * set 游戏对外服列表
     *
     * @param externalServerList 游戏对外服列表
     * @return this
     */
    public NettyClusterSimpleRunOne setExternalServerList(List<ExternalServer> externalServerList) {
        this.runOne.setExternalServerList(externalServerList);
        return this;
    }

    public NettyClusterSimpleRunOne setOpenWithNo(boolean openWithNo) {
        this.runOne.setOpenWithNo(openWithNo);
        return this;
    }

    /**
     * 禁用 broker （游戏网关）集群
     * <pre>
     *     本地不启动游戏网关集群
     *     如果公司团队开发中，可以把 broker （游戏网关）集群，部署在其他机器上
     *     而本机启动的逻辑服连接到这些游戏网关集群上，这样就可以共用游戏网关集群，不用每次在本机启动集群
     *     这样调试起来也方便
     * </pre>
     *
     * @return this
     */
    public NettyClusterSimpleRunOne disableBrokerServerCluster() {
        this.runBrokerServerCluster = false;
        return this;
    }

    private void clusterBrokerServer() {

        /*
         * 种子节点地址
         * <pre>
         *     格式： ip:port
         *
         *     -- 生产环境的建议 --
         *     注意，在生产上建议一台物理机配置一个 broker （游戏网关）
         *     一个 broker 就是一个节点
         *     比如配置三台机器，端口可以使用同样的端口，假设三台机器的 ip 分别是:
         *     192.168.1.10:30056
         *     192.168.1.11:30056
         *     192.168.1.12:30056
         *
         *     -- 为了方便演示 --
         *     这里配置写死是方便在一台机器上启动集群
         *     但是同一台机器启动多个 broker 来实现集群就要使用不同的端口，因为《端口被占用，不能相同》
         *     所以这里的配置是：
         *     127.0.0.1:30056
         *     127.0.0.1:30057
         *     127.0.0.1:30058
         * </pre>
         */
        List<String> seedAddress = List.of(
                "127.0.0.1:30056",
                "127.0.0.1:30057",
                "127.0.0.1:30058"
        );

        // Gossip listen port 监听端口
        int gossipListenPort = IoGameGlobalConfig.gossipListenPort;
        // broker 端口（游戏网关端口）
        int port = IoGameGlobalConfig.brokerPort;
        // ---- 第1台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);

        // Gossip listen port 监听端口
        gossipListenPort = 30057;
        // broker 端口（游戏网关端口）
        port = 10201;
        //  ---- 第2台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);

        // Gossip listen port 监听端口
        gossipListenPort = 30058;
        // broker 端口（游戏网关端口）
        port = 10202;
        //  ---- 第3台 broker ----
        this.createBrokerServer(seedAddress, gossipListenPort, port);
    }

    private void createBrokerServer(List<String> seedAddress, int gossipListenPort, int port) {
        BrokerServer brokerServer = NettyClusterSimpleHelper.createBrokerServer(seedAddress, gossipListenPort, port);

        // 启动游戏网关
        brokerServer.setWithNo(this.runOne.getWithNo());
        this.runOne.execute(brokerServer::startup);
    }

    private void banner() {

        int num = 0;

        if (Objects.nonNull(this.runOne.logicServerList)) {
            num += this.runOne.logicServerList.size();
        }

        if (Objects.nonNull(this.runOne.externalServerList)) {
            num += this.runOne.externalServerList.size();
        }

        if (this.runBrokerServerCluster) {
            num += 3;
        }

        IoGameBanner.me().initCountDownLatch(num);
        IoGameBanner.render();
    }
}
