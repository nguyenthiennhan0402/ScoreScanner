package com.example.scorescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class danhsachkithi extends AppCompatActivity {
    ListView lvdanhsachkt;
    ArrayList<Exam> mylist;
    MyArrayAdapter myArrayAdapter;

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    String username = "";
    ImageButton backbtn, addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachkithi);
        lvdanhsachkt = findViewById(R.id.lvdanhsachkt);
        addbtn = findViewById(R.id.addbtn);
        backbtn = findViewById(R.id.back_btnds);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        processCopy();

        database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);
//        String sql = "select * from kithi where username = '" + username + "'";
        mylist = new ArrayList<>();//tạo mới mảng rỗng
        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);

        Cursor c = database.rawQuery("select * from kithi where username = '" + username + "'", null);
        c.moveToFirst();
        mylist.clear();
        String data ="";
        while (c.isAfterLast() == false)
        {
            int madethi = Integer.parseInt(c.getString(0));
            String tendethi = c.getString(1);
            Exam exam = new Exam(madethi, tendethi, username);
            mylist.add(exam);
            c.moveToNext();
        }
        lvdanhsachkt.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();
//        addkithi();
        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(danhsachkithi.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent made = new Intent(danhsachkithi.this, MadeOption.class);
                String makithi = mylist.get(i).getMakithi() + "";
                made.putExtra("makithi", makithi);
                made.putExtra("username", username);
//                Toast.makeText(danhsachkithi.this, "makithi " +  mylist.get(i).getMakithi(), Toast.LENGTH_SHORT).show();
                startActivity(made);
            }
        });
//        c.close();/

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhsachkithi.this.finish();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(danhsachkithi.this, DanhSachKithi_Add.class);
                add.putExtra("username", username);
                startActivityForResult(add,105);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 105 && resultCode == 33) {
            Intent result = getIntent();
            String username2 = result.getStringExtra("username");
//            Toast.makeText(this, "vao ham result", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "User name " + username, Toast.LENGTH_SHORT).show();
            String tenkithi = data.getStringExtra("tenkithi");
            int socau = data.getIntExtra("socau",20);
            int hediem = data.getIntExtra("hediem",10);
            String loaiphieu = data.getStringExtra("loaiphieu");
//            Toast.makeText(this, "" + tenkithi + " " + socau, Toast.LENGTH_SHORT).show();

            //them ki thi moi vao database
            ContentValues valuekithi = new ContentValues();
            valuekithi.put("tenkithi",tenkithi );
            valuekithi.put("username",username2);
            valuekithi.put("socau",socau);
            valuekithi.put("hediem",hediem);
            valuekithi.put("loaiphieu",loaiphieu);
            String msg  = "";
//            Exam exam = new Exam(madethi, tenkithi, username);
            if(database.insert("kithi",null,valuekithi)==-1){
                msg = "Fail to insert kithi";

            }else{
                msg = "Insert kithi thanh cong ";
                loadkithi(username2);
            }
        }
    }
    private void loadkithi(String username2) {
        mylist.clear();
        Cursor c = database.rawQuery("select * from kithi where username = '" + username2 + "'", null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            int madethi = Integer.parseInt(c.getString(0));
            String tendethi = c.getString(1);
            Exam exam = new Exam(madethi, tendethi, username);
            mylist.add(exam);
            c.moveToNext();
        }
        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
        lvdanhsachkt.setAdapter(myArrayAdapter);
//        myArrayAdapter.notifyDataSetChanged();
        c.close();
    }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
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
//            if (!f.exists())
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

}