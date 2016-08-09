package com.cnsebe.it.webdemo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wind on 16/8/9.
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

    ////生成salt值,并存储到内存中
    public static String gen_salt( String user ){
        String salt = UUID.randomUUID().toString();
        user_salt.put(user,salt);

        pl( "GEN: user=" + user + ",salt=" + salt );

        return salt;
    }

    //校验方法 p= sha256(  md5(password) + salt ) , m= sha256(  md5(mac) + salt )
    public static String validate_pum(String user, String password_encrypt, String mac_encrypt){
        String salt = user_salt.get(user);
        String pwd_md5 = getPassword_encrypt(user);
        String pwd_sha = sha256( pwd_md5 + salt);
        String mac_md5 = get_mac_encrypt(user);
        String mac_sha = sha256( mac_md5+ salt );

        pl( "VALIDATE input :u="+user+",p=" + password_encrypt + ",m=" + mac_encrypt);
        pl( "VALIDATE inner: salt=" + salt );
        pl( "pwd_md5=" + pwd_md5 + ",pwd_sha=" + pwd_sha);
        pl( "mac_md5=" + mac_md5 + ",mac_sha=" + mac_sha);

        if( password_encrypt.equals(pwd_sha)){
            if( mac_encrypt.equals(mac_sha)){
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
    public static Map<String,String> user_salt = new ConcurrentHashMap<String, String>();

    //////////////////////inner methods
    public static String getPassword_encrypt(String user ){
        //TODO 应该从数据库读取
        return "c4ca4238a0b923820dcc509a6f75849b"; //此处默认密码 1。
    }
    public static String get_mac_encrypt(String user ){
        //TODO 应该从数据库读取
        return md5("9801a7a826f5");//此处默认了一个mac地址。
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
