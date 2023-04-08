package com.example.scorescanner;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class dapan_adapter extends ArrayAdapter<dapan_item> {
    Activity context;
    int idlayout;
    private ArrayList<dapan_item> mylist;

    public dapan_adapter(Activity context, int idlayout, ArrayList<dapan_item> mylist)
    {
        super(context, idlayout, mylist);
        this.context = context;
        this.idlayout = idlayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(idlayout, null);
        try
        {
            dapan_item item = mylist.get(position);

            TextView number = convertView.findViewById(R.id.num);

            ArrayList<RadioButton> array = new ArrayList<>();
//            RadioButton radioButton = convertView.findViewById(R.id.btnA);
            array.add(convertView.findViewById(R.id.btnA));
            array.add(convertView.findViewById(R.id.btnB));
            array.add(convertView.findViewById(R.id.btnC));
            array.add(convertView.findViewById(R.id.btnD));


//            for (int i = 0; i < array.size(); i++) {
//                RadioButton btn = array.get(i);
//                if (btn.getText().equals(item.getDapan())) {
//                    btn.setChecked(true);
//                    item.setDapan(btn.getText()+"");
//                } else {
//                    btn.setChecked(false);
//                }
//            }

            number.setText("["+item.getNum()+"]");

        }
        catch (Exception ex)
        {
//            Log.println(Log.DEBUG,"=====adap=====",ex.getMessage()+"=========");
        }
        return convertView;
    }

//    public String getData()
//    {
//        String data="";
//        for (int i = 0; i < mylist.size(); i++) {
//            dapan_item item = mylist.get(i);
//            data=data+item.getDapan();
//        }
//        Log.println(Log.DEBUG,"==========","" + data);
//        return data;
//    }
}
