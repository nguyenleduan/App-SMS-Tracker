package com.usexpress.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.MainSMS;
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
    int index;
    DataSetting dataSetting = new DataSetting();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        index = intent.getIntExtra("Key_2", -1);
        setContentView(R.layout.activity_setting_app);
        spnCategory = findViewById(R.id.spnCategory);
        edtDate = findViewById(R.id.edtDate);
        edtTimeStart = findViewById(R.id.edtTimeStart);
        edtTimeEdt = findViewById(R.id.edtTimeEdt);
        SAnswer = findViewById(R.id.SAnswer);
        btSetup = findViewById(R.id.btSetup);
        edtPhone = findViewById(R.id.edtPhone);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,DataSetting.arrayContentSendSMS);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        if(index != -2){
            Log.d("index:", "---------- "+index);
            spnCategory.setSelection(dataSetting.getIndexListContent(DataSetting.arraySMSMain.get(index).Content+""));
        }else{
            spnCategory.setSelection(0);
            Log.d("ind123ex:", "---------- "+index);
        }
        setValue();
        Event();
        adapter.notifyDataSetChanged();
    }


    void Save() {
        String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataSetting.arraySMSMain);
        prefsEditor.putString(DataSetting.KeySMS, json);
        prefsEditor.apply();
        finish();
        Log.d("Save cache SMS", "----------Save cache success");
    }

    private void Event() {
        btSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == -2){
                    MainSMS sms = new MainSMS(
                            new ArrayList<>(),
                            isAnswer,
                            content,
                            edtDate.getText()+"",
                            edtPhone.getText()+"",
                            edtTimeStart.getText()+"",
                            edtTimeEdt.getText()+"");
                    DataSetting.arraySMSMain.add(sms);
                }else{
                    MainSMS sms = new MainSMS(
                            DataSetting.arraySMSMain.get(index).arrSMS,
                            isAnswer,
                            content,
                            edtDate.getText()+"",
                            edtPhone.getText()+"",
                            edtTimeStart.getText()+"",
                            edtTimeEdt.getText()+"");
                    dataSetting.setValueArrMainSMS(index,sms,SettingAppActivity.this);
                }
                Save();
            }
        });

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("",position+"   "+DataSetting.arrayContentSendSMS.get(position));
                content = DataSetting.arrayContentSendSMS.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("","asdsss");
            }
        });
        SAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAnswer = isChecked;
            }
        });
    }
    private void setValue(){
        if(index == -2){
            edtPhone.setText("Custom phone");
            edtTimeStart.setText("0");
            edtTimeEdt.setText("0");
            edtDate.setText("30/12/2021");
            SAnswer.setChecked(false);
        }else{
            edtPhone.setText(""+DataSetting.arraySMSMain.get(index).phone);
            edtTimeStart.setText(""+DataSetting.arraySMSMain.get(index).timeStartTracker);
            edtTimeEdt.setText(""+DataSetting.arraySMSMain.get(index).timeEndTracker);
            edtDate.setText(""+DataSetting.arraySMSMain.get(index).date);
            SAnswer.setChecked(DataSetting.arraySMSMain.get(index).isSendSMS);
        }
    }

}