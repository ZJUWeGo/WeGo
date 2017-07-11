package com.wego.wego.Item;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Wu on 2017/7/11.
 */

public class ItemList {
    public ArrayList<Item> itemList;
    public ItemList(String itemList){
        this.itemList = new ArrayList<Item>();
        String[] items = itemList.substring(0,itemList.length() - 1).split("&");
        for (String item: items) {
            Item thisItem = new Item(item);
            System.out.print(thisItem);
            this.itemList.add(thisItem);
        }
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
