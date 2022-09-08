package com.usexpress.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.usexpress.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SettingAppActivity extends AppCompatActivity {
    private EditText edtDate,edtTimeStart,edtTimeEdt,edtPhone;
    private Switch SAnswer;
    private Spinner spnCategory;
    private Button btSetup;
    private String content, phone,date,timeStart,timeEnd;
    boolean isAnswer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_app);
        List<String> list = new ArrayList<>();
        list.add("Xin cảm ơn !!");
        list.add("Chào ngày mới <3");
        list.add("Buổi tối vui vẻ :D");
        list.add("Xin cảm ơn Xin cảm ơn Xin cảm ơn Xin cảm ơn Xin cảm ơn Xin cảm ơn !!");
        list.add("Chào ngày mới <3");
        list.add("Buổi tối vui vẻ :D");
        spnCategory = findViewById(R.id.spnCategory);
        edtDate = findViewById(R.id.edtDate);
        edtTimeStart = findViewById(R.id.edtTimeStart);
        edtTimeEdt = findViewById(R.id.edtTimeEdt);
        btSetup = findViewById(R.id.btSetup);
        edtPhone = findViewById(R.id.edtPhone);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("",""+list.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setValue();
        Event();
    }

    private void Event() {
        btSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setValue(){
//        edtDate.setText("");
//        edtTimeStart = findViewById(R.id.edtTimeStart);
//        edtTimeEdt = findViewById(R.id.edtTimeEdt);
//        edtPhone = findViewById(R.id.edtPhone);
    }




}