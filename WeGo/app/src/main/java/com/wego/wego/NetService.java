package com.wego.wego;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Wu on 2017/7/8.
 */
public class NetService {

    /**
     * 登录验证
     * @param email
     * @param password
     * @return
     */
    public static boolean login(String email, String password) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/login";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("name", email);
        customer.put("password", NetService.encode(password));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            boolean res = jsonObject.getBoolean("status");
            if (res){
                System.out.printf("id is %d\n",jsonObject.getInt("id"));
                return true;
            }
            else return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
//    /**
//     * 发送GET请求
//     * @param path  请求路径
//     * @param params  请求参数
//     * @param ecoding 格式，通常为"UTF-8"
//     * @return
//     * @throws Exception
//     */
//    private static boolean sendLoginRequestByGet(String path, Map<String, String> params, String ecoding) throws Exception{
//        //?name=1233&password=abc
//        StringBuilder url = new StringBuilder(path);
//        url.append("?");
//        for(Map.Entry<String, String> map : params.entrySet()){
//            url.append(map.getKey()).append("=");
//            url.append(URLEncoder.encode(map.getValue(), ecoding));
//            url.append("&");
//        }
//        url.deleteCharAt(url.length()-1);
//        System.out.println(url);
//        HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
//        conn.setConnectTimeout(100000);
//        conn.setRequestMethod("Get");
//        conn.setReadTimeout(8000);
//        InputStream in = conn.getInputStream();
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(in));
//
//        StringBuilder builder = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            builder.append(line);// 一行行的读取内容并追加到builder中去
//        }
//        System.out.println(builder);
//        JSONObject jsonObject = new JSONObject(new String(builder));
//
//        boolean res = jsonObject.getBoolean("status");
//        if (res){
//            System.out.printf("id is %d\n",jsonObject.getInt("id"));
//            return true;
//        }
//        else return false;
//    }
    /**
     * 发送Post请求
     * @param path  请求路径
     * @param params  请求参数
     * @param ecoding 格式，通常为"UTF-8"
     * @return 返回的字符串，一般为json格式，需要解析
     * @throws Exception
     */
    private static String sendRequestByPost(String path, Map<String, String> params, String ecoding) throws Exception{
        StringBuilder url = new StringBuilder(path);
        System.out.println(url);

        HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(8000);
        conn.setUseCaches(false);
        byte[] data = getRequestData(params, ecoding).toString().getBytes();

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(data);

        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in));

        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);// 一行行的读取内容并追加到builder中去
        }
        System.out.println(builder);
        return new String(builder);
    }

    /**
     * 进行请求数据的封装
     * @param params  请求参数
     * @param encode 格式，通常为"UTF-8"
     * @return 封装好的数据
     * @throws Exception
     */
    private static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), encode))
                    .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 进行加密
     * @param string 待加密的明文
     * @return 加密好的密文
     */
    private static String encode(String string) throws NoSuchAlgorithmException {
        //以上对数据进行加密
//        MessageDigest sha = MessageDigest.getInstance("SHA");
//        sha.update(password.getBytes());
//        return new String(sha.digest());
        return string;
    }
}