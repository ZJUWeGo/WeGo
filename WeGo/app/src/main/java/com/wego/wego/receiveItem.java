package com.wego.wego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wego.wego.Item.Item;
import com.wego.wego.Item.ItemList;
import com.wego.wego.Item.ItemListAapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class receiveItem extends AppCompatActivity {


    private ListView mListViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_item);

        ItemList itemList = (ItemList)getApplication();

        mListViewArray = (ListView)findViewById(R.id.itemListView);
        LayoutInflater inflater = getLayoutInflater();

        ItemListAapter adapter = new ItemListAapter(inflater,itemList.itemList);
        mListViewArray.setAdapter(adapter);

        TextView itemListPrice = (TextView)findViewById(R.id.itemListPrice);
        itemListPrice.setText(Double.toString(itemList.getTotalPrice()));

        final Button payBtn = (Button) findViewById(R.id.itemListPayBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("我要开始支付了！！！！！");
            }
        });


    }
}
