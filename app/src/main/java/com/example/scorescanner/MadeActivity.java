package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MadeActivity extends AppCompatActivity {
    ListView lvmade;
    ArrayList<String> mylist;
    MadeAdapter myArrayAdapter;
    ImageView backbtn;

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made);
        lvmade = findViewById(R.id.lvmade);
        Intent intent = getIntent();
        backbtn = findViewById(R.id.back_btnds);
        String makithi = intent.getStringExtra("kithi");
        Toast.makeText(this, "makithi " + makithi, Toast.LENGTH_SHORT).show();
        mylist = new ArrayList<>();//tạo mới mảng rỗng


//        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
        myArrayAdapter = new MadeAdapter(this, R.layout.kithi_item,mylist);



        database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);
        Cursor c = database.rawQuery("select * from made where makithi = " + makithi , null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            String made = c.getString(1);
            mylist.add(made);
            c.moveToNext();
        }
        lvmade.setAdapter(myArrayAdapter);
//        myArrayAdapter.notifyDataSetChanged();

        String finalMakithi = makithi;
        lvmade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent madeoption = new Intent(MadeActivity.this, MadeOption.class);
                String made = mylist.get(i);
                madeoption.putExtra("made", made);
                madeoption.putExtra("kithi", finalMakithi);
                startActivity(madeoption);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent danhsach = new Intent(MadeActivity.this, danhsachkithi.class);
//                danhsach.putExtra("kithi", finalMakithi);
//                startActivity(danhsach);
            }
        });
        c.close();
    }
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
}