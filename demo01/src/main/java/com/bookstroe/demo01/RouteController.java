package com.bookstroe.demo01;


import com.bookstroe.demo01.beans.Author;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class RouteController {

    @RequestMapping("/index")
    public String index(HttpServletRequest req, HttpServletResponse res){
        return "index";
    }
    @RequestMapping("/")
    public String index1(HttpServletRequest req, HttpServletResponse res,ModelMap map){
        HttpSession session = req.getSession();
        Author author = (Author) session.getAttribute("author");
        if( author != null){
            map.addAttribute("user",author.toString());
            map.addAttribute("status",session.getAttribute("status"));
        }else
        map.addAttribute("user","当前身份：游客");
        return "index";
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
