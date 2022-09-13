package com.usexpress.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.R;

import java.util.ArrayList;

public class AdapterListView extends BaseAdapter {
    private Context mycontext;
    private int myLayout;
    private ArrayList<ItemModel> arry;

    public AdapterListView(Context context, int layout, ArrayList<ItemModel> arr) {
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
        TextView tvAlowPhone, tvMessage, tvDateFail, tvDateSuccess, tvsend, tvBody;
        RelativeLayout Relative;
        tvBody = convertView.findViewById(R.id.tvBody);
        tvsend = convertView.findViewById(R.id.tvsend);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        tvAlowPhone = convertView.findViewById(R.id.tvAlowPhone);
        Relative = convertView.findViewById(R.id.Relative);
        tvDateFail = convertView.findViewById(R.id.tvDateFail);

        tvDateFail.setText("Date Call API: " + arry.get(position).getDate_CallSuccessful());
        tvAlowPhone.setText(arry.get(position).getPhone() + "");
        tvBody.setText("MyNumber: " + DataSetting.myPhoneNumber + "\nCustomerNumber: " + arry.get(position).getPhone()
                + "\nContentData: " + arry.get(position).getMessage() + "\nDateData: " + arry.get(position).getDate_SMSArrived());
        if (arry.get(position).getAnswer() == 1) {
            tvsend.setText("Answer: Success");
        } else if (arry.get(position).getAnswer() == 2) {
            tvsend.setText("Answer: Skip send");
        } else if (arry.get(position).getAnswer() == 0) {
            tvsend.setText("Answer: Unsent");
        } else {
            tvsend.setText("Answer: Error");
        }

        if (!arry.get(position).isSucceeded()) {
            imageView.setImageResource(R.drawable.uncheck);
            Relative.setBackgroundResource(R.color.list);
        }
        return convertView;
    }


}