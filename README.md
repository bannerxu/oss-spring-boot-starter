# 对接各种云存储，简单配置，完成对接

已经支持的云存储

- 阿里云OSS
- 七牛云
- 腾讯云COS

![a405f3d5-892a-47fa-8927-2cd8e6241289-image](https://image.xuguoliang.top/2022/03/01/a405f3d5-892a-47fa-8927-2cd8e6241289-image-iVX0g3.jpg)

## 为什么要选择前端直传

传统方式相比直传OSS，相对来说有三个缺点：

- 上传慢：用户数据需先上传到应用服务器，之后再上传到OSS。网络传输时间比直传到OSS多一倍。如果用户数据不通过应用服务器中转，而是直传到OSS，速度将大大提升。而且OSS采用BGP带宽，能保证各地各运营商之间的传输速度。
- 扩展性差：如果后续用户多了，应用服务器会成为瓶颈。
- 费用高：需要准备多台应用服务器。由于OSS上传流量是免费的，如果数据直传到OSS，不通过应用服务器，那么将能省下几台应用服务器。

## 使用方式

Maven导入jar

```xml

<dependency>
    <groupId>io.github.bannerxu</groupId>
    <artifactId>oss-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### [阿里云OSS](doc/ali.md)

### [七牛云](doc/qiniu.md)

### [腾讯云COS](doc/qcloud.md)

