package com.wego.wego.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wego.wego.Item.ItemList;
import com.wego.wego.R;

import java.security.PublicKey;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a4950 on 2017/7/14.
 */

public class HomepageFragment extends Fragment {
    private TextView logoInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_layout, container, false);
        logoInfo = (TextView)view.findViewById(R.id.LogoText);
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        System.out.println("OnResume!!");
        ItemList itemList = (ItemList)getActivity().getApplication();
        boolean isPay = itemList.getPay();
        System.out.println("tryPay: "+itemList.tryPay+"  isPay: "+isPay);
        if(itemList.tryPay && isPay){       //支付过，支付成功
            logoInfo.setText("支付成功:)");

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logoInfo.setText("欢迎光临");
                        }
                    });
                }
            };
            timer.schedule(timerTask,5000);
        }
        else if(itemList.tryPay && !isPay){     //支付过，支付失败
            logoInfo.setText("支付失败:(");

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logoInfo.setText("欢迎光临");
                        }
                    });
                }
            };
            timer.schedule(timerTask,5000);
        }
        else{
            logoInfo.setText("欢迎光临");
        }
    }

}
