package com.usexpress.myapplication.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usexpress.myapplication.Activity.HomeActivity;
import com.usexpress.myapplication.Adapter.AdapterListView;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.ApiRequet;
import com.usexpress.myapplication.Model.ItemModel;
import com.usexpress.myapplication.Model.bodyModel;
import com.usexpress.myapplication.R;
import com.usexpress.myapplication.Service.CallApi;
import com.usexpress.myapplication.Service.CallApiToken;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTreackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListTreackerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ListTreackerFragment() {
        // Required empty public constructor
    }

    public static ListTreackerFragment newInstance(String param1, String param2) {
        ListTreackerFragment fragment = new ListTreackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ListView lvFail;
    DataSetting data;
    public static AdapterListView adapterListView;
    androidx.appcompat.widget.AppCompatButton btCheckFail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        lvFail = view.findViewById(R.id.lv);
        btCheckFail = view.findViewById(R.id.btResend);
        adapterListView = new AdapterListView(getContext(), R.layout.item_listfail_listview, data.arrayListData);
        lvFail.setAdapter(adapterListView);
        btCheckFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Đang gửi lại SMS", Toast.LENGTH_SHORT).show();
//                ResendApi(0);
            }
        });
        return view;
    }

//    void ResendApi(final int i) {
//        if (data.arrayListData.size() > 0) {
//            if (i < data.arrayListData.size()) {
//                if (data.arrayListData.get(i).getSucceeded() == 0) {
//                    ResendApi(i + 1);
//                } else {
//                    if (data.arrayListData.get(i).getCountFail() < 5) {
//                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                        Date date = new Date();
//                        String dateFormat = formatter.format(date);
//                        // call api
//                        try {
//                            String phone = data.arrayListData.get(i).getPhone();
//                            String message = data.arrayListData.get(i).getMessage();
//                            if (data.dataProfile.User.equals("")) {
//                                CallApiToken.apiService.PostSMSToken(data.UrlApi, phone, message, dateFormat).enqueue(new Callback<ApiRequet>() {
//                                    @Override
//                                    public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
//                                        if (response.code() == 200) {
//                                            if (response.body().getCode() == 0) {
//                                                data.arrayListData.set(i, new ItemModel(data.arrayListData.get(i).getPhone(), data.arrayListData.get(i).getMessage(), data.arrayListData.get(i).getCountFail()
//                                                        , data.arrayListData.get(i).getDate_SMSArrived(), formatter.format(date), 0));
//                                                ResendApi(i + 1);
//                                                adapterListView.notifyDataSetChanged();
//                                            } else {
//                                                int mCount = data.arrayListData.get(i).getCountFail();
//                                                data.arrayListData.get(i).setCountFail(mCount++);
//                                                ResendApi(i + 1);
//                                            }
//                                        } else {
//                                            int mCount = data.arrayListData.get(i).getCountFail();
//                                            data.arrayListData.get(i).setCountFail(mCount++);
//                                            ResendApi(i + 1);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ApiRequet> call, Throwable t) {
//                                        int count = data.arrayListData.get(i).getCountFail();
//                                        data.arrayListData.set(i, new ItemModel(data.arrayListData.get(i).getPhone(), data.arrayListData.get(i).getMessage(), count++
//                                                , data.arrayListData.get(i).getDate_SMSArrived(), data.arrayListData.get(i).getDate_CallSuccessful(), 1));
//
//                                        ResendApi(i + 1);
//                                    }
//                                });
//                            } else {
//                                bodyModel model = new bodyModel(phone, message, dateFormat);
//                                CallApi.apiService.PostSMS(data.UrlApi, model).enqueue(new Callback<ApiRequet>() {
//                                    @Override
//                                    public void onResponse(Call<ApiRequet> call, Response<ApiRequet> response) {
//                                        if (response.code() == 200) {
//                                            if (response.body().getCode() == 0) {
//                                                data.arrayListData.set(i, new ItemModel(data.arrayListData.get(i).getPhone(), data.arrayListData.get(i).getMessage(), data.arrayListData.get(i).getCountFail()
//                                                        , data.arrayListData.get(i).getDate_SMSArrived(), formatter.format(date), 0));
//                                                ResendApi(i + 1);
//                                                adapterListView.notifyDataSetChanged();
//                                            } else {
//                                                int mCount = data.arrayListData.get(i).getCountFail();
//                                                data.arrayListData.get(i).setCountFail(mCount++);
//                                                ResendApi(i + 1);
//                                            }
//                                        } else {
//                                            Toast.makeText(getActivity(), data.arrayListData.get(i).getPhone() + "-Fail-[" + response.code() + "]", Toast.LENGTH_SHORT).show();
//                                            int mCount = data.arrayListData.get(i).getCountFail();
//                                            data.arrayListData.get(i).setCountFail(mCount++);
//                                            ResendApi(i + 1);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ApiRequet> call, Throwable t) {
//                                        int count = data.arrayListData.get(i).getCountFail();
//                                        data.arrayListData.set(i, new ItemModel(data.arrayListData.get(i).getPhone(), data.arrayListData.get(i).getMessage(), count++
//                                                , data.arrayListData.get(i).getDate_SMSArrived(), data.arrayListData.get(i).getDate_CallSuccessful(), 1));
//                                        Save();
//                                        ResendApi(i + 1);
//                                    }
//                                });
//
//                            }
//                        } catch (Exception e) {
//                            Log.i("Count", e.toString());
//                        }
//                    } else {
//                        ResendApi(i + 1);
//                    }
//                }
//            }
//        }
//    }

    static final String MY_PREFS_NAME = "MyPrefsFile";

    void Save() {
        SharedPreferences mPrefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data.arrayListData);
        prefsEditor.putString("arrDataKey", json);
        prefsEditor.apply();
    }


}