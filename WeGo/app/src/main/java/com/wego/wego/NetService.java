package com.wego.wego;

import android.os.Bundle;

import org.json.JSONArray;
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
     * 发送订单列表
     * @param jsonArray
     * @return 是否成功
     */
    public static JSONObject sendItemList(int id, String password , String jsonArray) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/add-order";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        customer.put("itemList", jsonArray);
        System.out.println(customer);
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 登录验证
     * @param email
     * @param password
     * @return 是否成功
     */
    public static int login(String email, String password) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/login";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("name", email);
        customer.put("password", NetService.encode(password));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            boolean res = jsonObject.getBoolean("status");
            if (res) return jsonObject.getInt("id");
            else return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 请求历史订单
     * @param id
     * @param password
     * @return 返回JSON对象
     */
    public static JSONObject getHistoryList(int id, String password) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/order-list";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求订单详情
     * @param id
     * @param password
     * @return 返回JSON对象
     */
    public static JSONObject getListItems(int id, String password, int order_id) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/order-detail";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        customer.put("order_id", String.valueOf(order_id));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求银行卡号
     * @param id
     * @param password
     * @return 返回JSON对象
     */
    public static JSONObject getCards(int id, String password) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/card-list";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加银行卡号
     * @param id
     * @param password
     * @return 返回JSON对象
     */
    public static JSONObject addCards(int id, String password, String card_id, String phone_number) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/add-card";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        customer.put("card_id", String.valueOf(card_id));
        customer.put("phone_number", String.valueOf(phone_number));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除银行卡号
     * @param id
     * @param password
     * @return 返回JSON对象
     */
    public static JSONObject deleteCards(int id, String password, String card_id) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/remove-card";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("id", String.valueOf(id));
        customer.put("password", NetService.encode(password));
        customer.put("card_id", String.valueOf(card_id));
        try {
            JSONObject jsonObject = new JSONObject(sendRequestByPost(path, customer, "UTF-8"));

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取个人信息
     * @param id
     * @param password
     * @return 是否成功
     */
    public static boolean getPersonInfo(String id, String password) throws NoSuchAlgorithmException {
        String path = "http://101.200.42.170:5000/login";
        Map<String, String> customer = new HashMap<String, String>();
        customer.put("name", id);
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


