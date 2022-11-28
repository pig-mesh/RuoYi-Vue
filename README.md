## PIGX 整合前后端分离项目登录

![](https://minio.pigx.vip/oss/202211/1669615968.jpg)

## 插入客户端

```sql
INSERT INTO `sys_oauth_client_details` VALUES (10, 'ruoyi', NULL, 'ruoyi', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://127.0.0.1:1024/sso', NULL, 0, 0, NULL, 'false', '0', ' ', ' ', NULL, NULL, 1);
```

## RuoYi 后端信息

> 配置 ②  RuoYi 前端地址 默认 1024 端口
> 配置 ③  PIGX 认证中心地址

![](https://minio.pigx.vip/oss/202211/1669630046.png)

## RuoYi 前端配置

- 登录页面配置
![](https://minio.pigx.vip/oss/202211/1669630262.png)

- 退出页面配置
![](https://minio.pigx.vip/oss/202211/1669630290.png)
