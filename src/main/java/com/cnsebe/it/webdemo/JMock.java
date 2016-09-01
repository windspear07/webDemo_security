package com.cnsebe.it.webdemo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wind on 16/8/19.
 * 模拟工具类
 */
public class JMock {

    public static void main(String[] args) throws Exception{
        String salt = UUID.randomUUID().toString();
        pl(salt);

        String s = "1";
        pl(md5(s));
        pl(sha256(s));
    }

    //tools
    public static void pl(String s){
        System.out.println(s);
    }

    //生成salt值,并存储到内存中
    public static String gen_salt( String user ){
        String salt = UUID.randomUUID().toString();
        user_salt.put(user,salt);
        pl( "GEN: user=" + user + ",salt=" + salt );

        return salt;
    }

    //校验方法 p= sha256(  md5(password) + salt ) , m= sha256(  md5(特征码) + salt )
    public static String validate_pum(String user, String password_encrypt, String finger_encrypt){
        String salt = user_salt.get(user);
        String pwd_md5 = getPassword_encrypt(user);
        String pwd_sha = sha256( pwd_md5 + salt);
        List<String> fingers = get_fingers(user);

        //POSSIABLE fingers
        List<String> fingers_sha256 = new ArrayList<String>();
        for( String finger : fingers ){
            fingers_sha256.add( sha256( md5(finger) + salt ));
        }

        pl( "VALIDATE input :u="+user+",p=" + password_encrypt + ",m=" + finger_encrypt);
        pl( "VALIDATE inner: salt=" + salt );
        pl( "pwd_md5=" + pwd_md5 + ",pwd_sha=" + pwd_sha);
        for( String fs : fingers_sha256 ){
            pl( "finger=" + fs);
        }

        if( password_encrypt.equals(pwd_sha)){
            if( fingers_sha256.contains(finger_encrypt)){
                user_salt.remove(user);
                return  " login success ;)";
            }else{
                return "wrong client!";
            }
        }else{
            return "wrong password!";
        }
    }

    ///////////////////////vars
    public static Map<String,String> user_salt = new ConcurrentHashMap<String, String>();//动态salt，注意定期修改

    //////////////////////inner methods
    public static String getPassword_encrypt( String user ){
        //TODO 应该从数据库读取
        HashMap<String,String> user_pwd = new HashMap<String, String>();

        //todo 模拟密码。
        user_pwd.put("1",md5("123"));

        if( user_pwd.keySet().contains(user)){
            return user_pwd.get(user); //此处默认密码 1。
        }

        return "-1??";
    }
    public static List<String> get_fingers(String user ){
        //TODO 应该从数据库读取
        HashMap<String,List<String>> user_m = new HashMap<String, List<String>>();

        //TODO 模拟特征码。 想让客户端验证通过，此处要添加机器特征码
        List<String> user1 = new ArrayList<String>();
        user1.add("467acc037c33510f30561e4b8bafa8b790e97b6a6de8ad86f8443b83dae1e4c6");
        user1.add("9adfb6b491d8b33f052973e282f07d46c27af4c834f858df109608d3b32fe8ae");
        user_m.put("1",user1);

        if( user_m.keySet().contains(user)){
            return user_m.get(user);
        }

        return new ArrayList<String>();
    }

    ///encrypt alog
    public static String md5(String s){
        MessageDigest digest_md5 = null;
        try {
            digest_md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHex(digest_md5.digest(s.getBytes()));
    }

    public static String sha256(String s){
        MessageDigest digest_sha256 = null;
        try {
            digest_sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return byteArrayToHex(digest_sha256.digest(s.getBytes()));
    }

    public static String byteArrayToHex(byte[] byteArray){
        if( byteArray == null || byteArray.length == 0 )
            return "";

        char[] hexDigits={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] results = new char[byteArray.length*2];
        int index = 0;
        for( byte b : byteArray ){
            results[index++]=hexDigits[b>>>4 & 0xf];
            results[index++]=hexDigits[b & 0xf];
        }
        return new String(results);
    }
}
