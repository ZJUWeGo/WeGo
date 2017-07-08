package com.wego.wego;

import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Created by Wu on 2017/7/8.
 */
public class NewsService {
    /**
     * 登录验证
     * @param email
     * @param password
     * @return
     */
    public static boolean save(String email, String password) throws NoSuchAlgorithmException {
        String path = "http://192.168.1.104:8080/Register/ManageServlet";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("name", email);
        customer.put("password", NewsService.encode(password));
        try {
            return SendGETRequest(path, customer, "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 发送GET请求
     * @param path  请求路径
     * @param student  请求参数
     * @return 请求是否成功
     * @throws Exception
     */
    private static boolean SendGETRequest(String path, Map<String, String> student, String ecoding) throws Exception{
        // http://127.0.0.1:8080/Register/ManageServlet?name=1233&password=abc
        StringBuilder url = new StringBuilder(path);
        url.append("?");
        for(Map.Entry<String, String> map : student.entrySet()){
            url.append(map.getKey()).append("=");
            url.append(URLEncoder.encode(map.getValue(), ecoding));
            url.append("&");
        }
        url.deleteCharAt(url.length()-1);
        System.out.println(url);
        HttpsURLConnection conn = (HttpsURLConnection)new URL(url.toString()).openConnection();
        conn.setConnectTimeout(100000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            return true;
        }
        return false;
    }
    private static String encode(String password) throws NoSuchAlgorithmException {
        //以上对数据进行加密
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(password.getBytes());
        return new String(sha.digest());
    }
}