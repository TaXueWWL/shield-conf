# Shield-CONF分布式配置服务
## 简介
> Shield-CONF是一个分布式配置服务平台，提供统一的配置管理服务。

## 特性
0. 提供两种版本的配置方式，单机版直接添加依赖到项目中即可集成配置管理能力；
分布式版本需要依赖zookeeper，基于dubbo实现了集群方式的配置管理能力。
1. 分布式版本分为C/S架构，客户端采用PULL模式定时更新配置，通过简洁API从本地加载配置;
2. 在线管理: 提供配置中心, 通过Web界面在线操作配置数据;
3. 动态更新: 配置更新后, 客户端轮询服务端获取最新配置, 项目中配置数据会实时更新并生效,
不需要重启线上机器;
4. 配置中心HA：配置中心支持集群部署，提供系统可用性；
5. 高性能: 客户端本地通过ConcurrentHashMap对拉取的配置做Local Cache, 提高性能;
6. 客户端掉线不崩溃：客户端API可指定默认配置项，通过get(String key, String defaultValue)
方法，指定返回为NULL时的默认值。保证远程未响应或远程响应NULL时不崩溃;
7. 配置备份: (TODO)配置数据在MySQL和本地文件中存储和备份， 提高配置数据的安全性;
8. 分布式: 支持多业务线接入并统一管理配置信息，支撑分布式业务场景;
9. 项目隔离: 以项目为维度管理配置, 方便隔离不同业务线配置;
10. 配置变更监听功能：(TODO)可开发Listener逻辑，监听配置变更事件，可据此动态刷新JDBC
连接池等高级功能；
11. 空配置处理：主动缓存null或不存在类型配置，避免配置请求穿透到ZK引发雪崩问题；
12. 登录授权：配置管理模块需要在登录后获取授权码，拼接授权码后方能访问，提高了安全性
13. 配置权限控制；(TODO)以项目为维度进行配置权限控制，管理员拥有全部项目权限，普通用户
只有分配才拥有项目下配置的查看和管理权限；
14. 历史版本回滚：(TODO)记录配置变更历史，方便历史配置版本回溯，默认记录10个历史版本；
15. 支持超时二次推送：客户端获取到配置信息后会响应ACK给服务端，服务端记录客户端更新详情
，对超时未响应(>20s)的客户端手动/自动推送（C/S时间服务保持一致）
16. 服务端缓存优化，(TODO,待考虑)服务端轮询数据库更新缓存，保证服务端稳定性
## 背景

        why not properties

常规项目开发过程中, 通常会将配置信息位于在项目resource目录下的properties文件文件中,
配置信息通常包括有: jdbc地址配置、redis地址配置、活动开关、阈值配置、黑白名单……等等。
使用properties维护配置信息将会导致以下几个问题:

1. 需要手动修改properties文件;
2. 需要重新编译打包;
3. 需要重启线上服务器 (项目集群时,更加令人崩溃) ;
4. 配置生效不及时: 因为流程复杂, 新的配置生效需要经历比较长的时间才可以生效;
5. 不同环境上线包不一致: 例如JDBC连接, 不同环境需要差异化配置;


       why shield-CONF


1. 不需要 (手动修改properties文件) : 在配置中心提供的Web界面中, 定位到指定配置项,
输入新的配置的值等待客户端刷新即可;
2. 不需要 (重新编译打包) : 配置更新后, 客户端实时拉取新配置信息至项目中, 不需要编译打包;
3. 不需要 (重启线上服务器) : 配置更新后, 实时推送新配置信息至项目中, 实时生效, 不需要重
启线上机器; (在项目集群部署时, 将会节省大量的时间, 避免了集群机器一个一个的重启, 费时费力)
4. 配置生效 "非常及时" : 点击更新按钮, 新的配置信息将会准实时更新到项目中, 非常及时。
比如一些开关类型的配置, 配置变更后, 将会立刻推送至项目中并生效, 相对常规配置修改繁琐的流程,
及时性可谓天壤之别;
5. 不同环境 "同一个上线包" : 因为差异化的配置托管在配置中心, 因此一个上线包可以复用在生产、
测试等各个运行环境, 提供能效;
6. 基于SDK的加载方式，统一接口，客户端使用无感知，springboot项目直接引用即可


        <dependency>
              <artifactId>shield-config-client</artifactId>
              <groupId>com.hispeed.development</groupId>
              <version>1.0</version>
        </dependency>

## 更新说明
1. 新增单机版客户端配置，spring应用直接引入下方坐标

		<dependency>
            <artifactId>shield-config-client-single</artifactId>
            <groupId>com.hispeed.development</groupId>
            <version>1.3</version>
        </dependency>
2. 新增客户端响应，响应信息存在Redis中，响应信息

        {"appName":"shop-portal-server","clientIp":"192.168.21.1","updateTime":"2018-04-23 00:02:18"}

3. 新增单机版管理功能，开发中，增加配置查询、新增、修改、配置项激活、禁用功能。下阶段计划增加账户体系，保证配置安全

        单机版配置页面url：ip:port/configure.html
        
4. 单机版配置管理页面新增授权码机制。应用启动时会在本地配置表中添加默认的用户账户名密码，默认为admin，12345
登陆后尽快修改为自己的用户名密码。不登录的情况下直接访问配置页面路由**/configure.html**会被强制重定向
到登录页面。输入用户名密码，获取到授权token，访问配置路由页面，拼接授权token，即可进入配置管理页面。

        url：ip:port/configure.html?authcode=xxxxxxxxxxxxxxxxxxxxxxxxx
5. 正在规划单机版本的拆分，与分布式版本作为一个大项目的子项目同步开发。
   
        
   