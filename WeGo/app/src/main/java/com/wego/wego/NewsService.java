package com.wego.wego;

import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


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
        String path = "http://101.200.42.170:5000/login";
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
        //?name=1233&password=abc
        StringBuilder url = new StringBuilder(path);
        url.append("?");
        for(Map.Entry<String, String> map : student.entrySet()){
            url.append(map.getKey()).append("=");
            url.append(URLEncoder.encode(map.getValue(), ecoding));
            url.append("&");
        }
        url.deleteCharAt(url.length()-1);
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
        conn.setConnectTimeout(100000);
        conn.setRequestMethod("GET");
        conn.setReadTimeout(8000);
        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in));

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);// 一行行的读取内容并追加到builder中去
        }
        System.out.println(builder);
        JSONObject jsonObject = new JSONObject(new String(builder));

        boolean res = jsonObject.getBoolean("status");
        if (res){
            System.out.printf("id is %d\n",jsonObject.getInt("id"));
            return true;
        }
        else return false;
    }
    private static String encode(String password) throws NoSuchAlgorithmException {
        //以上对数据进行加密
//        MessageDigest sha = MessageDigest.getInstance("SHA");
//        sha.update(password.getBytes());
//        return new String(sha.digest());
        return password;
    }
}