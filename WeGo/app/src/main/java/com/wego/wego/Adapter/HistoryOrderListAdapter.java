package com.wego.wego.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wego.wego.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by a4950 on 2017/7/15.
 */

public class HistoryOrderListAdapter extends BaseAdapter {
    private List<String> mOrderList;
    private LayoutInflater mInflater;
    private int payStateColor;       //0为未支付，1为已支付

    public HistoryOrderListAdapter(LayoutInflater inflater,List<String> data){
        mOrderList = data;
        mInflater = inflater;
        payStateColor = 0;
    }
    @Override
    public int getCount(){
        return mOrderList.size();
    }
    @Override
    public Object getItem(int position){
        return mOrderList.get(position);
    }
    @Override
    public long getItemId(int position){
        return  position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup){
        View viewItem = mInflater.inflate(R.layout.history_orderlist,null);

        Context context = viewGroup.getContext();

        String order = mOrderList.get(position);
        TextView orderState = (TextView)viewItem.findViewById(R.id.orderStateText);
        TextView orderDate = (TextView)viewItem.findViewById(R.id.orderDateText);
        TextView orderPrice = (TextView)viewItem.findViewById(R.id.orderPriceText);

        ImageView orderlist_top_left = (ImageView)viewItem.findViewById(R.id.orderlist_top_left);
        ImageView orderlist_top_right = (ImageView)viewItem.findViewById(R.id.orderlist_top_right);

        orderlist_top_left.setBackgroundResource(R.drawable.orderlist_top);
        orderlist_top_right.setBackgroundResource(R.drawable.orderlist_top);

        System.out.println("order!!!!!"+order);
        String[] temp = order.split("&");
        String date = temp[0];
        String price = temp[1];
        String state = temp[2];


        orderState.setText(state);
        orderDate.setText(date);
        orderPrice.setText(price);

        //根据状态设置不同颜色
        if(state.equals("已支付")){
            payStateColor = ContextCompat.getColor(context, R.color.orderList_pay_true);
        }
        else if(state.equals("未支付")){
            payStateColor = ContextCompat.getColor(context, R.color.orderList_pay_false);
        }
        else{
            payStateColor = ContextCompat.getColor(context, android.R.color.black);
        }


        GradientDrawable myGrad = (GradientDrawable)orderlist_top_left.getBackground();
        myGrad.setColor(payStateColor);
        GradientDrawable myGrad2 = (GradientDrawable)orderlist_top_right.getBackground();
        myGrad.setColor(payStateColor);

        orderPrice.setTextColor(payStateColor);

        return viewItem;
    }
}
