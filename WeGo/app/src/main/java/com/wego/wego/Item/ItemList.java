package com.wego.wego.Item;

import android.app.Application;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a4950 on 2017/7/9.
 */

public class ItemList extends Application {

    private List<Item> itemlist = new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void addItem(Item item) {
        if(itemlist.contains(item)){
            System.out.println(item.getItemName()+"already exists!!");

            Item existItem = itemlist.get(itemlist.indexOf(item));
            int num = existItem.getItemQuantity();
            num = num + 1;
            existItem.setItemQuantity(num);
            //System.out.println(existItem.getItemName()+"!!!"+existItem.getItemQuantity());
            Toast.makeText(getApplicationContext(), "添加成功!"+existItem.getItemName()+"共有"+existItem.getItemQuantity()+"件", Toast.LENGTH_SHORT).show();
        }
        else{
            //System.out.println("New item!!");
            Toast.makeText(getApplicationContext(), "添加成功!"+item.getItemName(), Toast.LENGTH_SHORT).show();
            itemlist.add(item);
        }
    }

    public void deleteItem(Item item) {
        if(itemlist.contains(item)){
            //System.out.println("找到了这个： "+ item.getItemName());
            Item existItem = itemlist.get(itemlist.indexOf(item));
            int num = existItem.getItemQuantity();
            if(num > 1) {
                num -= 1;
                existItem.setItemQuantity(num);
                //System.out.println(existItem.getItemName()+"!!!"+existItem.getItemQuantity());
                Toast.makeText(getApplicationContext(), "删除成功!"+item.getItemName()+"还剩"+existItem.getItemQuantity()+"件", Toast.LENGTH_SHORT).show();
            }
            else {
                //System.out.println("Goodbye, myfriend "+ item.getItemName());
                Toast.makeText(getApplicationContext(), "GG,Myfriend "+ item.getItemName(), Toast.LENGTH_SHORT).show();
                itemlist.remove(existItem);
            }
        }
        else {
            //System.out.println("兄弟，你没刷这个商品："+ item.getItemName());
            Toast.makeText(getApplicationContext(), "兄der，购物车中没有"+item.getItemName(), Toast.LENGTH_SHORT).show();
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

}
