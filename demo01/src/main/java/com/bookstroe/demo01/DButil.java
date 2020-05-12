package com.bookstroe.demo01;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.dao.AuthorDao;
import com.bookstroe.demo01.dao.BookDao;
//import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.CachedRowSet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.RowSetProvider;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DButil {

    private static AuthorDao dao = new AuthorDao();
    private static BookDao bookDao = new BookDao();

    public static Connection GetConnection() {
        //String DBPath = "jdbc:sqlite://C:/Users/world/Desktop/java web/实训/login/src/db/login.db";
        //String DBPath = "jdbc:sqlite:C:\\Users\\world\\Desktop\\javaweb\\demo01\\src\\main\\db\\login.db";
        String DBPath = "jdbc:sqlite:";
        File directory = new File("../../db/login.db");
        try{
            DBPath += directory.getCanonicalPath();
            //System.out.println(DBPath);
        }catch(IOException e){
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DBPath);
            //connection.setAutoCommit(false); //开启自动提交
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static int execQueryCountEmail(String email){
        int count = 0;
        String sql = "select count(*) from book_user where email='" +email+ "'";
        CachedRowSet rowset = DButil.execQuery(sql);
        try{
            if( rowset.next()){
                count = rowset.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return count;
    }

    public static CachedRowSet execQuery(String sql)  {
        CachedRowSet rowset = null;
        try{
            rowset = RowSetProvider.newFactory().createCachedRowSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection conn = GetConnection();
        Statement stat = null;
        try{
            stat = conn.createStatement();
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        ResultSet res = null;
        try {
            res = stat.executeQuery(sql);
            rowset.populate(res);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }

        try{
            res.close();
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowset;
    }

    public static int addUser(Author author) {
        author.setUid(otherUtil.StringUUID());
        author.setActivarionCode(otherUtil.stringToMD5(otherUtil.StringUUID()));
        author.setLoginpass(otherUtil.stringToMD5(author.getLoginpass()));
        author.setRegistTime(otherUtil.timestamp());
        return dao.add(author);
    }

    public static int addBook(Book book){
        book.setId(otherUtil.StringUUID());
        if( book.getImgurl() ==null)
            book.setImgurl("/img/default.jpg");
        return bookDao.add(book);
    }

    public static Map<String, Object> allBook(String len){
        return bookDao.allBook(len);
    }

    public static Map<String, Object> searchBook(String sql){
        return bookDao.searchBook(sql);
    }

    public static int passwordUser(Author author,String newpass){
        newpass = otherUtil.stringToMD5(newpass);
        String sql = "UPDATE book_user SET loginpass='"+ newpass +"' WHERE uid='"+author.getUid()+"'";
        return dao.update(sql);
    }

    public static Author findUser(String username){
        Author author =  new Author();
        if( otherUtil.isEmail(username))
            author =  dao.findAuthor(null,username);
        else
            author =  dao.findAuthor(username,null);
        return author;
    }

    //notice的方法 有点乱 通用
    public static int execUpdate(String sql) {
        try {
            Connection conn = GetConnection();
            Statement statement = conn.createStatement();
            int len = statement.executeUpdate(sql);
            statement.close();
            conn.close();
            return len;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //游客账户
    public static Author setGuest(){
        Author author = new Author();
        author.setUid(otherUtil.StringUUID());
        author.setLoginuser("guest");
        author.setLoginpass("guest");
        author.setStatus(0);
        author.setActivarionCode(null);
        author.setRegistTime(otherUtil.timestamp());
        author.setLoginGroup("guest");
        return author;
    }

    //获取账号
    public static Author getAuthor(HttpServletRequest req){
        Cookie[] cookies = req.getCookies();
        String uuid = null;
        for(Cookie cookie : cookies){
            if( cookie.getName().equals("uuid") )
                uuid = cookie.getValue();
            //System.out.println(cookie.getName() + cookie.getValue());
        }

        HttpSession session =  req.getSession();
        Author author = (Author)session.getAttribute("author");

            if( author != null ){
                return author;
            }else if( uuid != null){
                return dao.findAuthor(uuid);
            }

        return setGuest();
    }

    //查找图片
    public static Book findBook(String book_id) {
        return bookDao.findBook(book_id);
    }
}
