package com.usexpress.myapplication.Fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.usexpress.myapplication.DataSetting;
import com.usexpress.myapplication.Model.DataModel;
import com.usexpress.myapplication.Model.TokenModel;
import com.usexpress.myapplication.R;
import com.usexpress.myapplication.Service.GetTokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    DataSetting data;
    static final String MY_PREFS_NAME = "MyPrefsFile";
    androidx.appcompat.widget.AppCompatButton btSave;
    EditText edtUser, edtPass;
    MultiAutoCompleteTextView edtLink, edtAllPhone;

    public SettingFragment() {
        // Required empty public constructor
    }
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        FindView(view);
        even();
        if (data.dataProfile != null) {
            setText();
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void even() {
        edtLink.setText("https://api.usexpressglobal.com/api/app/sms/receive");
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtLink.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Link is not have data", Toast.LENGTH_SHORT).show();
                } else if (edtAllPhone.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "AllowPhone is not have data", Toast.LENGTH_SHORT).show();
                } else {
                    Save();
                    Toast.makeText(getActivity(), "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void FindView(View view) {
        btSave = view.findViewById(R.id.btSave);
        edtUser = view.findViewById(R.id.edtUser);
        edtPass = view.findViewById(R.id.edtPass);
        edtLink = view.findViewById(R.id.edtLink);
        edtAllPhone = view.findViewById(R.id.edtAllPhone);
    }

    void setText() {
        if(data.dataProfile.AllowPhone==null || data.dataProfile.AllowPhone.equals("")){
            edtAllPhone.setText("Techcombank");
        }else{
            edtAllPhone.setText(data.dataProfile.AllowPhone);
        }

        if(data.dataProfile.Pass == null || data.dataProfile.Pass.equals("")){
            edtPass.setText("7SgwVem7x34Ss9YYeY8W6UUnzxxCsVXSpBqP734Ss9YY7");
        }else{
            edtPass.setText(data.dataProfile.Pass);
        }
        edtUser.setText(data.dataProfile.User);
        if(data.dataProfile.Link!=null){
            edtLink.setText(data.dataProfile.Link);
        }
    }

    void Save() {
        SharedPreferences mPrefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        DataModel mData = new DataModel();
        mData.User = edtUser.getText().toString();
        mData.Pass = edtPass.getText().toString();
        mData.Link = edtLink.getText().toString();
        if(mData.User.equals("")){
            data.isUrlToken = true;
        }
        mData.AllowPhone = edtAllPhone.getText().toString();
        data.dataProfile = mData;
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mData);
        prefsEditor.putString("DataKey", json);
        prefsEditor.putBoolean("UrlTokenKey", data.isUrlToken);
        prefsEditor.apply();
        getData();
    }

    void getData() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String jsonModel = sharedPref.getString("DataKey", "");
        if(data.dataProfile.User.equals("")){ 
            data.isUrlToken = sharedPref.getBoolean("UrlTokenKey", true);
        }
        Gson gson = new Gson();
        DataModel datas = gson.fromJson(jsonModel, DataModel.class);
        data.dataProfile = datas;
        GetDomainAndUrl(data.dataProfile.Link);
        setText();
        CallApi();
    }

    private void GetDomainAndUrl(String s) {
        String url = "";
        String[] parts = s.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if(i==0){
                url = url + parts[i];
            }else{

                url = url +"/"+ parts[i];
            }
        }
        data.DoMain = url+"/";
        data.UrlApi = parts[parts.length-1];
    }
    void DialogS(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    void CallApi() {
        if (!data.dataProfile.User.equals("")&&!data.dataProfile.Pass.equals("")) {
            try {
                GetTokenService.ApiService.GetToken(data.dataProfile.User, data.dataProfile.Pass, "password").enqueue(new Callback<TokenModel>() {
                    @Override
                    public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                        if(response.code()==200){
                            data.Token = response.body().access_token;
                            data.isUrlToken=false;
                            DialogS("Đăng nhập thành công");
                        }else{
                            DialogS("Đăng nhập thất bại");
                        }
                    }
                    @Override
                    public void onFailure(Call<TokenModel> call, Throwable t) {
                        DialogS("Đăng nhập thất bại");
                    }
                });
            } catch (Exception e) {
                DialogS("Lỗi đăng nhập 3312");
            }
        } else {
            data.isUrlToken = true;
            Toast.makeText(getActivity(), "Sử dụng Api chứ Token", Toast.LENGTH_SHORT).show();
        }
    }
}