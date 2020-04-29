package com.bookstroe.demo01.dao;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Author;
import com.bookstroe.demo01.beans.userDao;
import com.sun.rowset.CachedRowSetImpl;
import org.springframework.stereotype.Repository;

import javax.management.remote.JMXConnectionNotification;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AuthorDao implements userDao{

    @Override
    public int add(Author author){
        PreparedStatement statement = null;
        Connection conn = null;
        int len = 0;
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
            statement.setString(8,author.getRegistTime());
            len = statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        try{
            statement.close();
            conn.close();
        }catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return len;
    }

    @Override
    public int update(String sql){
        try {
            Connection conn = DButil.GetConnection();
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

    @Override
    public int delete(String uuid) {

        return 0;
    }

    @Override
    public Author findAuthor(String username,String email) {
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
                author.setRegistTime(res.getString("registTime"));
            }else
                return DButil.setGuest();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            res.close();
            pst.close();
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return author;
    }

    public Author findAuthor(String uuid){
        Author author = new Author();
        String sql = "select * from book_user where uid='"+uuid+"'";
        CachedRowSetImpl res = DButil.execQuery(sql);
        if( res == null){
            return DButil.setGuest();
        }
        try{
            if( res.next() ){
                author.setUid(res.getString("uid"));
                author.setLoginuser(res.getString("loginname"));
                author.setLoginpass(res.getString("loginpass"));
                author.setEmail(res.getString("email"));
                author.setStatus(res.getInt("status"));
                author.setActivarionCode(res.getString("activationCode"));
                author.setLoginGroup(res.getString("loginGroup"));
                author.setRegistTime(res.getString("registTime"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            author = DButil.setGuest();
        }

        return author;
    }
}
