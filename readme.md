
> 具体实现细节链接地址：

1. [项目简介](https://blog.takake.co/posts/4398/)
2. [密钥交换和用户认证](https://blog.takake.co/posts/57370/)
3. [敏感数据加密传输](https://blog.takake.co/posts/3456/)
4. [传输签名校验](https://blog.takake.co/posts/41661/)
5. [数据库用户密码加密](https://blog.takake.co/posts/9622/)
6. [配置文件加密](https://blog.takake.co/posts/50790/)

> 项目仓库链接地址

- [前端VUE](https://github.com/takakie/VUE-SMEncrypt-DEMO)
- [后端SpringBoot](https://github.com/takakie/SMEncrypt-SpringBoot-DEMO)

> 项目演示链接地址

- [LOGIN-DEMO](http://119.23.186.200/#/Login)


# 1.项目简介

## 1.1.密钥交换和用户认证

1. 前端请求后端获取**SM2公钥**
2. 前端随机生成**SM4密钥**(key+iv)
3. 用户认证（用户输入用户名和**密码**）
4. 前端分别使用**SM2公钥**加密(**用户密码、key、iv**)
5. 后端使用**SM2私钥**解密获得(**用户密码、key、iv**)
6. 用户认证成功将**key、iv**存入**loginUser**然后存入**redis**
7. 生成JWT，请勿将**key、iv**存入**JWT** (**<u>JWT只能保证数据完整性</u>**)
8. 返回前端存入sessionStorage **(完成用户认证和密钥交互)**
9. 使用sm4密钥加密业务数据进行传输

![image-20231229170222712](https://hongkong-img.oss-cn-hongkong.aliyuncs.com/markdown-img/image-20231229170222712.png?x-oss-process=style/img-to-webp)

## 1.2.敏感数据加密传输

1. 前端从**sessionStorage**中获取**sm4密钥**和**iv**
2. 加密敏感数据发送至后端
3. 后端使用从JWT中获取到**redisKey**获取该登陆会话的**loginUser**，并从**loginUser**中获取到登陆时存入的sm4密钥和iv
4. 使用该sm4密钥解密前端传来的加密数据
5. 使用该密钥加密需要响应的敏感数据
6. 前端获取到响应中sm4加密后的密文，使用sessionStorage中的密钥和iv进行解密

## 1.3.传输签名校验

> 前端密钥交换成功的前提是需要保证传输过程中数据不被篡改，因此需要使用到签名校验

1. 通过**axios**请求拦截器，拦截到需要签名的请求
1. 判断**get**还是**post**请求，以及请求体类型
1. 拼接参数**SM3(SALT$+URI+拼接的请求参数+$Timpstamp)**
1. 将**Sign**和**Timestamp**放入请求头
1. 签名过滤器判断是否存在**签名**与**时间戳**
1. 拼接前端传来的**请求参数**
1. 签名生成**enSign**与前端**Sign**做对比，**相同**签名校验通过，**不同**签名校验失败

![image-20231229192445103](https://hongkong-img.oss-cn-hongkong.aliyuncs.com/markdown-img/image-20231229192445103.png?x-oss-process=style/img-to-webp)

## 1.4.数据库用户密码加密

> 数据库用户密码也需要使用国密算法进行加密存储，数据库加密通常使用单向hash加密算法

1. 添加自定义密码加密盐

2. 对用户密码进行三次加盐的sm3加密

   

## 1.5.配置文件加密

1. 使用jasypt对application配置密钥及密码数据进行加密
2. 实现PBEStringEncryptor接口使用自定义SM4加密算法进行加密
3. 将配置文件解密密钥存入环境变量



# 2.项目关于

## 2.1.项目启动

- 安装依赖 `npm install`
- 项目启动 `npm run serve`
- 启动redis，（默认为空密码，请在生产环境中设置密码）
- 启动mysql，（默认为若口令密码123456，请在生产环境中设置强密码）
- 启动后端服务
- 默认登陆用户名密码 admin，admin123

## 2.2.需要一些简单的基础知识

- [编码转换](https://blog.takake.co/posts/28315/)
- [国密算法](https://blog.takake.co/posts/49250/)

## 2.3.生产环境中的一些注意事项和提示

- 请勿在代码中硬编码密钥和密码
- 请勿在配置文件中明文存储密钥和密码
- 该项目均使用国密算法实现
- 该项目编码者本身并非专业开发，项目可能存在诸多BUG，欢迎提issue，或者联系本人。



> 注：本项目为个人项目，仅提供演示作用，且不为任何企业或组织的编码规范
