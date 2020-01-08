# springcloud-kubernetes-example
基于springcloud-kubernetes的微服务实例，包括provide-service，consumer-service，gateway-service。
   1. 项目使用kubernetes作为服务发现，不再依赖额外的注册中心，
   2. 实现简单的本地服务发现，并自定义配置Feign，实现本地服务调用，以及本地直接调用集群服务。
   3. 在项目结构上我也搭建了一个基本的模块化编程框架，可做参考。

# 基本组件
    服务发现：          spring-cloud-kubernetes
    网关：              spring-cloud-gateway   
    rpc：              openFeign（底层使用okHttpClient）             
    
# 项目结构说明
    ---client 对外服务接口模块
    ---common 公共实体模块
    ---server 服务实现模块
    
# 本地开发
    1. 配置环境变量：LOCAL_DISCOVER_DIR，作为本地服务注册目录
    2：设置profile为local（必要）
    3：配置k8s服务hosts：
        例如：127.0.0.1 provide-service consumer-service
    4：启动各项目，即可

本地测试地址:http://localhost:8080/provide-service/api/hello
swagger地址：http://localhost:8080/doc.html
      
