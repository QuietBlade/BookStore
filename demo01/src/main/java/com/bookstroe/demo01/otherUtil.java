package com.bookstroe.demo01;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.DigestUtils;

import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class otherUtil {

    public static String timestamp(){
        Long time = new Date().getTime();
        String stamp = time.toString();
        return stamp.substring(0,10);
    }

    public static String parString(String str){
        if(str == null)
            return null;
        String Str;
        Str = str.replace("/","");
        Str = Str.replace("%20","");
        Str = Str.replace("'","");
        return Str;
    }

    public static boolean isConSpeCharacters(String str) {
        if(str == null)
            return false;
        String regEx = "[ _`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find(); //有特殊字符返回true ，没有字符返回false
    }

    public static String StringUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    public static boolean isEmail(String str){
        if (str == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(str);
        return m.matches();
    }

    public static void sendMail(String email,String activeCode) throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();//直接生产一个实例
        //String users[] = {"xxxxx@qq.com","xxxxx@126.com"};
        mailSender.setHost("smtp.163.com");//动态添加配置
        mailSender.setUsername("yuanzhangzcc@163.com");
        mailSender.setPassword("wy1185752791");
        mailSender.setProtocol("smtp");
        mailSender.setPort(25);
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true,"UTF-8");
        helper.setFrom(mailSender.getUsername());
        helper.setTo(email);
        helper.setSubject("在线书城 用户激活邮件");
        helper.setText(
                "<html><body>"+
                "<h1>欢迎注册在线书城系统</h1>"+
                "<h2>点击下面链接进行激活</h2> <p><a href=\"http://localhost:8080/api/user/activeCode?code="+ activeCode +"\">http://localhost:8080/api/user/activeCode?code="+ activeCode +"</a></p>" +
                        "</body></html>",true);
        try{
            mailSender.send(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Map<String, Object> getImageCode(int width, int height, int len, OutputStream os) {
        char mapTable[] = {
                '0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', '0', '1',
                '2', '3', '4', '5', '6', '7',
                '8', '9'};
        Map<String,Object> returnMap = new HashMap<String, Object>();
        if( width < 60 ) width = 60;
        if(height < 20 ) height = 20;
        if( len < 4) len = 4;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        // 随机产生168条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 168; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        //取随机产生的码
        String strEnsure = "";
        //4代表4位验证码,如果要生成更多位的认证码,则加大数值
        for (int i = 0; i < len; ++i) {
            strEnsure += mapTable[(int) (mapTable.length * Math.random())];
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            //直接生成
            String str = strEnsure.substring(i, i + 1);
            g.drawString(str, 13 * i + 6, 16);
        }
        // 释放图形上下文
        g.dispose();
        returnMap.put("image",image);
        returnMap.put("strEnsure",strEnsure);
        return returnMap;
    }

    public static Color getRandColor(int fc, int bc) {
        //给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public static String stringToMD5(String str) {
        str = "Book"+str;
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

}
