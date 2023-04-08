package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

public class testLoadAnh extends AppCompatActivity {
    DataBase db = null;
    ImageView testimg;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_load_anh);
        testimg = findViewById(R.id.testimg);
        textView2 = findViewById(R.id.textView2);
        db = new DataBase(this);
        Cursor c = db.mydatabase.rawQuery("select * from diem ", null);
        c.moveToFirst();
        String data ="";
        data = c.getString(2);

//        ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
//        Bitmap theImage= BitmapFactory.decodeStream(imageStream);
//        Toast.makeText(db, "" + bytes[0], Toast.LENGTH_SHORT).show();
//        testimg.setImageBitmap(theImage);
//        c.close();
//        File directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "tnhan1");
//        file = new File(directory.getAbsolutePath()+"/"+ name +".jpg");
//        Uri imgUri=Uri.parse(directory.getAbsolutePath() + "/1.jpg");
        Uri imgUri=Uri.parse(data);
        textView2.setText(imgUri.toString());
        testimg.setImageURI(imgUri);
    }
}