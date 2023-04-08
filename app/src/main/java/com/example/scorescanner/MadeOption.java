package com.example.scorescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.os.Environment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MadeOption extends AppCompatActivity {
    TextView txtmade;
    Button dapanbtn,chambaibtn,baidachambtn,xuatdiembtn,thongkebtn;
    ImageButton backbtn;

    Uri imageUri;
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_made_option);
            dapanbtn = findViewById(R.id.dapanbtn);
            chambaibtn = findViewById(R.id.chambaibtn);
//            xuatdiembtn = findViewById(R.id.xuatdiembtn);
            thongkebtn = findViewById(R.id.thongkebtn);

            backbtn = findViewById(R.id.back_btnds);
            baidachambtn = findViewById(R.id.baidachambtn);

            txtmade = findViewById(R.id.txtmade);
            Intent intent = getIntent();
            String makithi = intent.getStringExtra("makithi");
//            Toast.makeText(this, ""+ makithi, Toast.LENGTH_SHORT).show();
            String username =  intent.getStringExtra("username");
//        String made = intent.getStringExtra("made");
            txtmade.setText("Kì thi "+makithi);

//        Toast.makeText(MadeOption.this, "Ki thi " + makithi + " made " + made, Toast.LENGTH_SHORT).show();
            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MadeOption.this.finish();
                }
            });
            thongkebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent testload = new Intent(MadeOption.this, testLoadAnh.class);
                    startActivity(testload);
                }
            });
            dapanbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent acti = new Intent(MadeOption.this, MadeOptionAddActivity.class);
//                    acti.putExtra("made", made + "");
                        acti.putExtra("kithi", makithi + "");
                        startActivity(acti);
                    }catch (Exception ex)
                    {
                        Log.println(Log.DEBUG,"dapanbtn",ex.getMessage()+"");
                    }
                }
            });
            baidachambtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent baidacham = new Intent(MadeOption.this, Baidacham.class);
                baidacham.putExtra("makithi", makithi + "");
                startActivity(baidacham);
                }
            });
            chambaibtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processCopy();
                    database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);

                    int kithi = Integer.parseInt(makithi);
                    Cursor c = database.rawQuery("select * from cauhoi where makithi = " + makithi, null);
//                    Cursor c = database.query("cauhoi2",null,null,null,null,null,null, null);
                    c.moveToFirst();
                    String data ="";
                    while (c.isAfterLast() == false)
                    {
                        String listanswer = c.getString(0);
                        data+=listanswer;
                        c.moveToNext();

                    }
                    if(data.equals("")){

                        Toast.makeText(MadeOption.this, "Vui lòng nhập đáp án!", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(MadeOption.this, "chuyển sang chấm bài", Toast.LENGTH_SHORT).show();
//                    Intent myintent = new Intent(ACTION_IMAGE_CAPTURE);

                        Intent camerachambai = new Intent(MadeOption.this, CameraChamBai.class);
//                    camerachambai.putExtra("made", made + "");
                        camerachambai.putExtra("kithi", makithi + "");
                        camerachambai.putExtra("username", username + "");
                        startActivity(camerachambai);
                        if (ActivityCompat.checkSelfPermission(MadeOption.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(MadeOption.this,new String[]{Manifest.permission.CAMERA}, 1);
                            return;
                        }

//                    startActivityForResult(myintent,99);
                    }
                    c.close();
//                try{
//
//                }catch (Exception e){
//                    Toast.makeText(MadeOption.this, "Bị lỗi", Toast.LENGTH_SHORT).show();
//                }

//                Toast.makeText(MadeOption.this, "cham bai", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e)
        {
            Log.println(Log.ERROR,"====",e.getMessage());
        }


    }


    //hàm load database từ asset
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {

        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                    f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99){
            if(resultCode == Activity.RESULT_OK){
                Bitmap image = (Bitmap) data.getExtras().get("data");

                String imgname = String.valueOf(System.currentTimeMillis());//tên ảnh
                File directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                File mypath = new File(directory.getAbsolutePath()+"/myimg.jpg");

//                Toast.makeText(this, mypath+"", Toast.LENGTH_SHORT).show();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}