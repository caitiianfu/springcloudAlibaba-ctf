# SpringcloudAlibaba 系列
## **tian-activiti** 工作流引擎
  - 该模块主要实现工作流引擎一个小demo
  - 整合了流程设计器，测试需要数据库中已经存在model记录，可以先调用create接口通过浏览器请求形式创建一个模型，然后浏览器跳转才可以看到效果,只要替换modelId就可以了。
             访问 http:localhost:3350/modeler.html?modelId=25001
## **tian-admin**  管理员服务
  - 该模块主要集成mp+security实现鉴权认证
## **tian-common**  通用模块
  - 该模块主要包含了通用的工具类，包含
  全局异常处理、Druid连接池配置、mp配置、swagger配置、
  数据鉴权
## **tian-gateway**  网关服务
  - 微服务网关，采用的事WebFlux
## **tian-gen**  代码生成
  - 代码生成，目前无用
## **tian-mbg**  代码生成模块
  - mp生成工具类及包含的pojo、mapper、xml，引用了common模块
## **tian-mongo**  MongoDB模块
  - MongoDB的通用写法
## **tian-netty**  通信服务
  - netty通信，目前无用
## **tian-oauth2-jwt**  
  - oauth2-security使用jwt作为token验证，
  jdbc获取验证配置并整合路径拦截
## **tian-oauth2-redis** 
  - oauth2-security使用redis作为token验证，
       jdbc获取验证配置并整合路径拦截
## **tian-rabbitmq**   队列模块
  - 实现队列延迟队列：1、死信  2、定时任务
## **tian-redis**   缓存模块
  - 使用jedis实现redis缓存操作，包括幂等性和分布式锁
## **tian-redisson**  缓存模块
  - redisson实现分布式锁
## **tian-redisTemplate**  缓存模块
  - 使用springboot-data-redis实现缓存操作
## **tian-search**   搜索引擎
  - elasticSearch实现搜索引擎
## **tian-security**   验证模块
  - security进行权限验证过滤，通用依赖
## **tian-sentinel**   熔断、远程调用服务
  - 使用sentinel熔断机制及feign远程调用
## **分布式事务**
- **tian-seata-account**   账户服务
- **tian-seata-order**     订单服务
- **tian-seata-storage**   库存服务