package com.bookstroe.demo01;


import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.NoticeDao;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    Author author = null;

    @ResponseBody
    @RequestMapping(value= "/user/loginverification",produces = "application/json;charset=utf-8")
    public String loginVerification(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        HttpSession session = req.getSession();
        Map<String,String> json = new HashMap<String,String>();
        json.put("code","-1");
        json.put("status","error");
        json.put("msg","_token 异常，请刷新页面再试");
        json.put("link","login.html");
        String _token1 = (String)session.getAttribute("_token");
        String _token2 = req.getParameter("_token");
        String code = req.getParameter("captcha").toLowerCase();
        String captcha = (String)session.getAttribute("captcha");
        String remember = req.getParameter("remember");

        if(!_token1.equals(_token2) || _token1 == null || _token2 == null ){
            json.put("code","-1");
            json.put("msg","_token 异常，请刷新页面再试");
            return JSON.toJSONString(json);
        }

        if( !code.equals(captcha) || captcha == null){
            json.put("code","-2");
            json.put("msg","验证码错误");
            return JSON.toJSONString(json);
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = null;

        if (username == null || password == null) {
            json.put("code","-3");
            json.put("msg", "用户名或密码不能为空");
            return JSON.toJSONString(json);
        }

        if (otherUtil.isConSpeCharacters(username) || otherUtil.isConSpeCharacters(password)) {
            json.put("code","-4");
            json.put("msg", "用户名或密码出现特殊字符");
            return JSON.toJSONString(json);
        }

        if (username.length() < 6 || username.length() > 20 || password.length() < 8 || password.length() > 20) {
            json.put("code","-5");
            json.put("msg", "用户名或密码长度不足");
            return JSON.toJSONString(json);
        }

        author = DButil.findUser(username);

        if (author == null) {
            json.put("code","-6");
            json.put("msg", "用户名或密码错误");
            return JSON.toJSONString(json);
        }

        if (!author.getLoginpass().equals(otherUtil.stringToMD5(password))) {
            json.put("code","-7");
            json.put("msg", "用户名或密码错误");
            return JSON.toJSONString(json);
        }

        if( "0".equals(author.getStatus()) ){
            json.put("code","-8");
            json.put("msg", "用户名未激活");
            json.put("link","index.html");
            //这里需要更改link跳转页面
            return JSON.toJSONString(json);
        }


        json.put("msg","登录成功");
        json.put("code","1");
        json.put("status","success");
        String status = otherUtil.StringUUID();
        session.setAttribute("author",author);
        session.setAttribute("status",status);
        return JSON.toJSONString(json);
    }

    @RequestMapping( value ="/user/registration",produces = "application/json;charset=utf-8")
    public String registration(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Author author = new Author();
        Map<String,String> json = new HashMap<String,String>();
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


        if(!_token1.equals(_token2) || _token1 == null || _token2 == null ){
            json.put("msg","_token 异常");
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
            json.put("msg","用户名冲突");
            return JSON.toJSONString(json);
        }

        json.put("code","1");
        json.put("status","success");
        json.put("msg","注册成功,等待验证邮箱");
        json.put("activeCode",author.getActivarionCode());
        json.put("link","login.html");
        otherUtil.sendMail(author.getEmail(), author.getActivarionCode() );
        //发送邮箱
        return JSON.toJSONString(json);
    }

    @RequestMapping( value = "/user/UpdatePassword",produces = "application/json;charset=utf-8")
    public String updatePassword(HttpServletRequest req, HttpServletResponse res){
        HttpSession session = req.getSession();
        Author author = (Author) session.getAttribute("author");
        if( author == null){
            return "请登录后再试";
        }else
            return "当前身份:"+author.getLoginGroup();
    }

    //激活用户
    @RequestMapping( value = "/user/activeCode",produces = "text/html;charset=utf-8")
    public String activeCode(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String code = req.getParameter("code");
        if( code == null || otherUtil.isConSpeCharacters(code) || code.length() != 32){
            res.sendRedirect("http://localhost:8080/index");
        }else{
            String sql = "UPDATE book_user SET status=1 WHERE activationCode='"+ code +"'";
            try {
                if(DButil.execUpdate(sql) <= 0)
                    return "激活失败,请检查";
            }catch (Exception e){
                e.printStackTrace();
                return "激活失败,请检查";
            }
        }
        return "激活完成，<a href=\"http://localhost:8080/login\">点击返回登陆界面</a>";
    }

    //图片验证码
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

    //测试用户查找
    @RequestMapping("/user/find")
    public String find(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        return JSON.toJSONString(DButil.findUser("123456"));
    }

    @RequestMapping("/user/logoff")
    public String logoff(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        if( session.getAttribute("author") != null)
            session.removeAttribute("author");
        author = null;
        res.sendRedirect(req.getContextPath()+"/index");
        return null;
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


//    测试验证码是否生效
//    @RequestMapping("/api/code")
//    public String image(HttpServletRequest req, HttpServletResponse res){
//        HttpSession session = req.getSession();
//        String code = (String)session.getAttribute("captcha");
//        return code;
//    }
//  测试邮件是否有效
//    @RequestMapping("seed")
//    public String seedMail() throws Exception {
//        try{
//            otherUtil.sendMail("yuanzhangzcc@163.com", otherUtil.stringToMD5(otherUtil.StringUUID()));
//            return "发送成功";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "发送失败";
//        }
//    }

    //测试查询公告是否生效
    @RequestMapping("/notice")
    public String notice(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        String length  = req.getParameter("length");
        String page = req.getParameter("page"); //String
        if( length == null || page == null){
            return NoticeDao.SelectNotice(10,1);
        }else{
            return NoticeDao.SelectNotice(Integer.valueOf(length),Integer.valueOf(page));
        }
    }

    @RequestMapping("/notice_add")
    public String notice_add(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        String time = req.getParameter("time");
        Map<String,String> map = new HashMap<>();

        if( author == null){
            map.put("code","-1");
            map.put("msg","请先登录");
        }else if( !author.getLoginGroup().equals("admin") ) {
            map.put("code","-1");
            map.put("msg","权限不够");
        }
        else{
            map = NoticeDao.InsertNotice(title, text, time);
        }
        return JSON.toJSONString(map);
    }

//  测试md5是否生效
    @RequestMapping("/md5")
    public String md5(){
        return otherUtil.stringToMD5("123456")+"长度:"+otherUtil.stringToMD5("123456").length();
    }

//  测试Unix时间戳是否生效

    @RequestMapping("/time")
    public String time(){
        return otherUtil.timestamp();
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
            return "连接数据库成功";
        }
        return "连接数据库失败";
    }

// 测试项目是否正常
    @RequestMapping("/hello")
    public String test(){
        return "this /api/hello";
    }



}
