package com.wego.wego.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wego.wego.R;

import java.util.List;


/**
 * Created by a4950 on 2017/7/12.
 */

public class ItemListAapter extends BaseAdapter {
    private List<Item> mItemList;
    private LayoutInflater mInflater;

    public ItemListAapter(LayoutInflater inflater,List<Item> data){
        mItemList = data;
        mInflater = inflater;
    }

    @Override
    public int getCount(){
        return mItemList.size();
    }
    @Override
    public Object getItem(int position){
        return mItemList.get(position);
    }
    @Override
    public long getItemId(int position){
        return  position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup){
        View viewItem = mInflater.inflate(R.layout.receive_itemlist,null);
        Item item = mItemList.get(position);
        TextView name = (TextView)viewItem.findViewById(R.id.itemName);
        TextView itemId = (TextView)viewItem.findViewById(R.id.itemID);
        TextView price = (TextView)viewItem.findViewById(R.id.itemPrice);
        TextView num = (TextView)viewItem.findViewById(R.id.itemNum);
        name.setText(item.name);
        itemId.setText(Integer.toString(item.itemId));
        price.setText(Double.toString(item.price));
        num.setText(Integer.toString(item.num));
        return viewItem;
    }


}
