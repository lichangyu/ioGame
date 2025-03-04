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
package com.iohao.game.action.skeleton.toy;

import com.iohao.game.common.kit.RandomKit;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * ioGame 启动新闻
 * <pre>
 *     名字来源 Michael Jackson 的歌曲 BreakingNews
 * </pre>
 *
 * @author 渔民小镇
 * @date 2023-06-10
 */
@UtilityClass
class BreakingNews {

    public String randomNews() {
        News news = random();
        return String.format("%s - %s", news.title, news.url);
    }

    private News random() {
        List<News> list = new ArrayList<>();

        list.add(new News("支持者名单", "https://www.yuque.com/iohao/game/backers"));
        list.add(new News("模拟客户端请求（真实网络环境）", "https://www.yuque.com/iohao/game/tc83ud"));

        // 授权相关
        list.add(new News("项目成本分析", "https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv#aSk5x"));
        list.add(new News("为什么采用授权许可申请？", "https://www.yuque.com/iohao/game/gd5l3b0y0h027kcv"));
        list.add(new News("授权成员的更多权益", "https://www.yuque.com/iohao/game/ruqkacwigfnlk129"));

        list.add(new News("ioGame 诞生、发展", "https://www.yuque.com/iohao/game/mun9gbwzfph3y5vn"));
        list.add(new News("框架版本更新日志", "https://www.yuque.com/iohao/game/ab15oe"));
        list.add(new News("需要给到游戏前端的", "https://www.yuque.com/iohao/game/zfg3ci"));

        // 整体、架构相关
        list.add(new News("架构简介", "https://www.yuque.com/iohao/game/dqf0he"));
        list.add(new News("ioGame 架构多样性", "https://www.yuque.com/iohao/game/zqgdv3g9if8w37vr"));
        list.add(new News("与传统架构的对比", "https://www.yuque.com/iohao/game/cklv8p"));

        list.add(new News("ioGame 请求的处理流程", "https://www.yuque.com/iohao/game/ibwgawdy4al6o389"));
        list.add(new News("ioGame 线程相关", "https://www.yuque.com/iohao/game/eixd6x"));
        list.add(new News("多服单进程、多服多进程的启动方式", "https://www.yuque.com/iohao/game/qni8eqlzsxk7gabm"));
        list.add(new News("代码组织与约定", "https://www.yuque.com/iohao/game/keyrxn"));

        // 游戏逻辑服
        list.add(new News("动态绑定游戏逻辑服", "https://www.yuque.com/iohao/game/idl1wm"));
        list.add(new News("解决协议碎片", "https://www.yuque.com/iohao/game/ieimzn"));
        list.add(new News("元信息-附加信息", "https://www.yuque.com/iohao/game/sw1y8u"));

        // 游戏对外服
        list.add(new News("新游戏对外服设计", "https://www.yuque.com/iohao/game/wotnhl"));
        list.add(new News("新游戏对外服使用", "https://www.yuque.com/iohao/game/ea6geg"));
        list.add(new News("心跳设置与心跳钩子", "https://www.yuque.com/iohao/game/uueq3i"));
        list.add(new News("用户上线、下线钩子", "https://www.yuque.com/iohao/game/hv5qqh"));
        list.add(new News("路由访问权限控制", "https://www.yuque.com/iohao/game/nap5y8p5fevhv99y"));
        list.add(new News("游戏对外服缓存", "https://www.yuque.com/iohao/game/khg23pvbh59a7spm"));

        // 通讯方式
        list.add(new News("脉冲通讯方式", "https://www.yuque.com/iohao/game/zgaldoxz6zgg0tgn"));
        list.add(new News("游戏逻辑服之间的交互", "https://www.yuque.com/iohao/game/anguu6"));
        list.add(new News("请求同类型多个逻辑服通信结果", "https://www.yuque.com/iohao/game/rf9rb9"));
        list.add(new News("获取游戏对外服的数据与扩展", "https://www.yuque.com/iohao/game/ivxsw5"));

        // 小部件
        list.add(new News("领域事件可解决多人同一业务的并发问题", "https://www.yuque.com/iohao/game/gmfy1k"));
        list.add(new News("任务延时器", "https://www.yuque.com/iohao/game/niflk0"));
        return RandomKit.randomEle(list);
    }

    record News(String title, String url) {

    }
}
