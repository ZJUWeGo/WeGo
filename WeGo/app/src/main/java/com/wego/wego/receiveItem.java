package com.wego.wego;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wego.wego.Item.ItemList;
import com.wego.wego.Item.ItemListAapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class receiveItem extends AppCompatActivity {


    private ListView mListViewArray;
    private View mPayFormView;
    private View mProgressView;

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

        mProgressView = findViewById(R.id.pay_progress);
        mPayFormView = findViewById(R.id.pay_form);

        final Button payBtn = (Button) findViewById(R.id.itemListPayBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("我要开始支付了！！！！！");
                showProgress(true);
                ExecutorService executorService= Executors.newCachedThreadPool();
                Callable<JSONObject> callable=new NetThread(6,bundle);
                Future future=executorService.submit(callable);
                try {
                    JSONObject jsonObject = (JSONObject) future.get(10000, TimeUnit.MILLISECONDS);//3s超时
                    System.out.println(jsonObject.getBoolean("status"));
                    showProgress(false);
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


    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mPayFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            mPayFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPayFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mPayFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
