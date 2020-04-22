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
    "save": boolean | true | false
    "code": str(4)
}

返回数据
json{
	"code": "-1", 
    "status : "error",
	"msg" : "用户名和密码不能为空",
    "link" : "index.html",
}//默认返回
    
    code{
        -1 ： 登陆失败
        0 : 登录异常
        1 : 登录成功
    }
    
    status : "error" / "success"
    
    msg消息{
        0 : 登陆成功
        -1 : 用户名或者密码出错
        -2 : 用户名或者密码不能为空
        -3 : 用户未激活
        -4 : 验证码错误
        -5 : token出错
        -6 : 用户名长度
        -7 : 用户名或密码不能包含特殊字符
    }
    
    link 默认返回主页的url，如果身份是管理员，默认返回后台管理页面url
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
	"msg" : "用户名和密码不能为空",
    "link" : "index.html",
    "active" : str(32) //激活码,验证通过才会发送
}//默认返回
    其余参考用户登录返回
    
  
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

