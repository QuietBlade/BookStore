package com.bookstroe.demo01.dao;

import com.alibaba.fastjson.JSON;
import com.bookstroe.demo01.DButil;
import com.bookstroe.demo01.otherUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class NoticeDao {

    //这里的noti_title 和 noti_text 没有过滤

    //查询公告
    public static Map<String,Object> SelectNotice(int length, int page) throws SQLException {
            String sql = "select * from book_notice order by noti_id desc" + " limit "+ String.valueOf( (page-1) *length)  +","+ String.valueOf(length)+"";
            //System.out.println(sql);
            ResultSet res = DButil.execQuery(sql);
            if(res == null){
                return null;
            }
            Map<String,Object> map = new HashMap<>();
            try {
                int i = 0;
                while(res.next() && i < length){
                    Map<String,String> temp = new HashMap<>();
                    temp.put("noti_id",res.getString("noti_id"));
                    temp.put("noti_title",res.getString("noti_title"));
                    temp.put("noti_text",res.getString("noti_text"));
                    temp.put("noti_time",res.getString("noti_time"));
                    map.put(String.valueOf(i),temp);
                    i = i + 1;
                }
            }catch(SQLException e){
                e.printStackTrace();
                return null;
            }
            //conn.commit();
            return map;
        }

    //添加公告
    public static Map<String,String> InsertNotice(String noti_title,String noti_text,String noti_time ){
        Map<String,String> map = new HashMap<>();
        map.put("code","-1");
        map.put("msg","信息不能为空");
        if( noti_title == null || noti_text == null){
            return map;
        }else{
            noti_title = otherUtil.parString(noti_title);
            noti_text = otherUtil.parString(noti_text);
        }

        if( noti_time == null){
            noti_time = otherUtil.timestamp();
        }

        if(  noti_time.length() != 10){
            map.put("msg","时间戳错误");
            return map;
        }

        String sql = "insert into book_notice(noti_title,noti_text,noti_time) values('"+noti_title+"','"+noti_text+"','"+noti_time+"')";
        int res =  DButil.execUpdate(sql);
        if( res <= 0 ){
            map.put("msg","插入公告错误");
        }
        else{
            map.put("code","1");
            map.put("msg","添加成功");
        }
        return map;
    }

    //删除公告
    public static Map<String,String> DeleteNotice(String noti_id){
        Map<String,String> map = new HashMap<>();
        map.put("code","-1");
        map.put("msg","noti_id错误");
        if( noti_id == null ){
            return map;
        }

        if( otherUtil.isConSpeCharacters(noti_id) ){
            map.put("msg","id不能有特殊字符");
            return map;
        }

        String sql = "delete from book_notice where noti_id="+ noti_id +"";
        int res = DButil.execUpdate(sql);
        if( res < 1){
            map.put("msg","删除失败，没有这个ID");
        }else{
            map.put("code","1");
            map.put("msg","删除成功");
        }
        return map;
    }

    //更新公告
    public static Map<String,String> UpdateNotice(String id ,String noti_title,String noti_text,String noti_time){
        Map<String,String> map = new HashMap<>();
        map.put("code","-1");
        map.put("msg","信息不能为空");
        if( id == null || noti_title == null || noti_text == null || noti_time == null){
            return map;
        }
        if( otherUtil.isConSpeCharacters(id) || otherUtil.isConSpeCharacters(noti_time)){
            map.put("code","-1");
            map.put("msg","不能出现特殊字符");
            return map;
        }

        String sql = "update book_notice set noti_title='" +
                noti_title + "',noti_text='"+
                noti_text +"',noti_time='"+
                noti_time +"' where noti_id="+id;
        int res = DButil.execUpdate(sql);
        if( res < 1){
            map.put("msg","删除失败，没有这个ID");
        }else{
            map.put("code","1");
            map.put("msg","删除成功");
        }
        return map;
    }

}

