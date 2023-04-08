package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView txtusername;
    ImageButton chambaibtn, taiphieubtn, thuvienbtn, logout,guide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtusername = findViewById(R.id.txtusername);
        chambaibtn = findViewById(R.id.chambaibtn);
        taiphieubtn = findViewById(R.id.taiphieubtn);
        logout = findViewById(R.id.logout);
        guide = findViewById(R.id.guide);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        txtusername.setText(username);
        chambaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent danhsachcham = new Intent(HomeActivity.this, danhsachkithi.class);
                danhsachcham.putExtra("username", username);
                startActivity(danhsachcham);
            }
        });
        taiphieubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.trendysmarts.com/scorescanner");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.finish();
            }
        });
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sp = new Intent(HomeActivity.this, SupportOption.class);
                startActivity(sp);
            }
        });
    }
}