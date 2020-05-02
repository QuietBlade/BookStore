package com.bookstroe.demo01.Controller;

import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.otherUtil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class RouteController {

    @RequestMapping("/")
    public String index(HttpServletRequest req, HttpServletResponse res,ModelMap map) throws IOException {
        HttpSession session = req.getSession();
        Author author = DButil.getAuthor(req);
        if( author == null){
            author = DButil.setGuest();
            session.setAttribute("author",author);
        }
        map.addAttribute("author",author);
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

    @RequestMapping("/book/{id}")
    public static String searchBook(@PathVariable("id") String book_id, HttpServletRequest req, HttpServletResponse res, ModelMap map){
        System.out.println(book_id);
        return "book_tmp";
    }

}
