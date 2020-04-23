package com.bookstroe.demo01.dao;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.userDao;
import org.springframework.stereotype.Repository;

import javax.management.remote.JMXConnectionNotification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthorDao implements userDao{

    @Override
    public int add(Author author) throws SQLException {
        PreparedStatement statement = null;
        Connection conn = null;

        conn = DButil.GetConnection();
        String sql = "INSERT INTO book_user VALUES(?,?,?,?,?,?,?,?)";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1,author.getUid());
            statement.setString(2,author.getLoginuser());
            statement.setString(3,author.getLoginpass());
            statement.setString(4,author.getEmail());
            statement.setInt(5,author.getStatus());
            statement.setString(6,author.getActivarionCode());
            statement.setString(7,author.getLoginGroup());
            statement.setString(8,author.getRegistLogin());
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        try{
            statement.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }
        statement.close();
        conn.close();
        return 1;
    }

    @Override
    public int update(Author author) {
        return 0;
    }

    @Override
    public int delete(String str) {

        return 0;
    }

    @Override
    public int execute(String str) {
        PreparedStatement statement = null;
        Connection conn = null;
        conn = DButil.GetConnection();
        return 0;
    }

    @Override
    public Author findAuthor(String username,String email) throws SQLException {
        Author author = new Author();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        String sql = "select * from book_user where loginname = ? or email = ?";
        conn = DButil.GetConnection();
        try{
            pst = conn.prepareStatement(sql);
            pst.setString(1,username);
            pst.setString(2,email);
            res = pst.executeQuery();
            if(res.next()){
                author.setUid(res.getString("uid"));
                author.setLoginuser(res.getString("loginname"));
                author.setLoginpass(res.getString("loginpass"));
                author.setEmail(res.getString("email"));
                author.setStatus(res.getInt("status"));
                author.setActivarionCode(res.getString("activationCode"));
                author.setLoginGroup(res.getString("loginGroup"));
                author.setRegistLogin(res.getString("registLogin"));
            }else
                return null;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            res.close();
            pst.close();
            conn.close();
        }

        return author;
    }
}
