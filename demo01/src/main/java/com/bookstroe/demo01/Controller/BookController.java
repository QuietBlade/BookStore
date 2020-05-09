package com.bookstroe.demo01.Controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.otherUtil;
import com.sun.rowset.CachedRowSetImpl;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController
@RequestMapping("/api/book")
public class BookController {

    //@RequestMapping(value = "/",produces = "application/json;charset=utf-8")
    @RequestMapping(value = "/img")
    public String imgupload(HttpServletRequest req){
        return req.getParameter("file");
    }

    //图片上传接口
    @RequestMapping("/upload")
    public static String fileupload(@RequestParam(value = "file") MultipartFile file, ModelMap map) throws IOException {
        Map<String,String> json = new HashMap<>();
        if(file.isEmpty()){
            map.addAttribute("filepath", " ");
            json.put("src","/img/default.jpg");
            return JSON.toJSONString(json);
        }
        File directory = new File("../../resources/static/img/");
//        System.out.println(directory.getCanonicalPath());

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        Date dt=new Date();
        DateFormat time = new SimpleDateFormat("yyyy/MM/dd/");
        //String filePath = "D:/temp-rainy/" + time.format(dt); // 上传后的路径
        String filePath = directory.getCanonicalPath() + '/' + time.format(dt); // 上传后的路径
        System.out.println(filePath);
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

    //分类接口
    @RequestMapping(value = "/book_classify",produces = "application/json;charset=utf-8")
    public static String classify(HttpServletRequest req, HttpServletResponse res) {
        String mainid = req.getParameter("classifyMain");
        String classifyName = null;
        Map<String,Object> json = new HashMap<>();
        Map<String,String> classify = new HashMap<>();

        if( otherUtil.isConSpeCharacters(mainid)){
            json.put("code","-1");
            json.put("msg","不能有特殊字符");
            return JSON.toJSONString(json);
        }

        String sql = "select * from book_classifyTwo where classifyMainID="+mainid;
        try {
            CachedRowSetImpl rs = DButil.execQuery(sql);
            if( rs == null){
                return JSON.toJSONString(otherUtil.errorMessage("-1"));
            }
            while(rs.next()){
                classify.put(rs.getString("classifyID"),rs.getString("classifyName"));
            }
            rs = DButil.execQuery("SELECT * FROM book_classifyMain WHERE classifyID="+mainid);
            if( rs == null){
                return JSON.toJSONString(otherUtil.errorMessage("-1"));
            }
            if(rs.next()){
                classifyName = rs.getString("classifyName");
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
            json.put("code","-1");
            json.put("classifyMain",mainid);
            json.put("classifyName",classifyName);
            json.put("msg","连接数据库失败");
            return JSON.toJSONString(json);
        }
        json.put("code","1");
        json.put("classifyMain",mainid);
        json.put("classifyName",classifyName);
        json.put("data",classify);

        return JSON.toJSONString(json);
    }

    //添加图书
    @RequestMapping(value = "/book_add",produces = "application/json;charset=utf-8")
    public static String addBook(HttpServletRequest req, HttpServletResponse res){
        Book book = new Book();
//      {"bookname":"童书-少儿英语","bookmoney":"23.9","booknumber":"233","classifyMain":"1","classifyTwo":"23","booktext":"图片描述","bookimg":"","file":""}
        Map<String,String> json = new HashMap<>();
        json.put("code","-1");
        json.put("msg","不能为空");

        Author author = DButil.getAuthor(req);
        if( author.getLoginGroup().equals("guest")){
            json.put("msg","请登录后再添加");
            return JSON.toJSONString(json);
        }else if( !author.getLoginGroup().equals("admin")){
            json.put("msg","当前不是管理员");
            return JSON.toJSONString(json);
        }

        String bookname = req.getParameter("bookname");
        String bookmoney = req.getParameter("bookmoney");
        String booknumber = req.getParameter("booknumber");
        String classifyMain = req.getParameter("classifyMain");
        String classifyTwo = req.getParameter("classifyTwo");
        String booktext = req.getParameter("booktext");
        String bookimg = req.getParameter("bookimg");

        if( bookname==null || bookmoney==null || booknumber==null || classifyMain==null || classifyTwo==null || booktext==null){
            return JSON.toJSONString(json);
        }

        if( !otherUtil.isNumber(bookmoney) || !otherUtil.isNumber(booknumber) || !otherUtil.isNumber(classifyMain) || !otherUtil.isNumber(classifyTwo)){
            json.put("msg","价格或数量必须是数字");
            return JSON.toJSONString(json);
        }

        if( otherUtil.isConSpeCharacters(bookname) || otherUtil.isConSpeCharacters(booktext)){
            json.put("msg","不能有特殊字符");
            return JSON.toJSONString(json);
        }

        book.setName(bookname);
        book.setNum(Integer.valueOf(booknumber));
        book.setPrice(Double.valueOf(bookmoney));
        book.setImgurl(bookimg);
        book.setClassifyMain(Integer.valueOf(classifyMain));
        book.setClassifyTwo(Integer.valueOf(classifyTwo));
        book.setDesc(booktext);
        if( DButil.addBook(book) < 1){
            json.put("msg","数据库连接失败");
        }else{
            json.put("msg","添加成功");
            json.put("code","1");
        }
        return JSON.toJSONString(json);
    }

    //图书推荐
    @RequestMapping(value = "/recommend",produces = "application/json;charset=utf-8")
    public static String recommendBook(HttpServletRequest req, HttpServletResponse res){
        String len = req.getParameter("len");
        if( len == null || !otherUtil.isNumber(len) ){
            len = "10";
        }
        Map<String,Object> json = new HashMap<>();
        json.put("code","1");
        json.put("data",DButil.allBook(len));
        json.put("msg","查询成功");
        return JSON.toJSONString(json);
    }

    //图书搜索
    @RequestMapping(value = "/search",produces = "application/json;charset=utf-8")
    public static String search(HttpServletRequest req, HttpServletResponse res){
        String main = req.getParameter("classifyMain");
        String two = req.getParameter("classifyTwo");
        String page = req.getParameter("page");
        String length = req.getParameter("length");
        String key = req.getParameter("book");
        String sql = null;
        //如果没有key, 检查是否有二级分类,是否有主类
        //有key,按照key来搜索
        //page第几页
        if( main == null || !otherUtil.isNumber(main)){
            main = "%";
        }
        if( two == null || !otherUtil.isNumber(two)){
            two = "%";
        }
        if( page == null || !otherUtil.isNumber(page)){
            page = "1";
        }
        if( length == null || !otherUtil.isNumber(length)){
            length = "8";
        }
        if( key == null){
            key = "%";
        }

        String limit = String.valueOf((Integer.valueOf(page) - 1) * Integer.valueOf(length) );

        sql = "SELECT * from book_products WHERE book_name LIKE '%"+key+"%' and book_classifyMain like '%"+main+"%' and book_classifyTwo like '%"+two+"%' limit "+limit+","+length+"";
        System.out.println(sql);
        Map<String,Object> json = new HashMap<>();
        json.put("code","1");
        json.put("data",DButil.searchBook(sql));
        json.put("msg","查询成功");
        return JSON.toJSONString(json);
    }

    //添加到购物车
    @RequestMapping(value = "/addcart",produces = "application/json;charset=utf-8")
    public static String addCart(HttpServletRequest req, HttpServletResponse res){
        String book_id = req.getParameter("book_id");
        String num = req.getParameter("num");
        Author author = DButil.getAuthor(req);
        if( author.getLoginGroup().equals("guest")){
            return JSON.toJSONString(otherUtil.errorMessage("-16"));
        }
        if(num == null || !otherUtil.isNumber(num)){
            num = "1";
        }
        if(book_id == null){
            return JSON.toJSONString(otherUtil.errorMessage("-59"));
        }
        Book book = DButil.findBook(book_id);
        if( book == null){
            return JSON.toJSONString(otherUtil.errorMessage("-58"));
        }
        HttpSession session = req.getSession(true);
        Integer index = 0;
        boolean decide = true; //控制是否新增图书
        Map<String,Object> carts = (Map<String,Object>)session.getAttribute("carts");
        Map<String,Object> data  = new HashMap<>();
        if( carts == null){ //如果购物车为空
            carts = new HashMap<>();
        }else{
            while(true){
                if( carts.get(String.valueOf(index)) == null){
                    break;
                }
                //必须用临时变量,不然数据会叠加
                Map<String,Object> datatmp = (Map<String,Object>)carts.get(String.valueOf(index));
                Book booktmp = (Book)datatmp.get("data");
                if( booktmp.getId().equals(book.getId())){
                    //图书相同,数量相加
                    Integer snum = (Integer)datatmp.get("sum");
                    datatmp.put("sum",snum + Integer.valueOf(num));
                    data = datatmp;
                    decide = false;
                    break;
                }
                index += 1;
            }
        }

        if( decide ){ //没有相同的图书,新增
            data.put("data",book); //String.valueOf(index)
            data.put("sum",Integer.valueOf(num));
        }
        carts.put(String.valueOf(index),data);

        Map<String,Object> json = new HashMap<>();
        json.put("code","1");
        json.put("msg","添加到购物车成功");
        session.setAttribute("carts",carts);
        //return JSON.toJSONString(carts);
        return JSON.toJSONString(json);
    }

    //购物车修改或删除
    @RequestMapping(value = "/cartos",produces = "application/json;charset=utf-8")
    public static String osCart(HttpServletRequest req, HttpServletResponse res) {
        Author author = DButil.getAuthor(req);
        if( author.getLoginGroup().equals("guest")){
            return JSON.toJSONString(otherUtil.errorMessage("-16"));
        }

        String book_id = req.getParameter("bid");
        if( book_id == null)
            return JSON.toJSONString(otherUtil.errorMessage("-59"));

        Book book = DButil.findBook(book_id);
        if( book == null){
            return JSON.toJSONString(otherUtil.errorMessage("-58"));
        }

        String type = req.getParameter("type");
        String num = req.getParameter("num");
        if( num == null || type == null)
            type = "delete";

        HttpSession session = req.getSession(true);
        Integer index = 0;
        boolean decide = true; //控制是否删除/修改图书
        Map<String,Object> carts = (Map<String,Object>)session.getAttribute("carts");
        if( carts == null){ //如果购物车为空
            return JSON.toJSONString(otherUtil.errorMessage("-66"));
        }else{
            if( carts.get(String.valueOf(index)) == null){
                return JSON.toJSONString(otherUtil.errorMessage("-66"));
            }

            //删除购物车操作
            if( type.equals("delete") ) {
                while(true){
                    //必须用临时变量,不然数据会叠加
                    Map<String,Object> datatmp = (Map<String,Object>)carts.get(String.valueOf(index));
                    if( datatmp == null){
                        break;
                    }

                    Book booktmp = (Book)datatmp.get("data");
                    if( booktmp.getId().equals(book.getId())){
                        //id相同,删除记录
                        carts.remove(String.valueOf(index));
                        decide = false;
                        //删除记录,更新记录
                        for( ;  ; index++ ){
                            Map<String,Object> datatmptmp = (Map<String,Object>)carts.get(String.valueOf(index+1));
                            if( datatmptmp == null)
                                break;
                            carts.put(String.valueOf(index),datatmptmp);
                        }
                        carts.remove(String.valueOf(index));
                        break;
                    }
                    index += 1;
                }
            }
            //修改购物车操作
            else if( type.equals("update")){
                while(true){
                    //必须用临时变量,不然数据会叠加
                    Map<String,Object> datatmp = (Map<String,Object>)carts.get(String.valueOf(index));
                    if( datatmp == null)
                        break;
                    Book booktmp = (Book)datatmp.get("data");
                    if( booktmp.getId().equals(book.getId())){
                        //修改数量
                        datatmp.put("sum",Integer.valueOf(num));
                        carts.put(String.valueOf(index),datatmp);
                        decide = false;
                        break;
                    }
                    index += 1;
                }
            }
        }



        if( decide ){
            return JSON.toJSONString(otherUtil.errorMessage("-59"));
        }

        //System.out.println("delete:carts 365:"+carts);
        session.setAttribute("carts",carts);
        Map<String,Object> json = new HashMap<>();
        json.put("code","1");
        json.put("msg","删除成功");
        return JSON.toJSONString(json);
    }

    //购物车列表
    @RequestMapping(value = "/carts",produces = "application/json;charset=utf-8")
    public static String carts(HttpServletRequest req, HttpServletResponse res){
        Author author = DButil.getAuthor(req);
        if( author.getLoginGroup().equals("guest")){
            return JSON.toJSONString(otherUtil.errorMessage("-16"));
        }
        HttpSession session = req.getSession(false);
        if( session == null){
            return JSON.toJSONString(otherUtil.errorMessage("-66"));
        }
        Map<String,Object> carts = (Map<String,Object>)session.getAttribute("carts");
        if( carts == null)
            return JSON.toJSONString(otherUtil.errorMessage("-66"));
        Map<String,Object> json = new HashMap<>();
        json.put("code","1");
        json.put("carts",carts);
        json.put("msg","查询成功");
        return JSON.toJSONString(json);
    }

}
