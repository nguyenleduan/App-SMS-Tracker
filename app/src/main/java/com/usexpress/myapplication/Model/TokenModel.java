package com.usexpress.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class TokenModel {
    public  String access_token;
    public  String token_type;
    public  int expires_in;
    @SerializedName(".issued")
    public  String issued;
    @SerializedName(".expires")
    public  String expires;

    public TokenModel(String access_token, String token_type, int expires_in, String issued, String expires) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.issued = issued;
        this.expires = expires;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
