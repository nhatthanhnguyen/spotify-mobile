package com.ptit.spotify.dto.model;

public class AccountResponse {
    private Account account;
    private StatusError statusError;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public StatusError getStatusError() {
        return statusError;
    }

    public void setStatusError(StatusError statusError) {
        this.statusError = statusError;
    }
}