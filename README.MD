### Java web程序设计

BookStore在线书城系统，采用 Spring boot + Sqlite设计

功能如下：

略



##### 如何使用

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

  如使用idea中的maven,配置文件在 `IntelliJ IDEA安装目录\plugins\maven\lib\maven3\conf\settings.xml`



+ 改动数据库路径

  [demo01/src/main/java/com/bookstroe/demo01/DButil.java](http://cloud.yuanzhangzcc.com:3000/world/java-web/src/master/demo01/src/main/java/com/bookstroe/demo01/DButil.java)

  10行，sqlite文件所在位置，更改为自己磁盘中 login.db 的路径







