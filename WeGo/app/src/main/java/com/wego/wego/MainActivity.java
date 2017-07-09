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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Runnable{
    private String name;

    public Handler activityHandler =  new Handler();

    private ListView listView;

    public void run() {//display the username

        Intent intent = getIntent();

        //TextView AccountName = (TextView) LayoutInflater.from(AccountActivity.this).inflate(R.layout.nav_header_account, null).findViewById(R.id.Account_name);

        TextView AccountName = (TextView)findViewById(R.id.textView4);
        //System.out.println(intent.getStringExtra("thisName"));
        name = intent.getStringExtra("thisName");
        Bundle bundle = intent.getExtras();
        String email = bundle.getString("email");
        String password = bundle.getString("password");
        int id = bundle.getInt("id");
        String myString = "\n" +"\n" + "\n" + "\n" + "    " + intent.getStringExtra("thisName");

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personInfo) {

            System.out.println("This is in personInfo, and my name is ");
            System.out.println(name);
            listView = (ListView)findViewById(R.id.mylistview);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getInfo()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    showInfo(arg2);
                }

            });


        } else if (id == R.id.nav_historyList) {


//            Intent i = new Intent(AccountActivity.this, mylistActivity.class);
//            startActivity(i);

            listView = (ListView)findViewById(R.id.mylistview);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    showItem(arg2);
                }

            });

        }
        else if (id == R.id.nav_home) {

            listView = (ListView)findViewById(R.id.mylistview);
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,clearData()));




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<String> getData(){

        //获取json数组解析出title

        List<String> data = new ArrayList<String>();
        data.add("2017-7-9 100.00");
        data.add("2017-7-8 100.00");
        data.add("2017-7-7 100.00");
        data.add("2017-7-6 100.00");
        return data;
    }

    private List<String> getInfo(){

        Intent intent = getIntent();
        String Account = intent.getStringExtra("thisName");

        String[] cards = {"400 800 8820","400 800 8821","400 800 8822","添加新银行卡"};


        List<String> data = new ArrayList<String>();
        data.add("账号: "+ Account);
        for (int i = 0; i < cards.length; i++) {
            if ( i < cards.length - 1)
                data.add("银行卡" + ( i + 1 ) + ": " + cards[i] );
            else
                data.add( cards[i] );
        }
        return data;
    }

    private List<String> clearData(){

        List<String> data = new ArrayList<String>();
        return data;
    }

    public void showInfo(int arg2){

        String[] cards = {"400 800 8820","400 800 8821","400 800 8822"," "};


        if (( arg2 != cards.length ) && ( arg2 != 0 ))  {

            final TextView tv = new TextView(this);
            SpannableString msp = new SpannableString(cards[arg2 - 1]);
            int length = cards[arg2 - 1].length();
            msp.setSpan(new RelativeSizeSpan(2.0f), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

            tv.setText(msp);
            final String deletedCard = cards[arg2 - 1];
            new AlertDialog.Builder(this)
                    .setTitle("银行卡删除")
                    .setView(tv)
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //发送删除请求
                            System.out.println("删除银行卡：" + deletedCard);


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
        else if ( arg2 != 0 ){
            final EditText tv = new EditText(this);
            tv.setText(" ");

            new AlertDialog.Builder(this)
                    .setTitle("银行卡添加")
                    .setView(tv)
                    .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //发送删除请求
                            System.out.println("添加银行卡：" + tv.getText().toString());


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
    public void showItem(int arg2)  {

        //获取json数组解析出订单内容

        String[] Items = {"牙膏  1.00￥  3","牙刷  1.00￥  3","牙缸  1.00￥  3"};

        new AlertDialog.Builder(this)
                .setTitle("订单详情")
                .setItems(Items, null)
                .setNegativeButton("确定", null)
                .show();

    }



}
