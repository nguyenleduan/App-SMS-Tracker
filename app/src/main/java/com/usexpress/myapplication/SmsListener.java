//package com.usexpress.myapplication;
//
//import android.content.BroadcastReceiver;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telephony.SmsMessage;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.usexpress.myapplication.Fragment.ListTreackerFragment;
//import com.usexpress.myapplication.Model.ApiRequet;
//import com.usexpress.myapplication.Model.ItemModel;
//import com.usexpress.myapplication.Model.bodyModel;
//import com.usexpress.myapplication.Service.CallApi;
//import com.usexpress.myapplication.Service.CallApiToken;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SmsListener extends BroadcastReceiver {
//    DataSetting data;
//    private SharedPreferences preferences;
//
//    public static int counter = 0;
//    private Timer timer, timerSave;
//    private TimerTask timerTask;
//
//    public void startTimerCallApi(Context context) {
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            public void run() {
//                if (counter == DataSetting.TimeDelay) {
//                    timer.cancel();
//                    counter = 0;
//                    Toast.makeText(context, "11111111111", Toast.LENGTH_SHORT).show();
//                    startTimerCallApi(context);
//                }
//                Log.i("Count", "=========  " + counter);
//                counter++;
//            }
//        };
//        timer.schedule(timerTask, 1000, 1000); //
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        startTimerCallApi(context);
//        // TODO Auto-generated method stub
//        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")||intent.getAction().equals("android.permission.RECEIVE_SMS")) {
//            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
//            SmsMessage[] msgs = null;
//            String msg_from = null;
////            if (bundle != null) {
////                //---retrieve the SMS message received---
////                try {
////                    Object[] pdus = (Object[]) bundle.get("pdus");
////                    msgs = new SmsMessage[pdus.length];
////                    String msgBody = "";
////                    for (int i = 0; i < msgs.length; i++) {
////                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
////                        msg_from = msgs[i].getOriginatingAddress();
////                        msgBody = msgBody + msgs[i].getMessageBody();
////                    }
////                } catch (Exception e) {
////                    Log.d("Exception caught", e.getMessage());
////                }
////            }
//        }
//    }
////    void checksss(Context context,   String allowPhone, String message){
////        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
////        Date date = new Date();
////        String dateFormat = formatter.format(date);
////        bodyModel model = new bodyModel(allowPhone,message,dateFormat);
////        CallApi.apiService.PostSMS(data.UrlApi,model).enqueue(new Callback<ApiRequet>() {
////            @Override
////            public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
////                if(response.code()==200) {
////                    data.arrayListData.add(new ItemModel(allowPhone, message, 0, formatter.format(date), formatter.format(date), 0));
////                    Toast.makeText(context, "Đã gửi nội dung: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
////                    ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                    if(response.body().getCode()==0){
//////                        data.arrayListData.add(new ItemModel(allowPhone, message, 0, formatter.format(date), formatter.format(date), 0));
//////                        Toast.makeText(context, "Đã gửi nội dung: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                        ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                    }else{
//////                        data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                        ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                    }
////                } else {
////                    Toast.makeText(context, "Gửi Thông tin thất bại: "+response.code()+" --- [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
////                    data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Gửi Api fail "+response.code(), false));
////                    ListTreackerFragment.adapterListView.notifyDataSetChanged();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<ApiRequet> call, Throwable t) {
////                Toast.makeText(context, "Gửi Thông tin thất bại: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
////                data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Gửi Api fail SMS listener" , false));
////                ListTreackerFragment.adapterListView.notifyDataSetChanged();
////            }
////        });
////    }
////
////    void CallApi(Context context, String allowPhone, String message) {
////        if (true) {
////            checksss(context,allowPhone,message);
////            if (DataSetting.dataProfile.AllowPhone != "") {
////                String string = DataSetting.dataProfile.AllowPhone;
////                String[] list = string.split(",");
////                for (int i = 0; i < list.length; i++) {
////                    if (list[i].equals(allowPhone)) {
////                        checksss(context,allowPhone,message);
//////                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//////                        Date date = new Date();
//////                        String dateFormat = formatter.format(date);
//////                        bodyModel model = new bodyModel(list[i],message,dateFormat);
//////                        try {
//////                            if (data.isUrlToken) {
//////                                try {
//////                                    CallApiToken.apiService.PostSMSToken(data.UrlApi, list[i], message, dateFormat).enqueue(new Callback<ApiRequet>() {
//////                                        @Override
//////                                        public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
//////                                                if (response.code()==200) {
//////                                                    if(response.body().getCode()==0){
//////                                                        data.arrayListData.add(new ItemModel(allowPhone, message, 0, formatter.format(date), formatter.format(date), 0));
//////                                                        Toast.makeText(context, "Đã gửi nội dung: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                                        ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                                    }else{
//////                                                        data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                                        ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                                    }
//////                                            } else {
//////                                                data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                                ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                            }
//////
//////                                        }
//////
//////                                        @Override
//////                                        public void onFailure(Call<ApiRequet> call, Throwable t) {
//////                                            Toast.makeText(context, "Gửi Thông tin thất bại: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                            data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                            ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                        }
//////                                    });
//////                                } catch (Exception e) {
//////                                    Toast.makeText(context, "Error Link", Toast.LENGTH_SHORT).show();
//////                                }
//////                            } else {
//////                                CallApi.apiService.PostSMS(data.UrlApi,model).enqueue(new Callback<ApiRequet>() {
//////                                    @Override
//////                                    public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
//////                                        if (response.code()==200) {
//////                                            if(response.body().getCode()==0){
//////                                                data.arrayListData.add(new ItemModel(allowPhone, message, 0, formatter.format(date), formatter.format(date), 0));
//////                                                Toast.makeText(context, "Đã gửi nội dung: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                                ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                            }else{
//////                                                Toast.makeText(context, "Gửi Thông tin thất bại: "+response.code()+" --- [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                                data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                                ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                            }
//////                                        } else {
//////                                            Toast.makeText(context, "Gửi Thông tin thất bại: "+response.code()+" --- [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                            data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                            ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                        }
//////                                    }
//////
//////                                    @Override
//////                                    public void onFailure(Call<ApiRequet> call, Throwable t) {
//////                                        Toast.makeText(context, "Gửi Thông tin thất bại: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
//////                                        data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
//////                                        ListTreackerFragment.adapterListView.notifyDataSetChanged();
//////                                    }
//////                                });
//////                            }
//////                        } catch (Exception e) {
//////                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//////                        }
////                    }
////                }
////            } else {
////                Toast.makeText(context, "Settings have not been saved", Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            Toast.makeText(context, "Settings have not been saved", Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    void CallApiToken(Context context, String allowPhone, String message) {
////        if (allowPhone != "") {
////            if (DataSetting.dataProfile.AllowPhone != "") {
////                String string = DataSetting.dataProfile.AllowPhone;
////                String[] list = string.split(",");
////                for (int i = 0; i < list.length; i++) {
////                    if (list[i].equals(allowPhone)) {
////                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
////                        Date date = new Date();
////                        String dateFormat = formatter.format(date);
////                        bodyModel model = new bodyModel(list[i],message,dateFormat);
////                        try {
////                            CallApi.apiService.PostSMS(data.UrlApi,model).enqueue(new Callback<ApiRequet>() {
////                                @Override
////                                public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
////                                    if(response.code()==200){
////                                        if (response.body().getCode() == 0) {
////                                            data.arrayListData.add(new ItemModel(allowPhone, message, 0, formatter.format(date), formatter.format(date), 0));
////                                            Toast.makeText(context, "Gửi Thông tin thất bại: [Phone: " + allowPhone + "]", Toast.LENGTH_SHORT).show();
////                                        }
////                                    } else {
////                                        data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
////                                    }
////                                }
////
////                                @Override
////                                public void onFailure(Call<ApiRequet> call, Throwable t) {
////                                    data.arrayListData.add(new ItemModel(allowPhone, message, 1, formatter.format(date), "Unsuccessful", 1));
////                                }
////                            });
////                        } catch (Exception e) {
////                            Toast.makeText(context, "Lỗi SMSlistener 174", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                }
////            } else {
////                Toast.makeText(context, "Settings have not been saved", Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            Toast.makeText(context, "Settings have not been saved", Toast.LENGTH_SHORT).show();
////        }
////    }
//
//}