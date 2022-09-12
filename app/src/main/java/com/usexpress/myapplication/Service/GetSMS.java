package com.usexpress.myapplication.Service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;

import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.SMSModel;

import java.util.ArrayList;
import java.util.Date;

public class GetSMS {
    public boolean checkData() {
        if (DataSetting.dataProfile == null
                || DataSetting.arraySMSMain == null
                ||DataSetting.arraySMSMain.size() ==0 ) {
            return false;
        }
        return true;
    }
    public boolean checkSMS(ArrayList<ItemModel> list, long id){
        for(int i=0;i<list.size();i++){
            if(id == list.get(i).getID()){
                return false;
            }
        }
        return true;
    }


    public void readSMS(ContentResolver cr) {
        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id", "address", "body", "date"};
        Cursor cursor = cr.query(inboxURI, reqCols, null, null, null);
        if (!cursor.moveToFirst()) {
            return;
        }
        try {
            final int idIndex = cursor.getColumnIndex("_id");
            final int nameIndex = cursor.getColumnIndex("address");
            final int descriptionIndex = cursor.getColumnIndex("body");
            final int dateSMS = cursor.getColumnIndex("date");
            if (!cursor.moveToFirst()) {
            }
            do {
                final long id = cursor.getLong(idIndex);
                final String phone = cursor.getString(nameIndex);
                final String body = cursor.getString(descriptionIndex);
                final String date = DateFormat.format("HH:mm:ss dd/MM/yyyy", new Date(cursor.getLong(dateSMS))).toString();
                if (checkData()) {
                    for (int i = 0; i < DataSetting.arraySMSMain.size(); i++) {
                        if(phone.contains(DataSetting.arraySMSMain.get(i).phone)&& cursor.getLong(dateSMS) > DataSetting.arraySMSMain.get(i).getDateStart().getTime() ){
                            if(checkSMS(DataSetting.arraySMSMain.get(i).arrSMS,id)){
                                DataSetting.arraySMSMain.get(i).arrSMS.add(new ItemModel(id,phone,body,date,"Chưa gửi api",false,0));
                                Log.d("phone:", phone);
                                Log.d("body", body);
                                Log.d("date", date);
                                Log.d("list main SMS", DataSetting.arraySMSMain.size() + "");
                                Log.d("-------------------------", "-------------------------");
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }
    }

}
