package com.bookstroe.demo01.dao;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.beans.bookDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class BookDao implements bookDao {
    @Override
    public int add(Book book){
        PreparedStatement statement = null;
        Connection conn = null;

        conn = DButil.GetConnection();
        String sql = "INSERT INTO book_products VALUES(?,?,?,?,?,?,?)";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1,book.getId());
            statement.setString(2,book.getName());
            statement.setDouble(3,book.getPrice());
            statement.setString(4,book.getClassif());
            statement.setInt(5,book.getNum());
            statement.setString(6,book.getImgurl());
            statement.setString(7,book.getDesc());
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        try{
            statement.executeUpdate();
            statement.close();
            conn.close();
        }catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public int update(Book book){
        return 0;
    }

    public int delete(String bid){
        return 0;
    }

    public Book findBook(String username,String email){
        return null;
    }

}
