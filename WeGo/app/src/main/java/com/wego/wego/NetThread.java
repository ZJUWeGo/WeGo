package com.wego.wego;

import android.os.Bundle;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Wu on 2017/7/10.
 */

public class NetThread extends Thread {
    private int type;
    private Bundle bundle;


    public NetThread(int type, Bundle bundle) {
        this.type = type;
        this.bundle = bundle;
    }

    public void run() {
        switch (this.type){
            case 1:
                try {
                    JSONObject jsonObject = NetService.getHistoryList(bundle.getInt("id"),bundle.getString("password"));
                    System.out.println(jsonObject);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}