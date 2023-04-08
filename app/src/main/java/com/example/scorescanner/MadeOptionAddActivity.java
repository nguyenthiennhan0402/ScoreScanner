package com.example.scorescanner;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;


public class MadeOptionAddActivity extends AppCompatActivity {

    TextView txtmade;
    Button addFileBtn,addHandBtn,viewAns;
    ImageButton backbtn;

    String makithi;
    String made;

    DataBase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_option_add);
        backbtn = findViewById(R.id.backbtn3);
        txtmade = findViewById(R.id.txtmade);


//        viewAns = findViewById(R.id.addhandbtn11);

        addFileBtn = findViewById(R.id.dapanbtn);
        addHandBtn = findViewById(R.id.chambaibtn);

        Intent intent = getIntent();
        makithi = intent.getStringExtra("kithi");
        made = intent.getStringExtra("made");
//        Toast.makeText(this, "" + makithi, Toast.LENGTH_SHORT).show();
        txtmade.setText("Nhập đán án kì thi");
        db = new DataBase(this);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MadeOptionAddActivity.this.finish();
            }
        });

        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,requestCode);
            }
        });

        addHandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent acti = new Intent(MadeOptionAddActivity.this, dapan_activity.class);
                    acti.putExtra("kithi", makithi);
                    startActivity(acti);
                }catch (Exception ex)
                {
                    Log.println(Log.DEBUG,"=== add hand ===",ex.getMessage()+"");
                }
            }
        });

//        viewAns.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent acti = new Intent(MadeOptionAddActivity.this, MainActivityListAns.class);
//                    startActivity(acti);
//                }catch (Exception ex)
//                {
//                    Log.println(Log.DEBUG,"dapanbtn",ex.getMessage()+"");
//                }
//            }
//        });
    }

    int requestCode = 1;
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Context context = getApplicationContext();
        if(this.requestCode == requestCode && resultCode == Activity.RESULT_OK)
        {
            if(data==null)
                return;
            Uri uri = data.getData();
            if(readFile(context,uri))
                Toast.makeText(context, "thêm thành công", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "thêm không thành công", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean readFile(Context context,Uri uri)
    {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream input = contentResolver.openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(input);
            Sheet sheet = workbook.getSheetAt(0);
            ArrayList<String> list = new ArrayList<>();
            boolean status = true;
            for(int i=0; i<sheet.getPhysicalNumberOfRows(); i++)
            {
                String s="";
                boolean ishavestring = false;
                for(int j=0; j<sheet.getRow(i).getPhysicalNumberOfCells(); j++)
                {
                    Cell cell = sheet.getRow(i).getCell(j);
                    String str = cell.toString();
                    if(!str.isEmpty())
                    {
                        ishavestring=true;
                        s+=str+"-";
                    }
                }
                list.add(s);
                if(!ishavestring) break;
            }
            workbook.close();
            if(db.mydatabase == null)
            {
                Log.println(Log.DEBUG,"read excel","----- database = null -----");
                return false;
            }
            list.remove(0);
            for(String line : list)
            {
                String strmade = line.split("-")[0];
                Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi +" and made = '"+
                                                            strmade+"'", null);
                String dataupdate = String.join("",line.substring(strmade.length()).split("-"));
                ContentValues values = new ContentValues();
                values.put("dapan",dataupdate);
                if(c.getCount()==0)
                {
                    values.put("made",strmade);
                    values.put("makithi",makithi);
                    if(db.mydatabase.insert("cauhoi",null,values)==-1)
                    {
                        status = false;
                    }
                }
                else
                {
                    int row = db.mydatabase.update("cauhoi",values,"made = ? and makithi = ?",new String[]{strmade,makithi});
                    if(row==0)
                    {
                        Log.println(Log.DEBUG,"read excel","update NOT OK ");
                        status = false;
                    }
                    else
                    {
                        Log.println(Log.DEBUG,"read excel","update OK ");
                    }
                }
            }
            return status;
        } catch (Exception e) {
            Log.println(Log.DEBUG,"excel ","--- "+e.getMessage()+" ---");
        }
        return false;
    }
}