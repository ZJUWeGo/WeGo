package com.wego.wego_basket;

/**
 * Created by a4950 on 2017/7/10.
 */

public class Item {
    private String itemName;  //商品名
    private String itemNumber;  //商品编号
    private double itemPrice;  //商品价格
    private int itemQuantity;  //商品数量

    public Item(){
        itemName = "无";
        itemNumber = "0";
        itemPrice = 0;
        itemQuantity = 0;
    }

    public Item(String itemText){
        String[]item = itemText.split("-");
        itemName = item[0];
        itemNumber = item[1];
        itemPrice = Double.parseDouble(item[2]);
        itemQuantity = 1;
        //System.out.println(itemPrice+"!!!!");
    }

    public String getItemName(){
        return itemName;
    }
    public String getItemNumber(){
        return itemNumber;
    }
    public Double getItemPrice(){
        return itemPrice;
    }
    public int getItemQuantity(){
        return itemQuantity;
    }

    public void setItemQuantity(int num){
        itemQuantity = num;
    }

    @Override
    public boolean equals(Object x) {
        if (this == x) return true;
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Item s = (Item)x;
        if (!getItemNumber().equals(s.getItemNumber())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        String itemString = getItemName() + "-" + getItemNumber() + "-" + getItemPrice() + "*" + getItemQuantity();
        return itemString;
    }
}