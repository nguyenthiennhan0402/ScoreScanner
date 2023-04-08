package com.example.scorescanner;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Student> {
    Activity context;
    int Idlayout;
    ArrayList<Student> mylist;
    public ImageAdapter(Activity context1, int idlayout, ArrayList<Student> mylist) {
        super(context1,idlayout,mylist);
        this.context = context1;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(Idlayout,null);
        String imgpath = mylist.get(position).getImgpath();
        //set id cho item
        final Button btnxoaimg = convertView.findViewById(R.id.btnxoaimg);
        // Ứng với mỗi thuộc tính, ta thực hiện 2 việc

        // -Gán id
        ImageView imagebailam = convertView.findViewById(R.id.imagebailam);
        TextView txtid = convertView.findViewById(R.id.txtid);
        txtid.setText(mylist.get(position).getSbd());
        //convert imgname to bitmap
//        byte [] encodeByte = Base64.decode(imgName,Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        imagebailam.setImageBitmap(bitmap);

//        file = new File(directory.getAbsolutePath()+"/"+ name +".jpg");
//        File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + "tnhan1");
//        Uri imgUri=Uri.parse(directory.getAbsolutePath() + "/1.jpg");
        Uri imgUri=Uri.parse(imgpath);
        imagebailam.setImageURI(imgUri);
        btnxoaimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist.remove(position);
                int n = 0;
                n  = Baidacham.db.mydatabase.delete("diem","hinhanh =?",new String[]{imgpath});
                if(n > 0){
                    File file = new File(imgpath);
                    file.delete();
                    Toast.makeText(context, "Xoa thanh công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                }
                Baidacham.myimgadater.notifyDataSetChanged();
            }

        });
        return convertView;
    }
}
