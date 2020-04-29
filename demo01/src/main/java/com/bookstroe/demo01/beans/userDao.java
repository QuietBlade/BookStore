package com.bookstroe.demo01.beans;
import com.bookstroe.demo01.beans.Author;
import java.sql.SQLException;


public interface userDao {

    int add(Author author) throws SQLException;

    int update(String str);

    int delete(String uuid);

    Author findAuthor(String username,String email) throws SQLException;
}
