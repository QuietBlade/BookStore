package com.bookstroe.demo01;

import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.dao.AuthorDao;

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

    //废弃，不用
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
        author.setRegistLogin(otherUtil.timestamp());
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

    public static int ExecQuery(String SQLQuery) {
        try {
            Connection conn = GetConnection();
            Statement statement = conn.createStatement();
            int len = statement.executeUpdate(SQLQuery);
            return len;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
