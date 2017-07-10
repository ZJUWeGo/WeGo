package com.wego.wego;

import android.os.Bundle;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

/**
 * Created by Wu on 2017/7/10.
 */

public class NetThread implements Callable<JSONObject> {
    private int type;
    private Bundle bundle;
    private JSONObject jsonObject;

    NetThread(int type, Bundle bundle) {
        this.type = type;
        this.bundle = bundle;
    }

    @Override
    public JSONObject call() throws Exception {

        switch (this.type){
            case 1:
                try {
                    jsonObject = NetService.getHistoryList(bundle.getInt("id"),bundle.getString("password"));
//                    System.out.print("this is in NetTread, -----");
//                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    jsonObject = NetService.getListItems(bundle.getInt("id"),bundle.getString("password"),bundle.getInt("order_id"));
//                    System.out.print("this is in NetTread, -----");
//                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                try {
                    jsonObject = NetService.getCards(bundle.getInt("id"),bundle.getString("password"));
//                    System.out.print("this is in NetTread, -----");
//                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    jsonObject = NetService.addCards(bundle.getInt("id"),bundle.getString("password"),bundle.getString("card_id"),bundle.getString("phone_number"));
//                    System.out.print("this is in NetTread, -----");
//                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    jsonObject = NetService.deleteCards(bundle.getInt("id"),bundle.getString("password"),bundle.getString("card_id"));
//                    System.out.print("this is in NetTread, -----");
//                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
        return jsonObject;
    }
}