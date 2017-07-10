package com.wego.wego;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Runnable{
    private String email;
    private String password;
    private int id;

    public Handler activityHandler =  new Handler();

    private ListView listView;

    //display the username
    public void run() {

        Intent intent = getIntent();

        //TextView AccountName = (TextView) LayoutInflater.from(AccountActivity.this).inflate(R.layout.nav_header_account, null).findViewById(R.id.Account_name);

        TextView AccountName = (TextView)findViewById(R.id.textView4);
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        password = bundle.getString("password");
        id = bundle.getInt("id");

        String myString = "\n" +"\n" + "\n" + "\n" + "    " + email;
        AccountName.setText(myString);
        AccountName.invalidate();
        activityHandler.postDelayed(this, 1000);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //调用run函数
        activityHandler.post(MainActivity.this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //个人信息
        if (id == R.id.nav_personInfo) {

            System.out.println("This is in personInfo, and my name is ");
            System.out.println(email);
            listView = (ListView)findViewById(R.id.mylistview);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getInfo()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    showInfo(arg2);
                }

            });

        //历史订单
        } else if (id == R.id.nav_historyList) {


//            Intent i = new Intent(AccountActivity.this, mylistActivity.class);
//            startActivity(i);

            listView = (ListView)findViewById(R.id.mylistview);
            try {
                listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
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

        }
        //返回
        else if (id == R.id.nav_home) {

            listView = (ListView)findViewById(R.id.mylistview);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,clearData()));




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //获取历史订单列表
    private List<String> getData() throws InterruptedException, ExecutionException, TimeoutException {

        List<String> data = new ArrayList<String>();

        //获取json数组解析出title
        Bundle bundle = new Bundle();
        bundle.putInt("id",this.id);
        bundle.putString("password",this.password);

        ExecutorService executorService= Executors.newCachedThreadPool();
        Callable<JSONObject> callable=new NetThread(1,bundle);
        Future future=executorService.submit(callable);
        JSONObject jsonObject = (JSONObject) future.get(3000, TimeUnit.MILLISECONDS);//3s超时

        try {
            JSONArray list = jsonObject.getJSONArray("data");
            System.out.println(list);
            for ( int i = 0; i < list.length(); i ++){
                JSONObject temp = list.getJSONObject(i);

                String tempString = temp.get("order_time").toString() + "   "+ "金额："+ "￥" +  temp.get("order_price").toString()  + "   "+ temp.get("order_status").toString();

                data.add(tempString);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println("This is in main thread");
//        System.out.println(jsonObject);

        return data;
    }


    //获取银行卡信息
    private List<String> getInfo(){

        Intent intent = getIntent();
        String Account = intent.getStringExtra("thisName");

        List<String> data = new ArrayList<String>();

        //获取json数组解析出title
        Bundle bundle = new Bundle();
        bundle.putInt("id",this.id);
        bundle.putString("password",this.password);

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

                String tempString = "银行卡" + i + "：" + temp.get("card_id").toString();

                data.add(tempString);
            }
                data.add("添加新银行卡");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return data;
    }

    //清空界面
    private List<String> clearData(){

        List<String> data = new ArrayList<String>();
        return data;
    }

    //银行卡删除和添加操作
    public void showInfo(int arg2){

        final int Current_id = this.id;
        final String Current_password = this.password;


        List<String> cardsList = getInfo();

        String[] cards = new String[cardsList.size()];
        Object[] arr = cardsList.toArray();
        for (int i = 0; i < arr.length; i++) {
            String e = (String) arr[i];
            cards[i] = e;
        }


        if ( arg2 != cards.length - 1 )   {

            final TextView tv = new TextView(this);
            String thisString = "  " + cards[arg2].substring(5);
            SpannableString msp = new SpannableString(thisString);
            int length = thisString.length();
            msp.setSpan(new RelativeSizeSpan(2.0f), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

            tv.setText(msp);
            final String deletedCard = thisString;
            new AlertDialog.Builder(this)
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
                            bundle.putString("card_id",tv.getText().toString());


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

                                    Toast.makeText(MainActivity.this,"删除成功", Toast.LENGTH_SHORT).show();

                                }else {

                                    Toast.makeText(MainActivity.this,"删除失败", Toast.LENGTH_SHORT).show();

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


            TableLayout myTable = new TableLayout(this);


            final EditText tv1 = new EditText(this);
            tv1.setText("");
            final EditText tv2 = new EditText(this);
            tv2.setText("");

            String thisString = "卡号";
            String thatString = "联系电话";

            final TextView tv3 = new TextView(this);
            SpannableString msp = new SpannableString(thisString);
            int length = thisString.length();
            msp.setSpan(new RelativeSizeSpan(1.5f), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            tv3.setText(msp);


            final TextView tv4 = new TextView(this);
            SpannableString msp2 = new SpannableString(thatString);
            int length2 = thatString.length();
            msp2.setSpan(new RelativeSizeSpan(1.5f), 0, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp2.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            tv4.setText(msp2);



            myTable.addView(tv3);
            myTable.addView(tv1);
            myTable.addView(tv4);
            myTable.addView(tv2);

            new AlertDialog.Builder(this)
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

                                    Toast.makeText(MainActivity.this,"添加成功", Toast.LENGTH_SHORT).show();

                                    listView.invalidate();
                                }else {

                                    Toast.makeText(MainActivity.this,"添加失败", Toast.LENGTH_SHORT).show();

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

    //获取订单详情
    public void showItem(int arg2)  {

        List<String> data = new ArrayList<String>();
        //获取json数组解析出订单内容

        Bundle bundle = new Bundle();
        bundle.putInt("id",this.id);
        bundle.putString("password",this.password);
        bundle.putInt("order_id",arg2);

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

        new AlertDialog.Builder(this)
                .setTitle("订单详情")
                .setItems(Items, null)
                .setNegativeButton("确定", null)
                .show();

    }


    /**
     * unicode 转换成 utf-8
     * @author fanhui
     * 2007-3-15
     * @param theString
     * @return
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
