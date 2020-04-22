package com.bookstroe.demo01;


import ch.qos.logback.core.db.dialect.DBUtil;
import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.AuthorDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class user {
    Author author = new Author();

    @ResponseBody
    @RequestMapping(value= "/user/loginverification",produces = "application/json;charset=utf-8")
    public String loginVerification(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        Map<String,String> json = new HashMap<String,String>();
        json.put("code","-1");
        json.put("status","error");
        json.put("msg","登录失败");
        json.put("link","login.html");
        String _token1 = (String)session.getAttribute("_token");
        String _token2 = req.getParameter("_token");
        String code = req.getParameter("captcha").toLowerCase();
        String codever = (String)session.getAttribute("captcha");
        String remember = req.getParameter("remember");
        if(_token1 == null || _token2 == null){
        //if( _token2 == null ){
            json.put("msg","_token不能为空");
            return JSON.toJSONString(json);
        }
        if(!_token1.equals(_token2)){
            json.put("msg","_token错误");
            return JSON.toJSONString(json);
        }

        if( !code.equals(codever) ){
            json.put("msg","验证码错误");
            return JSON.toJSONString(json);
        }

        String username = req.getParameter("username"); //字符串过滤
        String password = req.getParameter("password"); //字符串过滤

        if(otherUtil.isConSpeCharacters(username)){
            json.put("msg","用户名或密码不能包含特殊字符");
            return JSON.toJSONString(json);
        }

        if( username == null || password == null){
            json.put("msg","用户名或密码不能为空");
            return JSON.toJSONString(json);
        }

        if( username.length() < 6 || username.length() > 20 || password.length() < 8 || password.length() > 20){
            json.put("msg","用户名或密码长度不够");
            return JSON.toJSONString(json);
        }


        json.put("msg","登录成功");
        json.put("code","1");
        json.put("status","success");
        String status = otherUtil.StringUUID();
        session.setAttribute("status",status);

        return JSON.toJSONString(json);
    }

    @RequestMapping(value="/user/login",produces = "application/json;charset=utf-8")
    public String login(HttpServletRequest req, HttpServletResponse res){
        return JSON.toJSONString(req.getParameterMap());
    }

    @RequestMapping( value ="/user/registration",produces = "application/json;charset=utf-8")

    public String registration(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        Author author = new Author();
        Map<String,String> json = new HashMap<String,String>();
        //res.setHeader("Content-Type","application/json,charset=utf-8");
        json.put("code","-1");
        json.put("status","error");
        json.put("msg","用户名或密码不能为空");
        json.put("link","index.html");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String surepassword = req.getParameter("surepassword");
        String email = req.getParameter("email");
        HttpSession session = req.getSession();
        String _token1 = req.getParameter("_token");
        String _token2 = (String)session.getAttribute("_token");

        if(_token1 == null || _token2 == null){
            json.put("msg","_token不能为空");
            return JSON.toJSONString(json);
        }

        if(!_token1.equals(_token2)){
            json.put("msg","_token错误");
            return JSON.toJSONString(json);
        }

        if( username == null || password == null)
            return JSON.toJSONString(json);

        if( username.length() < 6 || password.length() < 8){
            json.put("msg","用户名长度不足6或密码长度不足8");
            return JSON.toJSONString(json);
        }

        if(otherUtil.isConSpeCharacters(username) || otherUtil.isConSpeCharacters(password)){
            json.put("msg","用户名或密码不能包含特殊字符");
            return JSON.toJSONString(json);
        }

        if(!otherUtil.isEmail(email)){
            json.put("msg","邮箱格式不正确");
            return JSON.toJSONString(json);
        }

        if(!password.equals(surepassword)){
            json.put("msg","两次密码不正确");
            return JSON.toJSONString(json);
        }

        //查询是否有相同用户名
        //查询是否有相同邮箱

        author.setLoginuser(username);
        author.setLoginpass(password);
        author.setEmail(email);
        author.setLoginGroup("users");
        if(DButil.addUser(author) == -1){
            json.put("msg","用户不唯一");
            return JSON.toJSONString(json);
        }

        json.put("code","1");
        json.put("status","success");
        json.put("msg","注册成功");
        json.put("activeCode",author.getActivarionCode());
        json.put("link","login.html");
        return JSON.toJSONString(json);
    }

    @RequestMapping("/verificationcode")
    public String imagecode(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg");
        OutputStream os = res.getOutputStream();
        Map<String,Object> map = otherUtil.getImageCode(100,20,4, os);
        String simpleCaptcha = "captcha";
        req.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
        } catch (IOException e) {
            return "图片异常";
        }
        return null;
    }

    @RequestMapping("/user/find")
    public String find(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        return DButil.findUser("123456");
    }

//    添加用户的测试案例
//    @RequestMapping("/user/add")
//    public String addUser(HttpServletRequest req, HttpServletResponse res) throws SQLException {
//        Author author = new Author();
//        AuthorDao dao = new AuthorDao();
//        author.setLoginuser("1234561");
//        author.setLoginpass("123456");
//        author.setEmail("email@163.com");
//        author.setUid(otherUtil.StringUUID());
//        author.setStatus(0);
//        author.setActivarionCode(otherUtil.stringToMD5(otherUtil.StringUUID()));
//        author.setLoginGroup("user");
//        dao.add(author);
//        return author.toString();
//    }

//    原生sql，查看是否有连接问题
//    @RequestMapping("/db/add")
//    public String testdb() throws SQLException {
//        return DButil.query("test");
//    }

//    测试验证码是否生效
//    @RequestMapping("/api/code")
//    public String image(HttpServletRequest req, HttpServletResponse res){
//        HttpSession session = req.getSession();
//        String code = (String)session.getAttribute("captcha");
//        return code;
//    }

//  测试md5是否生效
    @RequestMapping("/md5")
    public String md5(){
        return otherUtil.stringToMD5("123456")+"长度:"+otherUtil.stringToMD5("123456").length();
    }

//  测试uuid是否生效
    @RequestMapping("/uuid")
    public String uuid(){
        return otherUtil.StringUUID() + "长度:" + otherUtil.StringUUID().length();
    }

//  测试数据库连接是否正常
    @RequestMapping("/db")
    public String db(){
        Connection sql = DButil.GetConnection();
        if(sql != null){
            return "连接成功";
        }
        return "连接数据库失败";
    }

    @RequestMapping("/hello")
    public String index(){
        return "1803010121 张源";
    }

    @RequestMapping("/re")
    public String test(){
        return "login";
    }



}
