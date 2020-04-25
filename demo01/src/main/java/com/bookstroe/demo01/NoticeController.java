package com.bookstroe.demo01;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.NoticeDao;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    Author author = DButil.Guest();

    @RequestMapping(value = "/",produces = "text/html;charset=utf-8")
    public String noti(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        return notice_sel(req, res);
    };

    //查询公告
    @RequestMapping(value = "/notice_sel",produces = "text/html;charset=utf-8")
    public String notice_sel(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        String length  = req.getParameter("length");
        String page = req.getParameter("page"); //String
        if( length == null || page == null){
            return JSON.toJSONString(NoticeDao.SelectNotice(10,1));
        }else{
            return JSON.toJSONString(NoticeDao.SelectNotice(Integer.valueOf(length),Integer.valueOf(page)));
        }
        //按下 Ctrl+B  然后 鼠标指向 SelectNotice 函数名  查看函数实现
    }

    //添加公告
    @RequestMapping(value = "/notice_add",produces = "text/html;charset=utf-8")
    public String notice_add(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        String time = req.getParameter("time");
        HttpSession session = req.getSession();
        author = (Author)session.getAttribute("author");
        Map<String,String> map = new HashMap<>();

        if( author.getLoginGroup().equals("guest") ) {
            map.put("code", "-1");
            map.put("msg", "请先登录");
        }else if( !author.getLoginGroup().equals("admin") ) {
            map.put("code","-1");
            map.put("msg","权限不够");
        }else{
            map = NoticeDao.InsertNotice(title, text, time);
        }
        return JSON.toJSONString(map);
    }

    //删除公告
    @RequestMapping(value = "/notice_del",produces = "text/html;charset=utf-8")
    public String notice_del(HttpServletRequest req, HttpServletResponse res){
//        String[] id = req.getParameterValues("id");
//        for( int i = 0 ; i < id.length ; i ++){
//            System.out.println(id[i]);
//        }
//      //如果是数组
        //不考虑多个id值
        String id = req.getParameter("id");
        Map<String,String> map = new HashMap<>();
        HttpSession session = req.getSession();
        author = (Author)session.getAttribute("author");

        if( author.getLoginGroup().equals("guest") ){
            map.put("code","-1");
            map.put("msg","请先登录");
        }else if( !author.getLoginGroup().equals("admin") ) {
            map.put("code","-1");
            map.put("msg","权限不够");
        }else{
            map = map = NoticeDao.DeleteNotice(id);
        }
        return JSON.toJSONString(map);
    }

    //修改公告
    @RequestMapping(value = "/notice_upd",produces = "text/html;charset=utf-8")
    public String notice_upd(HttpServletRequest req, HttpServletResponse res) {
        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String text = req.getParameter("text");
        String time = req.getParameter("time");
        Map<String, String> map = new HashMap<>();

        HttpSession session = req.getSession();
        author = (Author)session.getAttribute("author");

        if ( author.getLoginGroup().equals("guest") ) {
            map.put("code", "-1");
            map.put("msg", "请先登录");
        } else if (!author.getLoginGroup().equals("admin")) {
            map.put("code", "-1");
            map.put("msg", "权限不够");
        } else {
            map = NoticeDao.UpdateNotice(id , title, text, time);
        }
        return JSON.toJSONString(map);
    }

}
