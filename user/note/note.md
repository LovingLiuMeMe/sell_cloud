# user服务配置
注意: 当登录时 直接使用user服务地址登录,一切正常cookie能够保存且更新,但是,当使用ZUUL网管时,cookie不能正常保存,可单独添加敏感头忽略或忽略所有敏感头
```xml
zuul:
  # 排除所有服务的敏感头 
  sensitive-headers: 
```
