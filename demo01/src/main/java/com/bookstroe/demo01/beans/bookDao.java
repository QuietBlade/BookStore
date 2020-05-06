package com.bookstroe.demo01.beans;

import java.sql.SQLException;
import com.bookstroe.demo01.beans.Author;
import java.sql.SQLException;

public interface bookDao {

        int add(Book book);

        int update(Book book);

        int delete(String bid);

        Book  findBook(String id);

}
