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

    public static String Random(){
        //4位的随机数
        int num = (int)((Math.random()*9+1)*1000);
        return String.valueOf(num);
    }

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
        String regEx = "[ _`~!#$%^*()+=|{}':;'\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find(); //有特殊字符返回true ，没有字符返回false
    }

    public static boolean isDbSpeaceChar(String str){
        return false;
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

    public static int sendMail(String email,String title,String text) throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();//直接生产一个实例
        //String email[] = {"xxxxx@qq.com","xxxxx@126.com"}; //可以同时发送多个邮箱
        mailSender.setHost("smtp.163.com");//动态添加配置
        mailSender.setUsername("yuanzhangzcc@163.com");
        mailSender.setPassword("YJMEWHLTSIYRPDMK");
        mailSender.setProtocol("smtp");
        mailSender.setPort(25);
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true,"UTF-8");
        helper.setFrom(mailSender.getUsername());
        helper.setTo(email);
        helper.setSubject(title);
        helper.setText(text,true);
        try{
            mailSender.send(msg);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 1;
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

    public static boolean isNumber(String number){
        try {
            Integer.valueOf(number);
            Double.valueOf(number);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Map<String,String> errorMessage(String code){
        Map<String,String> map = new HashMap<>();
        map.put("code",code);
        map.put("msg","id不能为空");
        if( code == null){ return map; }
        switch( code ){
            case "1":map.put("msg","操作成功");break;
//            case "1":map.put("msg","");break;
//            case "1":map.put("msg","");break;
//            case "1":map.put("msg","");break;
//            case "1":map.put("msg","");break;
//            case "1":map.put("msg","注册成功");break;
//            case "1":map.put("msg","公告添加成功");break;
//            case "1":map.put("msg","公告已修改");break;
//            case "1":map.put("msg","用户已激活");break;
//            case "1":map.put("msg","公告已删除");break;
//            case "1":map.put("msg","密码已修改");break;
            case "0":map.put("msg","数据库异常");break;
            case "-1":map.put("msg","_token异常");break;
            case "-2":map.put("msg","添加公告失败");break;
            case "-3":map.put("msg","邮件发送失败");break;
            case "-4":map.put("msg","添加书籍失败");break;
            case "-5":map.put("msg","添加用户失败");break;
            case "-6":map.put("msg","修改密码失败");break;
            case "-7":map.put("msg","用户激活失败");break;
            case "-8":map.put("msg","删除公告失败");break;
            case "-9":map.put("msg","修改公告失败");break;
            case "-10":map.put("msg","用户名错误");break;
            case "-11":map.put("msg","用户名不能有特殊字符");break;
            case "-12":map.put("msg","用户名长度不足");break;
            case "-13":map.put("msg","用户名不能为空");break;
            case "-14":map.put("msg","用户未激活");break;
            case "-15":map.put("msg","用户名已存在");break;
            case "-16":map.put("msg","当前未登录");break;
            case "-17":map.put("msg","权限不足");break;
            case "-18":map.put("msg","用户成功注册");break;
            case "-19":map.put("msg","");break;
            case "-20":map.put("msg","密码错误");break;
            case "-21":map.put("msg","密码不能有特殊字符");break;
            case "-22":map.put("msg","密码长度不足");break;
            case "-23":map.put("msg","密码不能为空");break;
            case "-24":map.put("msg","旧密码错误");break;
            case "-25":map.put("msg","用户名或密码错误");break;
            case "-26":map.put("msg","用户名或密码不能有特殊字符");break;
            case "-27":map.put("msg","用户名或密码不能为空");break;
            case "-28":map.put("msg","用户名或密码长度不足");break;
            case "-29":map.put("msg","两次密码不正确");break;
            case "-30":map.put("msg","邮箱错误");break;
            case "-31":map.put("msg","邮箱格式不正确");break;
            case "-32":map.put("msg","邮箱不能有特殊字符");break;
            case "-33":map.put("msg","邮箱不能为空");break;
            case "-34":map.put("msg","邮箱已存在");break;
            case "-35":map.put("msg","用户名或邮箱不存在");break;
            case "-36":map.put("msg","");break;
            case "-37":map.put("msg","");break;
            case "-38":map.put("msg","");break;
            case "-39":map.put("msg","");break;
            case "-40":map.put("msg","验证码错误");break;
            case "-41":map.put("msg","验证码不能为空");break;
            case "-42":map.put("msg","验证码不能有特殊字符");break;
            case "-43":map.put("msg","已向您的邮箱发送验证码,请注意查收");break;
            case "-44":map.put("msg","邮箱验证码错误");break;
            case "-45":map.put("msg","激活码错误");break;
            case "-46":map.put("msg","");break;
            case "-47":map.put("msg","");break;
            case "-48":map.put("msg","");break;
            case "-49":map.put("msg","");break;
            case "-50":map.put("msg","公告标题不能为空");break;
            case "-51":map.put("msg","公告内容不能为空");break;
            case "-52":map.put("msg","公告时间戳错误");break;
            case "-53":map.put("msg","公告标题不能有特殊字符");break;
            case "-54":map.put("msg","公告ID异常");break;
            case "-55":map.put("msg","公告信息不能为空");break;
            case "-56":map.put("msg","");break;
            case "-57":map.put("msg","");break;
            case "-58":map.put("msg","");break;
            case "-59":map.put("msg","");break;
            case "-60":map.put("msg","书名不能为空");break;
            case "-61":map.put("msg","价格不能为空");break;
            case "-62":map.put("msg","数量不能为空");break;
            case "-63":map.put("msg","描述不能为空");break;
            case "-64":map.put("msg","价格或数量必须是数字");break;
            case "-65":map.put("msg","书名不能有特殊字符");break;
        }
        return map;
    }

    //md5函数加盐
    public static String stringToMD5(String str) {
        str = "Book"+str;
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

}
