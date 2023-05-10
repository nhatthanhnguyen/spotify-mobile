package com.ptit.spotify.dto.data;

public class UserSettingsHeaderData {
    private String accountImageUrl;
    private String accountName;

    public UserSettingsHeaderData(String accountImageUrl, String accountName) {
        this.accountImageUrl = accountImageUrl;
        this.accountName = accountName;
    }

    public String getAccountImageUrl() {
        return accountImageUrl;
    }

    public void setAccountImageUrl(String accountImageUrl) {
        this.accountImageUrl = accountImageUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
