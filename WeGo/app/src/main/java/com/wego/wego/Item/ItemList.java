package com.wego.wego.Item;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wu on 2017/7/11.
 */

public class ItemList extends Application{

    public ArrayList<Item> itemList;

    private boolean isPay = false;
    public boolean tryPay = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void addItemList(String itemList){
        this.itemList = new ArrayList<Item>();
        this.tryPay = false;
        this.isPay = false;
        String[] items = itemList.substring(0,itemList.length() - 1).split("&");
        for (String item: items) {
            Item thisItem = new Item(item);
            System.out.print(thisItem);
            this.itemList.add(thisItem);
        }
    }

    public boolean getPay(){
        return isPay;
    }

    public void setPay(boolean status){
        isPay = status;
    }

    public double getTotalPrice(){
        double totalPrice = 0;
        for (Item item: itemList) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

    public JSONArray getJsonArray() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Item item: itemList) {
            jsonArray.put(item.getJson());
        }
        return jsonArray;
    }
}
