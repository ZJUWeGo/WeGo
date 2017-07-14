package com.wego.wego;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.wego.wego.Base.BaseNfcActivity;
import com.wego.wego.Fragment.AccountFragment;
import com.wego.wego.Fragment.HomepageFragment;
import com.wego.wego.Fragment.OrderFragment;
import com.wego.wego.Item.ItemList;


import java.util.Arrays;


public class MainActivity extends BaseNfcActivity
        implements NavigationView.OnNavigationItemSelectedListener, Runnable{
    private String email;
    private String password;
    private int id;

    public Handler activityHandler =  new Handler();

    DrawerLayout drawer;


    private OrderFragment orderFragment;
    private AccountFragment accountFragment;
    private HomepageFragment homepageFragment;

    private FragmentManager fm;

    //display the username
    public void run() {

        Intent intent = getIntent();

        //这行代码不许改！
        TextView AccountName = (TextView)findViewById(R.id.Account_name);
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //首先加载homepage fragment
        fm = getFragmentManager();
        //FragmentManager.enableDebugLogging(true);
        FragmentTransaction transaction = fm.beginTransaction();
        System.out.println("new homepageFragment!!!!!!!!");
        homepageFragment = new HomepageFragment();
        transaction.add(R.id.main_layout,homepageFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment ft = (Fragment) getFragmentManager().findFragmentById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(ft instanceof HomepageFragment){
            moveTaskToBack(false);
        }
        else {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.main_layout,homepageFragment);
            transaction.commit();
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

        FragmentTransaction transaction = fm.beginTransaction();

        //个人信息
        if (id == R.id.nav_personInfo) {
            System.out.println("This is in personInfo, and my name is "+ email);

            if(accountFragment == null){
                System.out.println("new accountFragment!");
                accountFragment = new AccountFragment();
            }
            transaction.replace(R.id.main_layout,accountFragment);
            transaction.commit();

            //System.out.println("replace accountFragment!!!");

        //历史订单
        } else if (id == R.id.nav_historyList) {

            if(orderFragment == null){
                orderFragment = new OrderFragment();
            }
            transaction.replace(R.id.main_layout,orderFragment);
            transaction.commit();
            //System.out.println("replace orderFragment    !!!");
        }

        //返回
        else if (id == R.id.nav_home) {
            if(homepageFragment == null){
                homepageFragment = new HomepageFragment();
            }
            transaction.replace(R.id.main_layout,homepageFragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    private String mTagText;

    @Override
    public void onNewIntent(Intent intent){
        //获取Tag对象
        mTagText = "";
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取Ndef的实例
        Ndef ndef = Ndef.get(detectedTag);
        readNfcTag(intent);
        System.out.println("收到了！！！！！！"+mTagText);

        //mTagText = "神奇笔记本-2001-10.56*2&罗技G502-101-399.5*1&";
        ItemList itemList = (ItemList)getApplication();
        if(mTagText == ""){
            Toast.makeText(this, "购物篮中未发现商品!", Toast.LENGTH_SHORT).show();
        }
        else{
            itemList.addItemList(mTagText);
            Bundle bundle = new Bundle();
            bundle.putInt("id",this.id);
            bundle.putString("password",this.password);
            Intent intent_receiveItem = new Intent(MainActivity.this,receiveItem.class);
            intent_receiveItem.putExtras(bundle);
            startActivity(intent_receiveItem);
        }

    }

    private void readNfcTag(Intent intent){
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if(rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for(int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    contentSize += msgs[i].toByteArray().length;
                }
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    String textRecord = parseTextRecord(record);
                    mTagText += textRecord;
                }
            } catch (Exception e) {
            }
        }

    }


    /**
     * 解析NDEF文本数据，从第三个字节开始，后面的文本数据
     * @param ndefRecord
     * @return
     */
    public static String parseTextRecord(NdefRecord ndefRecord) {
        /**
         * 判断数据是否为NDEF格式
         */
        //判断TNF
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //判断可变的长度的类型
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //获得字节数组，然后进行分析
            byte[] payload = ndefRecord.getPayload();
            //下面开始NDEF文本数据第一个字节，状态字节
            //判断文本是基于UTF-8还是UTF-16的，取第一个字节"位与"上16进制的80，16进制的80也就是最高位是1，
            //其他位都是0，所以进行"位与"运算后就会保留最高位
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f最高两位是0，第六位是1，所以进行"位与"运算后获得第六位
            int languageCodeLength = payload[0] & 0x3f;
            //下面开始NDEF文本数据第二个字节，语言编码
            //获得语言编码
            String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            //下面开始NDEF文本数据后面的字节，解析出文本
            String textRecord = new String(payload, languageCodeLength + 1,
                    payload.length - languageCodeLength - 1, textEncoding);
            return textRecord;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /*设置app按返回键不退出，而是移至后台*/
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            if (drawer.isDrawerOpen(GravityCompat.START)) {
//                drawer.closeDrawer(GravityCompat.START);
//            }
//            else{
//                moveTaskToBack(false);
//                return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public String getEmail(){
        return email;
    }

    public int getID(){
        return id;
    }

    public String getPassword(){
        return password;
    }

}

