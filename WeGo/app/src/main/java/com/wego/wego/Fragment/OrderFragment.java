package com.wego.wego.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wego.wego.Adapter.AccountCardListAdapter;
import com.wego.wego.Adapter.HistoryOrderListAdapter;
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

import static com.wego.wego.MainActivity.unicodeToUtf8;

/**
 * Created by a4950 on 2017/7/14.
 */

public class OrderFragment extends Fragment {
    private ListView listView;
    private Activity mainActivity;
    private List<String> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_order_layout, container, false);
        listView = (ListView)view.findViewById(R.id.historyOrderList);
        mainActivity = getActivity();
        try {
                //listView.setAdapter(new ArrayAdapter<String>(mainActivity, android.R.layout.simple_expandable_list_item_1,getData()));

            listView = (ListView)view.findViewById(R.id.historyOrderList);
            mData = getData();
            HistoryOrderListAdapter historyOrderListAdapter = new HistoryOrderListAdapter(inflater,mData);

            listView.setAdapter(historyOrderListAdapter);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("out of time");
                e.printStackTrace();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    showItem(arg2);
                }

            });
        return view;
    }


    //获取历史订单列表
    private List<String> getData() throws InterruptedException, ExecutionException, TimeoutException {

        //title = (TextView) findViewById(R.id.myTitle);
        //title.setText("            订单时间                                 金额           状态");

        List<String> data = new ArrayList<String>();



        //获取json数组解析出title
        Bundle bundle = new Bundle();
        bundle.putInt("id",((MainActivity)mainActivity).getID());
        bundle.putString("password",((MainActivity)mainActivity).getPassword());

        ExecutorService executorService= Executors.newCachedThreadPool();
        Callable<JSONObject> callable=new NetThread(1,bundle);
        Future future=executorService.submit(callable);
        JSONObject jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);//3s超时

        try {
            JSONArray list = jsonObject.getJSONArray("data");
            //System.out.println(list);
            for ( int i = 0; i < list.length(); i ++){
                JSONObject temp = list.getJSONObject(i);

//                int tempLength = 5 - temp.get("order_price").toString().length();
//                //System.out.println(tempLength);
//                StringBuffer spaceString = new StringBuffer(" ");
//                while (tempLength > 0) {
//                    spaceString.append("   ");
//                    --tempLength;
//                }
                //System.out.println("spaceString length:"+spaceString.length());
                String tempString = temp.get("order_time").toString() + "&" +  temp.get("order_price").toString() + "&"+ temp.get("order_status").toString();

                data.add(tempString);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println("This is in main thread");
//        System.out.println(jsonObject);

        return data;
    }

    //获取订单详情
    public void showItem(int arg2)  {


        List<String> historyIDList = null;
        try {
            historyIDList = getListID();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        List<String> data = new ArrayList<String>();
        //获取json数组解析出订单内容

        Bundle bundle = new Bundle();
        bundle.putInt("id",((MainActivity)mainActivity).getID());
        bundle.putString("password",((MainActivity)mainActivity).getPassword());
        bundle.putInt("order_id",Integer.parseInt(historyIDList.get(arg2)));
        System.out.println(arg2);

        ExecutorService executorService= Executors.newCachedThreadPool();
        Callable<JSONObject> callable=new NetThread(2,bundle);
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

                String unicodeString = temp.get("item_name").toString();

                String utf8String = unicodeToUtf8(unicodeString);

                System.out.println(utf8String);

                String tempString = utf8String + "   "+ "数量："+  temp.get("item_num").toString();

                data.add(tempString);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] Items = new String[data.size()];
        Object[] arr = data.toArray();
        for (int i = 0; i < arr.length; i++) {
            String e = (String) arr[i];
            Items[i] = e;
        }

        //String[] Items = {"牙膏  1.00￥  3","牙刷  1.00￥  3","牙缸  1.00￥  3"};

        new AlertDialog.Builder(mainActivity)
                .setTitle("订单详情")
                .setItems(Items, null)
                .setNegativeButton("确定", null)
                .show();

    }


    private List<String> getListID() throws InterruptedException, ExecutionException, TimeoutException {

        List<String> data = new ArrayList<String>();

        //获取json数组解析出title
        Bundle bundle = new Bundle();
        bundle.putInt("id",((MainActivity)mainActivity).getID());
        bundle.putString("password",((MainActivity)mainActivity).getPassword());

        ExecutorService executorService= Executors.newCachedThreadPool();
        Callable<JSONObject> callable=new NetThread(1,bundle);
        Future future=executorService.submit(callable);
        JSONObject jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);//3s超时

        try {
            JSONArray list = jsonObject.getJSONArray("data");
            System.out.println(list);
            for ( int i = 0; i < list.length(); i ++){
                JSONObject temp = list.getJSONObject(i);
                data.add(temp.getString("order_id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println("This is in main thread");
//        System.out.println(jsonObject);

        return data;
    }
}
