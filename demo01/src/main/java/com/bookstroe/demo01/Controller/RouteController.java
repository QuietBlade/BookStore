package com.bookstroe.demo01.Controller;

import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.otherUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class RouteController {

    @RequestMapping("/head.html")
    public String index(HttpServletRequest req, HttpServletResponse res,ModelMap map) throws IOException {
        HttpSession session = req.getSession();
        Author author = DButil.getAuthor(req);
        if( author == null){
            author = DButil.setGuest();
            session.setAttribute("author",author);
        }
        map.addAttribute("author",author);
        return "head.html";
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
        Book book = DButil.findBook(book_id);
        if( book.getId() == null){
            return "404";
        }
        map.addAttribute("book",book);
        return "book";
    }

}
