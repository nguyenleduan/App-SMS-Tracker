package com.usexpress.myapplication.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.usexpress.myapplication.Adapter.AdapterListPhoneDashboard;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    public static AdapterListPhoneDashboard adapterListView;
    public ListView lv;
    Button btSaveTime;
    EditText edtTimeDelay;
    ArrayList<MainSMS> list  = new ArrayList<>();
    ArrayList<ItemModel> arrSMS = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        arrSMS.add(new ItemModel(11,"TEST1","asdas","asdasd","asdasd",true));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        lv = findViewById(R.id.lvDashboard);
        edtTimeDelay = findViewById(R.id.edtTimeDelay);
        btSaveTime = findViewById(R.id.btSaveTime);
        lv.setScrollContainer(false);
        addDate();
        Log.d("asdasd","asdasd" + list.size());
        edtTimeDelay.setText(DataSetting.TimeDelay+"");
        btSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }
    void addDate(){
        list.clear();
        if(DataSetting.arraySMSMain!=null && DataSetting.arraySMSMain.size()>0){
            list.addAll(DataSetting.arraySMSMain);
            list.add(new MainSMS(arrSMS,false,"","","","",""));
        }else{
            list.add(new MainSMS(arrSMS,false,"","","","",""));
        }
        adapterListView = new AdapterListPhoneDashboard(DashboardActivity.this, R.layout.item_list_phone_dasboard,R.layout.item_button, list);
        lv.setAdapter(adapterListView);
        adapterListView.notifyDataSetChanged();
        justifyListViewHeightBasedOnChildren(lv);
    }
    void Save() {
        try {
            String MY_PREFS_NAME = "MyPrefsFile";
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            SharedPreferences mPrefs = DashboardActivity.this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int timer;
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            if(edtTimeDelay.getText() != null || !edtTimeDelay.getText().equals("")){
                timer = Integer.parseInt(edtTimeDelay.getText()+"");
            }else{
                timer = 30;
            }
            edtTimeDelay.setText(timer+"");
            DataSetting.TimeDelay = timer;
            prefsEditor.putInt(DataSetting.KeyTime, timer);
            prefsEditor.apply();
            Log.d("Cache:","Save cache success");
            edtTimeDelay.setCursorVisible(false);
            DataSetting.hideKeyboard(DashboardActivity.this);
            DialogS("Save thành công");
        }catch (Exception e){
            Log.e("Cache:","Save time Error");
        }
    }

    void DialogS(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setMessage(s).setTitle("Thông báo")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDate();
        adapterListView.notifyDataSetChanged();
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}