package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.android.PyApplication;

//import org.opencv.android.OpenCVLoader;

public class TestPython extends AppCompatActivity {
    TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_python);
        textView3 = findViewById(R.id.textView3);
        
//        if(OpenCVLoader.initDebug()){
//            Toast.makeText(this, "Đã load được thư viện opencv", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "Chưa thêm thư viện", Toast.LENGTH_SHORT).show();
//        }
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyob = py.getModule("betaTest");

        PyObject obj = pyob.callAttr("run", Environment.DIRECTORY_DOWNLOADS + "/" + "tnhan1/1.png");
        textView3.setText(obj.toString());
    }
}