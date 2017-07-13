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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class receiveItem extends AppCompatActivity {


    private ListView mListViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_item);

        final Bundle bundle = this.getIntent().getExtras();
        System.out.println("Bundle！！！！！"+bundle);
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
                ExecutorService executorService= Executors.newCachedThreadPool();
                Callable<JSONObject> callable=new NetThread(6,bundle);
                Future future=executorService.submit(callable);
                try {
                    JSONObject jsonObject = (JSONObject) future.get(10000, TimeUnit.MILLISECONDS);//3s超时
                    System.out.println(jsonObject.getBoolean("status"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
