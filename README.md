# spring_clould分布式事务框架-v0.1
## 项目介绍
基于spring cloud 研发的分布式事务架构，用来解决多数据库下数据不一致的问题。
## 项目结构
 ### 架构源码
 dawdler-distributed-trasaction 分布式事务核心架构源码  
 dawdler-distributed-compensator 补偿处理架构源码  

### 使用实例

 dt_demo_order 订单模块(含下单页面)  
 dt_demo_user 用户模块  
 dt_demo-product 产品模块  
 dt_demo_compensator 补偿器模块    
 eureka-server eureka服务器  
 test.sql  需要导入到mysql中的数据库脚本 会生成3个库  
 
## 使用说明
1、通过maven安装dawdler-distributed-trasaction 和 dawdler-distributed-compensator  
 2、准备activemq或artemis，也可以用其他mq  
 3、准备redis (架构中可以扩展 mysql，本地文件等其他存储方式)用于存储发起者的记录用于做极端情况下的补偿  
 4、导入test.sql到mysql  
 5、更改项目中的mysql配置及redis，mq等配置  
 6、启动  eureka-server， dt_demo-product，dt_demo_user ，dt_demo_order ，dt_demo_compensator 。(先启动eureka 其他顺序无要求)  
 7、访问dt_demo_order 的index.html 测试  
 
