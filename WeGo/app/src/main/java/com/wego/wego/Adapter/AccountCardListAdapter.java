package com.wego.wego.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wego.wego.Item.Item;
import com.wego.wego.R;

import java.util.List;
import java.util.Random;



/**
 * Created by a4950 on 2017/7/15.
 */

public class AccountCardListAdapter extends BaseAdapter {
    private List<String> mCardList;
    private LayoutInflater mInflater;
    private int colorIndex;

    public AccountCardListAdapter(LayoutInflater inflater,List<String> data){
        mCardList = data;
        mInflater = inflater;
        colorIndex = 0;
    }
    @Override
    public int getCount(){
        return mCardList.size();
    }
    @Override
    public Object getItem(int position){
        return mCardList.get(position);
    }
    @Override
    public long getItemId(int position){
        return  position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup){
        View viewItem = mInflater.inflate(R.layout.account_cardlist,null);

        Context context = viewGroup.getContext();

        String mcard = mCardList.get(position);
        TextView cardName = (TextView)viewItem.findViewById(R.id.cardName);
        TextView cardNumber = (TextView)viewItem.findViewById(R.id.cardNumber);
        ImageView cardlistHead = (ImageView)viewItem.findViewById(R.id.cardlist_head);
        cardlistHead.setBackgroundResource(R.drawable.cardlist_head);

        if(position == (mCardList.size() - 1)){     //最后一行添加新卡
            ImageView cardAddBack = (ImageView)viewItem.findViewById(R.id.cardlist_add_back);
            ImageView cardAddIcon = (ImageView)viewItem.findViewById(R.id.cardlist_add_icon);
            ImageView cardlistBack = (ImageView)viewItem.findViewById(R.id.cardlist_back);

            cardName.setVisibility(View.GONE);
            cardlistBack.setVisibility(View.GONE);
            cardlistHead.setVisibility(View.GONE);
            cardAddBack.setVisibility(View.VISIBLE);
            cardAddIcon.setVisibility(View.VISIBLE);
            cardNumber.setText("XXXX");
        }
        else{
            System.out.println("mcard!!!!!"+mcard);
            String[] temp = mcard.split(":");
            String name = temp[0];
            String number = temp[1];

            //对卡号进行空格处理
            StringBuffer number_insert = new StringBuffer(number);
            number_insert.insert(4," ");
            number_insert.insert(9," ");
            number_insert.insert(14," ");

            System.out.println("number!!!!!"+number_insert);
            cardName.setText(name);
            cardNumber.setText(number_insert);

            //对卡头设置不同颜色

            int[] headColors = context.getResources().getIntArray(R.array.customizedColors);
            int randomHeadColor = headColors[colorIndex++];
            if(colorIndex >= headColors.length)
            {
                colorIndex = 0;
            }

            GradientDrawable myGrad = (GradientDrawable)cardlistHead.getBackground();
            myGrad.setColor(randomHeadColor);

        }
        return viewItem;
    }


}
