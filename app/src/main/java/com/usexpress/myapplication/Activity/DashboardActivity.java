package com.usexpress.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.usexpress.myapplication.Adapter.AdapterListPhoneDashboard;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.R;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    public static AdapterListPhoneDashboard adapterListView;
    public ListView lv;

    ArrayList<MainSMS> list  = new ArrayList<>();
    ArrayList<ItemModel> arrSMS = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        arrSMS.add(new ItemModel(11,"09999","asdas","asdasd","asdasd",true));

        list.add(new MainSMS(arrSMS,true,"Gui SMS","01/01/20022","097674917826","1","2"));
        list.add(new MainSMS(arrSMS,false,"Gui SMS","01/01/20022","09912311111","1","2"));
        list.add(new MainSMS(arrSMS,false,"Gui SMS","01/01/20022","TEST","1","2"));
        list.add(new MainSMS(arrSMS,true,"Gui SMS","01/01/20022","BAT","1","2"));
        list.add(new MainSMS(arrSMS,false,"Gui SMS","01/01/20022","010111","1","2"));
        DataSetting.arraySMSMain.addAll(list);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        lv = findViewById(R.id.lvDashboard);
        lv.setScrollContainer(false);
        adapterListView = new AdapterListPhoneDashboard(DashboardActivity.this, R.layout.item_list_phone_dasboard, list);
        lv.setAdapter(adapterListView);
        adapterListView.notifyDataSetChanged();
        justifyListViewHeightBasedOnChildren(lv);
        event();
    }
    public  void event(){
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