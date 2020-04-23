package com.bookstroe.demo01.beans;
import com.bookstroe.demo01.beans.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface userDao {

    int add(Author author) throws SQLException;

    int update(Author author);

    int delete(String str);

    int execute(String str);

    Author findAuthor(String username,String email) throws SQLException;
}
