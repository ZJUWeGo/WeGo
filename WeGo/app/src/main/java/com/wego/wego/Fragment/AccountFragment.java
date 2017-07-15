package com.wego.wego.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wego.wego.Adapter.AccountCardListAdapter;
import com.wego.wego.MainActivity;
import com.wego.wego.NetThread;
import com.wego.wego.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by a4950 on 2017/7/14.
 */

public class AccountFragment extends Fragment {

    private List<String> mData;
    private ListView listView;
    private Activity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_cardlist_layout, container, false);
        mainActivity = getActivity();

        listView = (ListView)view.findViewById(R.id.accountCardList);
        mData = getInfo();
        AccountCardListAdapter accountCardListAdapter = new AccountCardListAdapter(inflater,mData);

        listView.setAdapter(accountCardListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showInfo(arg2);
            }
        });

        return view;
    }

    //获取银行卡信息
    private List<String> getInfo(){

//        title = (TextView) findViewById(R.id.myTitle);
//        title.setText(" ");

        Intent intent = mainActivity.getIntent();
        String Account = intent.getStringExtra("thisName");

        List<String> data = new ArrayList<String>();

        //获取json数组解析出title
        Bundle bundle = new Bundle();
        bundle.putInt("id",((MainActivity)mainActivity).getID());
        bundle.putString("password",((MainActivity)mainActivity).getPassword());

        ExecutorService executorService= Executors.newCachedThreadPool();
        Callable<JSONObject> callable=new NetThread(3,bundle);
        Future future=executorService.submit(callable);
        JSONObject jsonObject = null;//3s超时
        try {
            jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        try {
            JSONArray list = jsonObject.getJSONArray("data");
            System.out.println(list);
            for ( int i = 0; i < list.length(); i ++){
                JSONObject temp = list.getJSONObject(i);

                String tempString = "银行卡" + i + ":" + temp.get("card_id").toString();

                data.add(tempString);
            }
            data.add("添加新银行卡");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    //银行卡删除和添加操作
    public void showInfo(int arg2){

        final int Current_id = ((MainActivity)mainActivity).getID();
        final String Current_password = ((MainActivity)mainActivity).getPassword();

        List<String> cardsList = getInfo();

        String[] cards = new String[cardsList.size()];
        Object[] arr = cardsList.toArray();
        for (int i = 0; i < arr.length; i++) {
            String e = (String) arr[i];
            cards[i] = e;
        }


        if ( arg2 != cards.length - 1 )   {

            final TextView tv = new TextView(mainActivity);
            String thisString = "  " + cards[arg2].substring(5);
            SpannableString msp = new SpannableString(thisString);
            int length = thisString.length();
            msp.setSpan(new RelativeSizeSpan(2.0f), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

            tv.setText(msp);
            final String deletedCard = cards[arg2].substring(5);
            new AlertDialog.Builder(mainActivity)
                    .setTitle("银行卡删除")
                    .setView(tv)
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //发送删除请求
                            System.out.println("删除银行卡：" + deletedCard);

                            Bundle bundle = new Bundle();
                            bundle.putInt("id",Current_id);
                            bundle.putString("password",Current_password);
                            bundle.putString("card_id",deletedCard);


                            ExecutorService executorService= Executors.newCachedThreadPool();
                            Callable<JSONObject> callable=new NetThread(5,bundle);
                            Future future=executorService.submit(callable);
                            JSONObject jsonObject = null;//3s超时
                            try {
                                jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                            //提示删除成功
                            try {
                                System.out.println(jsonObject.get("status").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (jsonObject.get("status").toString() == "true"){

                                    //listView.invalidate();

                                    Toast.makeText(mainActivity,"删除成功", Toast.LENGTH_SHORT).show();

                                }else {

                                    Toast.makeText(mainActivity,"删除失败", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();//关闭消息框
                        }
                    })
                    .show();
        }
        else {


            TableLayout myTable = new TableLayout(mainActivity);


            final EditText tv1 = new EditText(mainActivity);
            tv1.setText("");
            final EditText tv2 = new EditText(mainActivity);
            tv2.setText("");

            String thisString = "卡号";
            String thatString = "联系电话";

            final TextView tv3 = new TextView(mainActivity);
            SpannableString msp = new SpannableString(thisString);
            int length = thisString.length();
            msp.setSpan(new RelativeSizeSpan(1.5f), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            tv3.setText(msp);


            final TextView tv4 = new TextView(mainActivity);
            SpannableString msp2 = new SpannableString(thatString);
            int length2 = thatString.length();
            msp2.setSpan(new RelativeSizeSpan(1.5f), 0, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp2.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            tv4.setText(msp2);



            myTable.addView(tv3);
            myTable.addView(tv1);
            myTable.addView(tv4);
            myTable.addView(tv2);

            new AlertDialog.Builder(mainActivity)
                    .setTitle("银行卡添加")
                    .setView(myTable)
                    .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //发送添加请求
                            System.out.println("添加银行卡：" + tv1.getText().toString());
                            System.out.println("联系电话：" + tv2.getText().toString());


                            Bundle bundle = new Bundle();
                            bundle.putInt("id",Current_id);
                            bundle.putString("password",Current_password);
                            bundle.putString("card_id",tv1.getText().toString());
                            bundle.putString("phone_number",tv2.getText().toString());


                            ExecutorService executorService= Executors.newCachedThreadPool();
                            Callable<JSONObject> callable=new NetThread(4,bundle);
                            Future future=executorService.submit(callable);
                            JSONObject jsonObject = null;//3s超时
                            try {
                                jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                            //提示添加成功
                            try {
                                System.out.println(jsonObject.get("status").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (jsonObject.get("status").toString() == "true"){

                                    Toast.makeText(mainActivity,"添加成功", Toast.LENGTH_SHORT).show();

                                    listView.invalidate();
                                }else {

                                    Toast.makeText(mainActivity,"添加失败", Toast.LENGTH_SHORT).show();

                                    listView.invalidate();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();//关闭消息框
                        }
                    })
                    .show();
        }

    }

}
