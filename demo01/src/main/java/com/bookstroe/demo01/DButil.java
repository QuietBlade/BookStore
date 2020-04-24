package com.bookstroe.demo01;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.AuthorDao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DButil {

    private static AuthorDao dao = new AuthorDao();
    Connection connection = null;

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

    public static Author findUser(String username) throws SQLException{
        Author author =  new Author();
        if( otherUtil.isEmail(username))
            author =  dao.findAuthor(null,username);
        else
            author =  dao.findAuthor(username,null);
        return author;
    }

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

    public static Author Guest(){
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

}
