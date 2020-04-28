package com.bookstroe.demo01.Controller;
import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
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
import java.sql.SQLException;
import java.util.*;
@RestController
@RequestMapping("/api/book")
public class BookController {

    //@RequestMapping(value = "/",produces = "application/json;charset=utf-8")
    @RequestMapping(value = "/img")
    public String imgupload(HttpServletRequest req, HttpServletResponse res){
        Date dt=new Date();
        System.out.println(dt);
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
        String filePath = "D://temp-rainy//"; // 上传后的路径
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
        String fileLink = "/img/" + fileName;
        json.put("src",fileLink);
        return JSON.toJSONString(json);
    }

}
