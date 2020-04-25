package com.bookstroe.demo01.Controller;

import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.otherUtil;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class RouteController {

    @RequestMapping("/")
    public String index(HttpServletRequest req, HttpServletResponse res,ModelMap map){
        HttpSession session = req.getSession();
        Cookie cookie = getCookie(req.getCookies());
//        if(  == null){
//            ;
//        }
        Author author = (Author) session.getAttribute("author");
        if( author == null){
            author = DButil.Guest();
            session.setAttribute("author",author);
        }
        map.addAttribute("author",author);
        return "index";
    }

    //这里设置Cookie
    public Cookie getCookie( Cookie[] cookie){
        return null;
    }

    @RequestMapping("/login")
    public static String login(HttpServletRequest req, HttpServletResponse res,ModelMap map){
        String token = otherUtil.StringUUID();
        HttpSession session = req.getSession(true);
        session.setAttribute("_token", token);
        String html = "<input type=\"hidden\" name=\"_token\" value=\"" + token + "\"/>";
        map.addAttribute("csrf",html);
        return "login";
    }

    @RequestMapping("/regist")
    public static String regist(HttpServletRequest req, HttpServletResponse res,ModelMap map){
        String token = otherUtil.StringUUID();
        HttpSession session = req.getSession(true);
        session.setAttribute("_token", token);
        String html = "<input type=\"hidden\" name=\"_token\" value=\"" + token + "\"/>";
        map.addAttribute("csrf",html);
        return "regist";
    }

}