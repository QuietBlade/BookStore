### Java web程序设计

[TOC]



BookStore在线书城系统，采用 Spring boot + Sqlite设计

目前进度如下：

用户管理系统

- [ ] 用户管理系统UI
- [x] 用户名/邮箱登录
- [x] 用户注册
- [x] 邮箱验证
- [x] 用户修改密码
- [ ] <del>用户修改用户信息</del>
- [x] 用户忘记密码
- [x] 登录记住密码

公告管理系统

- [ ] 公告系统UI
- [x] 查询公告
- [x] 添加公告
- [ ] <del>富文本编辑器</del>
- [x] 删除公告
- [x] 修改公告

图书管理系统

- [ ] 图书管理系统UI
- [x] 查询图书
- [x] 添加图书
- [x] 图片上传(multipart)
- [ ] 删除图书

购物车管理系统

- [x] 添加图书到购物车
- [x] 查询购物车
- [ ] 从购物车删除图书
- [ ] 从购物车修改图书

订单管理系统

- [ ] 订单管理系统UI
- [ ] 订单查询
- [ ] 订单生成
- [ ] 取消订单
- [ ] 订单结束


##### 如何使用

+ Clone 项目

  `git clone http://cloud.yuanzhangzcc.com:3000/world/java-web`

+ 导入项目

  打开 `idea`，然后 `打开项目` - 选择项目中的 `demo01 `文件夹

+ 下载springboot包

  Idea 中执行 `maven install` 会自动下载jar包，遇到错误请设置maven代理  [阿里云仓库](<https://help.aliyun.com/document_detail/102512.html?spm=a2c40.aliyun_maven_repo.0.0.36183054oA3kNQ>)

  ```xml
  <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>*</mirrorOf>
      <name>阿里云公共仓库</name>
      <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
  ```

  如使用idea中的maven,配置文件在 `IntelliJ IDEA安装目录下\plugins\maven\lib\maven3\conf\settings.xml`

+ 运行项目
enjoy !
