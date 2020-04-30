package com.bookstroe.demo01.Controller;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import com.bookstroe.demo01.otherUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    Author author = DButil.setGuest();
    @ResponseBody

    //删除用户
    @RequestMapping(value = "/deluser",produces = "application/json;charset=utf-8")
    public String deleteuser(HttpServletRequest req, HttpServletResponse res){
        Map<String,String> json = new HashMap<String,String>();
        json.put("code","-1");
        json.put("msg", "权限不足");
        json.put("link","login.html");

        author = DButil.getAuthor(req);

        if( !author.getLoginGroup().equals("admin")){
            return JSON.toJSONString(json);
        }

        json.put("code","1");
        json.put("msg","删除成功");
        return JSON.toJSONString(json);
    }

}
