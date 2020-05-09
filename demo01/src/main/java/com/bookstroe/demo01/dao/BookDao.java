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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Book findBook(String id){
        Book book = new Book();
        String sql = "SELECT * from book_products where book_id='"+id+"'";
        CachedRowSetImpl crst = DButil.execQuery(sql);
        int i  = 0;
        try{
            while(crst.next()){
                book.setId(crst.getString("book_id"));
                book.setName(crst.getString("book_name"));
                book.setPrice(crst.getDouble("book_price"));
                book.setNum(crst.getInt("book_num"));
                book.setImgurl(crst.getString("book_imgurl"));
                book.setDesc(crst.getString("book_desc"));
                i = i + 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public Map<String,Object> allBook(String number){
        Map<String,Object> map = new HashMap<>();
        Map<String,String> list = new HashMap<>();
        int i = 0;
        String sql = "select * from book_products order by book_num desc limit "+number;
        CachedRowSet crst = DButil.execQuery(sql);
        try{
            while(crst.next()){
                list.put("book_id",crst.getString("book_id"));
                list.put("book_name",crst.getString("book_name"));
                list.put("book_price",crst.getString("book_price"));
                list.put("book_num",crst.getString("book_num"));
                list.put("book_imgurl",crst.getString("book_imgurl"));
                list.put("book_desc",crst.getString("book_desc"));
                map.put(String.valueOf(i),list);
                list = new HashMap<>();
                i = i + 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public static Map<String, Object> searchBook(String sql){
        Map<String, Object> map = new HashMap<>();
        Map<String, String> data = new HashMap<>();;
        CachedRowSetImpl crst = DButil.execQuery(sql);
        int i  = 0;
        try{
            while(crst.next()){
                data.put("book_id",crst.getString("book_id"));
                data.put("book_name",crst.getString("book_name"));
                data.put("book_price",crst.getString("book_price"));
                data.put("book_num",crst.getString("book_num"));
                data.put("book_imgurl",crst.getString("book_imgurl"));
                data.put("book_desc",crst.getString("book_desc"));
                map.put(String.valueOf(i),data);
                data = new HashMap<>();
                i = i + 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return map;
    }

}
