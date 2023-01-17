## PIGX 整合前后端分离项目登录

![](https://minio.pigx.vip/alei/2023/01/66d6c4525e9b90a2afbd0e4b8cd41ece.png)

## 插入客户端

```sql
INSERT INTO `pig`.`sys_oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `create_time`, `update_time`, `create_by`, `update_by`) VALUES ('ruoyi', '', 'ruoyi', 'server', 'authorization_code', '', '', 17800000, 17800000, '', 'false', '2023-01-17 10:37:32', '2023-01-17 10:37:32', 'admin', 'admin');

```

## RuoYi 后端信息

> 配置  RuoYi 前端地址 默认 1024 端口
> 配置  PIG 认证中心地址

![](https://minio.pigx.vip/alei/2023/01/222f680ce489d1d3545df1f191b69135.png)

## RuoYi 前端配置

- 登录页面配置
![](https://minio.pigx.vip/alei/2023/01/1368a6395176e47a3b9b4548da6bdd5e.png)

- 退出页面配置
![](https://minio.pigx.vip/alei/2023/01/54344adec7f766fa419e360da682526a.png)
