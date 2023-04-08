package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Base64;

public class Baidacham extends AppCompatActivity {
    GridView grvimg;
    public static DataBase db = null;
    String makithi = "";
    String username = "";
    ArrayList<Student> mylist;
    public static ImageAdapter  myimgadater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidacham);
        grvimg = findViewById(R.id.grvimg);
        ArrayList<String> testimg = new ArrayList<>();
        Intent intent = getIntent();
        mylist = new ArrayList<>();//tạo mới mảng rỗng
        myimgadater = new ImageAdapter(this,R.layout.imageitem,mylist);
        String makithi = intent.getStringExtra("makithi");
        String username =  intent.getStringExtra("username");
        db = new DataBase(this);
        Cursor c = db.mydatabase.rawQuery("select * from diem where makithi = " + makithi, null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            String imguri = c.getString(2);
//            Toast.makeText(Baidacham.this, "" + imguri, Toast.LENGTH_SHORT).show();
            String masv = c.getString(3);
            Student std = new Student(masv,imguri);
            mylist.add(std);
            c.moveToNext();
        }
        grvimg.setAdapter(myimgadater);
        myimgadater.notifyDataSetChanged();
        c.close();
    }

}