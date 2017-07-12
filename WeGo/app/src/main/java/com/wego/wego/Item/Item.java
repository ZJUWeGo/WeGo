package com.wego.wego.Item;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.Serializable;

/**
 * Created by Wu on 2017/7/11.
 */

public class Item {

    String name;
    int itemId;
    double price;
    int num;
    public Item(String item){
        String[] itemContent = item.split("-|\\*");
        this.name = itemContent[0];
        this.itemId = Integer.parseInt(itemContent[1]);
        this.price = Double.parseDouble(itemContent[2]);
        this.num = Integer.parseInt(itemContent[3]);
    }

    @Override
    public String toString() {
        return "This item is " + this.name + " and it's code is " + this.itemId + " and price and num is "+ this.price +" " + this.num + "\n";
    }
    public double getTotalPrice(){
        double totalPrice = price * num;
        return totalPrice;
    }

    public JSONObject getJson() throws JSONException {
        JSONStringer jsonStringer = new JSONStringer().object();
        jsonStringer.key("itemId").value(itemId);
        jsonStringer.key("num").value(num);
        JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStringer.endObject().toString()));
//        JSONArray json = new JSONArray();
//        json.put(0, itemId);
//        json.put(1, num);
        return jsonObject;
    }
}
