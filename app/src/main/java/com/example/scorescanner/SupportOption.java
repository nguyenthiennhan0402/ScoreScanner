package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SupportOption extends AppCompatActivity {
    Button btn_HD,btn_phone,btn_Rate;
    ImageButton backbtn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_option);
        btn_HD = findViewById(R.id.btn_HD);
        btn_Rate = findViewById(R.id.btn_Rate);
        backbtn3 = findViewById(R.id.backbtn3);
        btn_HD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hd = new Intent(SupportOption.this, Guide.class);
                startActivity(hd);
            }
        });
        backbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportOption.this.finish();
            }
        });
        btn_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent danhgia = new Intent(SupportOption.this, Danhgia.class);
                startActivity(danhgia);
            }
        });
    }
}