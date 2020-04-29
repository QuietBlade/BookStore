package com.bookstroe.demo01.Controller;
import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.dao.NoticeDao;
import com.bookstroe.demo01.otherUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController
@RequestMapping("/api/book")
public class BookController {

    //@RequestMapping(value = "/",produces = "application/json;charset=utf-8")
    @RequestMapping(value = "/img")
    public String imgupload(HttpServletRequest req, HttpServletResponse res){
        return req.getParameter("file");
    }

    @RequestMapping("/upload")
    public static String fileupload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest req, ModelMap map){
        Map<String,String> json = new HashMap<>();
        if( file.isEmpty() || file == null ){
            map.addAttribute("filepath", " ");
            json.put("src","none");
            return JSON.toJSONString(json);
        }

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        Date dt=new Date();
        DateFormat time = new SimpleDateFormat("yyyy/MM/dd/");
        String filePath = "D:/temp-rainy/" + time.format(dt); // 上传后的路径
        // System.out.println(filePath);
        // 输出的路径 : D:/temp-rainy/2020/04/28/
        fileName = otherUtil.StringUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileLink = "/img/" + time.format(dt) + fileName;
        json.put("src",fileLink);
        return JSON.toJSONString(json);
    }

    @RequestMapping(value = "/book_classify",produces = "application/json;charset=utf-8")
    public static String classify(HttpServletRequest req, HttpServletResponse res) throws SQLException {
        //获取一级分类
        String mainid = req.getParameter("classifyMain");
        Map<String,Object> json = new HashMap<>();
        Map<String,String> classify = new HashMap<>();

        if( otherUtil.isConSpeCharacters(mainid)){
            json.put("code","-1");
            json.put("msg","不能有特殊字符");
            return JSON.toJSONString(json);
        }

        String sql = "select * from book_classifyTwo where classifyMainID="+mainid;
        try {
            ResultSet rs = DButil.execQuery(sql);
            while(rs.next()){
                classify.put(rs.getString("classifyID"),rs.getString("classifyName"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            json.put("code","-1");
            json.put("msg","连接数据库失败");
            return JSON.toJSONString(json);
        }
        json.put("code","1");
        json.put("data",classify);
        return JSON.toJSONString(json);
    }

    @RequestMapping(value = "/book_add",produces = "application/json;charset=utf-8")
    public static String addBook(HttpServletRequest req, HttpServletResponse res){
        Book book = new Book();

        return null;
    }
}
