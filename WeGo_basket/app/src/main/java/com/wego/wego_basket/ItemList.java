package com.wego.wego_basket;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a4950 on 2017/7/9.
 */

public class ItemList extends Application {
    //private boolean is_active_;
    private List<Item> itemlist = new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
        //setIsActive(true);
    }

    public void addItem(Item item) {
        if(itemlist.contains(item)){
            System.out.println(item.getItemName()+"already exists!!");

            Item existItem = itemlist.get(itemlist.indexOf(item));
            int num = existItem.getItemQuantity();
            num = num + 1;
            existItem.setItemQuantity(num);
            System.out.println(existItem.getItemName()+"!!!"+existItem.getItemQuantity());
        }
        else{
            System.out.println("New item!!");
            itemlist.add(item);
        }

    }

    public String getItem(){
        String allItem = "";
        Iterator<Item> it = itemlist.iterator();
        while(it.hasNext()){
            Item nextItem = it.next();
            allItem += nextItem.toString() + "&";
        }
        return allItem;
    }

    public void printItem(){
        Iterator<Item> it = itemlist.iterator();
        while(it.hasNext()) {
            Item nextItem = it.next();
            System.out.println("购物车：" + nextItem.getItemName()+" * "+nextItem.getItemQuantity());
        }
    }

//    public boolean getIsActive() {
//        return is_active_;
//    }
//
//    public void setIsActive(boolean is_active) {
//        is_active_ = is_active;
//    }
}
