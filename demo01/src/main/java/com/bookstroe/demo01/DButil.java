package com.bookstroe.demo01;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.AuthorDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.server.ExportException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static String query(String str) throws SQLException {
        Connection conn = GetConnection();
        String sql = "INSERT INTO book_user VALUES('2','222','222','222',0,'222','222')";
        Statement sql_statement = conn.createStatement();
        try {
            sql_statement.executeUpdate(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return "执行失败";
        }
        conn.commit();
        return "执行成功";
    }


    public static int addUser(Author author) throws SQLException {
        author.setUid(otherUtil.StringUUID());
        author.setActivarionCode(otherUtil.stringToMD5(otherUtil.StringUUID()));
        author.setStatus(0);
        author.setLoginpass(otherUtil.stringToMD5(author.getLoginpass()));
        return dao.add(author);
    }

    public static String findUser(String username) throws SQLException{
        Author author =  dao.findAuthor(username);
        if( author.getLoginuser() == null){
            return "null";
        }
        return author.toString();
    }

    public String ExecQuery(Connection connection, String SQLQuery) {
        try {
            //String SQLQuery = "create table abc2(name varchar(20));";
            Statement sql_statement = connection.createStatement();
            sql_statement.executeUpdate(SQLQuery);
            connection.commit();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Success";
    }


}
