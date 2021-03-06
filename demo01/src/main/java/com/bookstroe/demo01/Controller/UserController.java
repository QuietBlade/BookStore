package com.bookstroe.demo01.Controller;


import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.otherUtil;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
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
public class UserController {

    Author author = DButil.setGuest();
    boolean vercodemsg = false; // 验证码有效性

    @ResponseBody
    //用户登录验证
    @RequestMapping(value = "/user/loginverification", produces = "application/json;charset=utf-8")
    public String loginVerification(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        Map<String, String> json = new HashMap<>();

        String _token1 = (String) session.getAttribute("_token");
        String _token2 = req.getParameter("_token");
        String code = req.getParameter("captcha").toLowerCase();
        String captcha = (String) session.getAttribute("captcha");
        String remember = req.getParameter("remember");

        if (_token1 == null || !_token1.equals(_token2))
            return JSON.toJSONString(otherUtil.errorMessage("-1"));

        if (!code.equals(captcha))
            return JSON.toJSONString(otherUtil.errorMessage("-40"));

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null)
            return JSON.toJSONString(otherUtil.errorMessage("-27"));

        if (otherUtil.isConSpeCharacters(username) || otherUtil.isConSpeCharacters(password))
            return JSON.toJSONString(otherUtil.errorMessage("-26"));

        if (username.length() < 6 || username.length() > 20 || password.length() < 8 || password.length() > 20)
            return JSON.toJSONString(otherUtil.errorMessage("-28"));

        author = DButil.findUser(username);

        if (author.getLoginGroup().equals("guest"))
            return JSON.toJSONString(otherUtil.errorMessage("-25"));

        if (!author.getLoginpass().equals(otherUtil.stringToMD5(password)))
            return JSON.toJSONString(otherUtil.errorMessage("-25"));

        if (author.getStatus() == 0)
            return JSON.toJSONString(otherUtil.errorMessage("-14"));

        Cookie cookie = new Cookie("uuid", author.getUid());

        //记住密码
        if (remember != null) {
            cookie.setMaxAge(60 * 60 * 24 * 7); //7天
        } else {
            cookie.setMaxAge(0);
        }
        cookie.setPath("/");
        res.addCookie(cookie);

        json.put("msg", "登录成功");
        json.put("code", "1");
        String status = otherUtil.StringUUID();
        session.setAttribute("author", author);
        session.setAttribute("status", status);
        return JSON.toJSONString(json);
    }

    //用户注册
    @RequestMapping(value = "/user/registration", produces = "application/json;charset=utf-8")
    public String registration(HttpServletRequest req) throws Exception {
        Author author = new Author();
        Map<String, String> json = new HashMap<>();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String surepassword = req.getParameter("surepassword");
        String email = req.getParameter("email");
        String code = req.getParameter("vercode");

        HttpSession session = req.getSession();
        String _token1 = req.getParameter("_token");
        String _token2 = (String) session.getAttribute("_token");
        String vercode = (String) session.getAttribute("vercode");
        String veremail = (String) session.getAttribute("veremail");
        System.out.println( vercode + veremail);
        if (!_token1.equals(_token2))
            return JSON.toJSONString(otherUtil.errorMessage("-1"));

        if( code == null)
            return JSON.toJSONString(otherUtil.errorMessage("-41"));

        if( vercode == null)
            return JSON.toJSONString(otherUtil.errorMessage("-39"));

        if( !code.equals(vercode) )
            return JSON.toJSONString(otherUtil.errorMessage("-40"));

        if (username == null || password == null)
            return JSON.toJSONString(otherUtil.errorMessage("-27"));

        if (username.length() < 6 || password.length() < 8)
            return JSON.toJSONString(otherUtil.errorMessage("-28"));

        if (otherUtil.isConSpeCharacters(username) || otherUtil.isConSpeCharacters(password))
            return JSON.toJSONString(otherUtil.errorMessage("-26"));

        if (!otherUtil.isEmail(email) || !otherUtil.isPhone(email) || !email.equals(veremail))
            return JSON.toJSONString(otherUtil.errorMessage("-31"));

        if (!password.equals(surepassword))
            return JSON.toJSONString(otherUtil.errorMessage("-29"));

        //查询是否有相同用户名
        //查询是否有相同邮箱

        if( DButil.execQueryCountEmail(email) >= 1 )
            return JSON.toJSONString(otherUtil.errorMessage("-29"));
        
        author.setLoginuser(username);
        author.setLoginpass(password);
        author.setEmail(email);
        author.setStatus(0);
        author.setLoginGroup("users");
        if (DButil.addUser(author) == -1)
            return JSON.toJSONString(otherUtil.errorMessage("-15"));

        json.put("code", "1");
        json.put("status", "success");
        json.put("msg", "注册成功");
        return JSON.toJSONString(json);
    }

    //用户修改密码
    @RequestMapping(value = "/user/UpdatePassword", produces = "application/json;charset=utf-8")
    public String updatePassword(HttpServletRequest req) {
        Map<String, String> json = new HashMap<>();
        json.put("code", "-1");
        json.put("msg", "修改失败,请重新登录");
        json.put("link", "login.html");

        author = DButil.getAuthor(req);

        if (author.getLoginGroup().equals("guest"))
            return JSON.toJSONString(otherUtil.errorMessage("-16"));

        String oldpass = req.getParameter("oldpassword");
        String newpass = req.getParameter("newpassword");

        if (otherUtil.isConSpeCharacters(newpass) || otherUtil.isConSpeCharacters(oldpass))
            return JSON.toJSONString(otherUtil.errorMessage("-26"));

        if (oldpass.length() < 6 || newpass.length() < 6)
            return JSON.toJSONString(otherUtil.errorMessage("-28"));

        String oldpassmd5 = otherUtil.stringToMD5(oldpass);

        if (!oldpassmd5.equals(author.getLoginpass()))
            return JSON.toJSONString(otherUtil.errorMessage("-24"));

        if (DButil.passwordUser(author, newpass) < 1)
            return JSON.toJSONString(otherUtil.errorMessage("-6"));

        json.put("code", "1");
        json.put("msg", "修改成功，请重新登录");
        json.put("link", "/login");
        return JSON.toJSONString(json);
    }

    //用户忘记密码
    @RequestMapping(value = "/user/forget", produces = "application/json;charset=utf-8")
    public String forget(HttpServletRequest req) throws Exception {
        Map<String, String> json = new HashMap<>();

        HttpSession session = req.getSession();
        String username = req.getParameter("username");
        String code = req.getParameter("code");
        String newpasword = req.getParameter("password");

        if (username == null) {
            return JSON.toJSONString(otherUtil.errorMessage("-27"));
        } else if (code == null || code.length() < 1) {  //发送验证码
            String randomcode = otherUtil.Random();
            String title = "忘记密码";
            String text = "<html><body><h2>" + "忘记密码" + "</h2>" +
                    "<p> 这是您的验证码 " + randomcode + "</p>";
            if (otherUtil.isEmail(username) || otherUtil.isPhone(username)) {
                session.setAttribute("code", randomcode);
                otherUtil.sendMail(username, title, text);
            } else {
                //没有过滤特殊字符
                System.out.println(username);
                author = DButil.findUser(username);
                System.out.println(author.toString());
                if( author.getLoginGroup().equals("guest")){
                    return JSON.toJSONString(otherUtil.errorMessage("-35"));
                }
                session.setAttribute("code", randomcode);
                otherUtil.sendMail(author.getEmail(), title, text);
            }
            json.put("code", "1");
            json.put("msg", "已向您的邮箱发送验证码,请注意查收");
            return JSON.toJSONString(json);
        }

        if (code.equals(session.getAttribute("code")) || !otherUtil.isConSpeCharacters(newpasword)) {
            //验证码正确
            if (DButil.passwordUser(author, newpasword) < 1) {
                return JSON.toJSONString(otherUtil.errorMessage("-6"));
            } else {
                json.put("code", "1");
                json.put("msg", "修改成功");
                author = DButil.setGuest();
            }
        } else {
            return JSON.toJSONString(otherUtil.errorMessage("-44"));
        }
        return JSON.toJSONString(json);
    }

    //图片验证码
    @RequestMapping("/verificationcode")
    public String imagecode(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Cache-Control", "no-cache");
        res.setDateHeader("Expires", 0);
        res.setContentType("image/jpeg");
        OutputStream os = res.getOutputStream();
        Map<String, Object> map = otherUtil.getImageCode(100, 20, 4, os);
        String simpleCaptcha = "captcha";
        req.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        try {
            ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
        } catch (IOException e) {
            return "图片异常";
        }
        return null;
    }

    //用户注销
    @RequestMapping("/logoff")
    public String logoff(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("author") != null) {
            //session.removeAttribute("author");
            session.setAttribute("author", DButil.setGuest());
        }
        Cookie cookies = new Cookie("uuid",null);
        cookies.setMaxAge(0);
        cookies.setPath("/"); //删除cookie
        res.addCookie(cookies);
        author = DButil.setGuest();
        res.sendRedirect(req.getContextPath() + "/");
        return null;
    }

    //添加地址
    @RequestMapping("/user/address")
    public String add_address(){
        return "0";
    }

    //发送验证码
    @RequestMapping(value = "/sendvercode", produces = "application/json;charset=utf-8")
    public String sendvercode(HttpServletRequest req, HttpServletResponse res){
        System.out.println(vercodemsg);
        Map<String, String> json = new HashMap<>();
        HttpSession session = req.getSession();
        if( vercodemsg == false){
            vercodemsg = true;
            String email = req.getParameter("email");
            String code = otherUtil.Random();

            json.put("code","1");
            if( otherUtil.isPhone(email)){
                if( otherUtil.sendsms(email,code) == -1 )
                    return JSON.toJSONString(otherUtil.errorMessage("-19"));
                json.put("msg","验证码已发送到您的手机,请注意查收");
            }else if( otherUtil.isEmail(email)){
                String title = "在线书城 用户激活邮件";
                String text = "<html><body>" +
                        "<h1>欢迎注册在线书城系统</h1>" +
                        "<h2>这是你的验证码,1分钟内有效</h2>"+code+
                        "</body></html>";
                //发送邮箱
                if (otherUtil.sendMail(email, title, text) == -1)
                    return JSON.toJSONString(otherUtil.errorMessage("-19"));
                json.put("msg","验证码已发送到您的邮箱,请注意查收");
            }else
                json.put("msg","格式有误");
            //定时器
//            try {
//                final Timer timer=new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        session.removeAttribute("veremail");
//                        session.removeAttribute("vercode");
//                        vercodemsg = false;
//                        timer.cancel();
//                    }
//                },60*1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            System.out.println( "发送验证码" + code + email);
            session.setAttribute("veremail",email);
            session.setAttribute("vercode",code);
            return JSON.toJSONString(json);
        }

        json.put("code","-100");
        json.put("msg","1分钟之后再试");
        return JSON.toJSONString(json);
    }

    //用户是否登录
    @RequestMapping("/islogin")
    public boolean islogin(HttpServletRequest req, HttpServletResponse res){
        Author author = DButil.getAuthor(req);
        if(author.getLoginGroup().equals("guest"))
            return false;
        else
            return true;
    }

    //    测试发送短信
//    @RequestMapping("/sms")
//    public String find(HttpServletRequest req, HttpServletResponse res) throws SQLException {
//        int a = otherUtil.sendsms("15500000000","1234");
//        return String.valueOf(a);
//    }

    //    测试用户查找
//    @RequestMapping("/user/find")
//    public String find(HttpServletRequest req, HttpServletResponse res) throws SQLException {
//        return JSON.toJSONString(DButil.findUser("123456"));
//    }
//
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
//
//    测试验证码是否生效
//    @RequestMapping("/api/code")
//    public String image(HttpServletRequest req, HttpServletResponse res){
//        HttpSession session = req.getSession();
//        String code = (String)session.getAttribute("captcha");
//        return code;
//    }
//    测试邮件是否有效
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
//
//
//
//    测试md5是否生效
//    @RequestMapping("/md5")
//    public String md5(){
//        return otherUtil.stringToMD5(otherUtil.StringUUID());
//    }
//
//    测试Unix时间戳是否生效
//
//    @RequestMapping("/time")
//    public String time(){
//        return otherUtil.timestamp();
//    }
//
//    测试uuid是否生效
//    @RequestMapping("/uuid")
//    public String uuid(){
//        return otherUtil.StringUUID() + "长度:" + otherUtil.StringUUID().length();
//    }
//
//    测试数据库连接是否正常
    @RequestMapping("/db")
    public String db() throws SQLException {
        Connection sql = DButil.GetConnection();
        if (sql == null) {
            return "连接数据库失败";
        }
        sql.close();
        return "连接数据库成功";
    }

    //
//   测试项目是否正常
    @RequestMapping("/hello")
    public String test() {
        return "this /api/hello";
    }

//    测试
//    @RequestMapping("/t")
//    public String t(){
//        try{
//            Integer i = Integer.valueOf("cnnn");
//            System.out.println("=>>" + i);
//            return String.valueOf(i);
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

}
