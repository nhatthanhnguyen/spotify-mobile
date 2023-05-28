package com.ptit.spotify.dto.model;
import java.util.List;

public class AccountsResponse {
    private List<Account> accounts;
    private StatusError statusError;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public StatusError getStatusError() {
        return statusError;
    }

    public void setStatusError(StatusError statusError) {
        this.statusError = statusError;
    }
}