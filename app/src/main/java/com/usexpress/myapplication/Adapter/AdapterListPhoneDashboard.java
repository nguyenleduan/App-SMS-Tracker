package com.usexpress.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.usexpress.myapplication.Activity.DashboardActivity;
import com.usexpress.myapplication.Activity.SettingAppActivity;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.MainSMS;
import com.usexpress.myapplication.R;

import java.util.ArrayList;

public class AdapterListPhoneDashboard extends BaseAdapter {
    private Context mycontext;
    private int myLayout;
    private ArrayList<MainSMS> arry;

    TextView tvAllowPhone, tvStartDate, tvTimeRun, tvResponse;
    Switch SResponse;

    public AdapterListPhoneDashboard(Context context, int layout, ArrayList<MainSMS> arr) {
        this.mycontext = context;
        this.myLayout = layout;
        this.arry = arr;
    }

    @Override
    public int getCount() {
        return arry.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        RelativeLayout relativeLayout;
        tvAllowPhone = convertView.findViewById(R.id.tvAllowPhone);
        relativeLayout = convertView.findViewById(R.id.Relative);
        tvStartDate = convertView.findViewById(R.id.tvStartDate);
        tvTimeRun = convertView.findViewById(R.id.tvTimeRun);
        tvResponse = convertView.findViewById(R.id.tvResponse);
        SResponse = convertView.findViewById(R.id.SResponse);
        tvAllowPhone.setText(arry.get(position).phone);
        tvStartDate.setText(arry.get(position).date);
        tvTimeRun.setText(arry.get(position).timeStartTracker + " ---- " + arry.get(position).timeEndTracker);
        tvResponse.setText(arry.get(position).Content);
        tvResponse.setText(arry.get(position).Content);
        SResponse.setChecked(arry.get(position).isSendSMS);
        DataSetting dataSetting = new DataSetting();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(inflater.getContext(), SettingAppActivity.class);
                mycontext.startActivity(intent);
            }
        });
        SResponse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Switch is Response [" + position + ": ", " " + isChecked);
                MainSMS sms = new MainSMS(
                        arry.get(position).arrSMS,
                        isChecked,
                        arry.get(position).Content,
                        arry.get(position).date,
                        arry.get(position).phone,
                        arry.get(position).timeStartTracker,
                        arry.get(position).timeEndTracker);
                dataSetting.setValueArrMainSMS(position, sms);
            }
        });
        return convertView;
    }

}
