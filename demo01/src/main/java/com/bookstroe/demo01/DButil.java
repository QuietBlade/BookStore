package com.bookstroe.demo01;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.AuthorDao;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;


public class DButil {

    private static AuthorDao dao = new AuthorDao();

    public static Connection GetConnection() {
        //String DBPath = "jdbc:sqlite://C:/Users/world/Desktop/java web/实训/login/src/db/login.db";
        //String DBPath = "jdbc:sqlite:C:\\Users\\world\\Desktop\\java web\\实训\\login\\src\\db\\login.db";
        String DBPath = "jdbc:sqlite:C:\\Users\\world\\Desktop\\javaweb\\demo01\\src\\main\\db\\login.db";
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

    public static ResultSet execQuery(String sql) throws SQLException {
        Connection conn = GetConnection();
        Statement stat = conn.createStatement();
        ResultSet res = null;
        try {
            res = stat.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
//        stat.close();
//        conn.close();
        return res;
    }

    public static int addUser(Author author) throws SQLException {
        author.setUid(otherUtil.StringUUID());
        author.setActivarionCode(otherUtil.stringToMD5(otherUtil.StringUUID()));
        author.setStatus(0);
        author.setLoginpass(otherUtil.stringToMD5(author.getLoginpass()));
        author.setRegistTime(otherUtil.timestamp());
        return dao.add(author);
    }

    public static int passwordUser(Author author,String newpass){
        newpass = otherUtil.stringToMD5(newpass);
        String sql = "UPDATE book_user SET loginpass='"+ newpass +"' WHERE uid='"+author.getUid()+"'";
        return dao.update(sql);
    }

    public static Author findUser(String username) throws SQLException{
        Author author =  new Author();
        if( otherUtil.isEmail(username))
            author =  dao.findAuthor(null,username);
        else
            author =  dao.findAuthor(username,null);
        return author;
    }

    //notice的方法 有点乱
    public static int execUpdate(String SQLQuery) {
        try {
            Connection conn = GetConnection();
            Statement statement = conn.createStatement();
            int len = statement.executeUpdate(SQLQuery);
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
        HttpSession session =  req.getSession();
        Author author = (Author)session.getAttribute("author");
        if( author == null ){
            return setGuest();
        }else
            return author;
    }

}
