package com.bookstroe.demo01.dao;
import com.bookstroe.demo01.beans.Book;
import com.bookstroe.demo01.beans.bookDao;
import org.springframework.stereotype.Repository;

@Repository
public class BookDao implements bookDao {
    @Override
    public int add(Book book){
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
