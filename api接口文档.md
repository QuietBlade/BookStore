### api接口文档



#### user接口

用户登录验证接口 `/api/user/loginverification`

```javascript
请求类型 get/post
请求参数
{
    "_token": str(32)
    "username": str(6,20)
    "password": str(8,20)
    "captcha": str(4)
    "remember": boolean | true | false
}

返回数据
json{
    "code": "-1", 
    "status : "error",
    "msg" : "_token 异常，请刷新页面再试",
    "link" : "index.html",
}//默认返回
    
    code : msg {
        1 : 登陆成功
        0 : 数据库异常，请联系管理员
        -1 ： _token 异常，请刷新页面再试
        -2 : 验证码错误
        -3 : 用户名或密码不能为空
        -4 : 用户名或密码出现特殊字符
        -5 : 用户名或密码长度不足
        -6 : 用户名或密码错误 //实际上是用户名错误
        -7 : 用户名或密码错误 //实际上是密码错误
        -8 : 用户未激活 
    }
    
    
    link ： 默认返回主页的url，code -8 返回的激活页面
    如果身份是管理员，默认返回后台管理页面url
```



用户注册验证接口`/api/user/registration`

```javascript
请求类型 get/post
请求参数{
    "username"
    "_token"
    "password"
    "surepassword"
    "email"
}

返回参数
json{
    "code": "-1", 
    "status : "error",
    "msg" : "_token 异常，请刷新页面再试",
    "link" : "index.html",
    "active" : str(32) //激活码,验证通过才会发送
}//默认返回
  

```





获取验证码接口 `/api/user/verification`

```javascript
请求类型 get/post
请求参数{
    无
} //或者考虑接受 width height参数

返回参数
    返回一个 image/jpeg 的图片 高度 100px 宽度 20px
```

