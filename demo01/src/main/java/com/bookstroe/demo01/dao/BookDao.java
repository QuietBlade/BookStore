package com.bookstroe.demo01.dao;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.beans.bookDao;
import com.bookstroe.demo01.otherUtil;
import com.sun.rowset.CachedRowSetImpl;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookDao implements bookDao {
    @Override
    public int add(Book book){
        PreparedStatement statement = null;
        Connection conn = null;
        int len = 0;
        conn = DButil.GetConnection();
        String sql = "INSERT INTO book_products VALUES(?,?,?,?,?,?,?,?)";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1,book.getId());
            statement.setString(2,book.getName());
            statement.setDouble(3,book.getPrice());
            statement.setInt(4,book.getNum());
            statement.setInt(5,book.getClassifyMain());
            statement.setInt(6,book.getClassifyTwo());
            statement.setString(7,book.getImgurl());
            statement.setString(8,book.getDesc());
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

    public int update(Book book){
        return 0;
    }

    public int delete(String bid){
        return 0;
    }

    public Book findBook(String username,String email){
        return null;
    }

    public Map<String,String> allBook(String number){
        Map<String,String> map = new HashMap<>();
        String sql = "select * from book_products order by book_num desc limit"+number;
        CachedRowSet crst = DButil.execQuery(sql);
        try{
            while(crst.next()){

            }
        }catch (SQLException e){
            e.printStackTrace();
            return otherUtil.errorMessage("-1");
        }


        return null;
    }
}
