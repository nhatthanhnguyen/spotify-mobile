package com.ptit.spotify.dto.model;

public class StatusError {
    private int returnCode;
    private String returnMessage;
    private int subReturnCode;
    private String subReturnMessage;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public int getSubReturnCode() {
        return subReturnCode;
    }

    public void setSubReturnCode(int subReturnCode) {
        this.subReturnCode = subReturnCode;
    }

    public String getSubReturnMessage() {
        return subReturnMessage;
    }

    public void setSubReturnMessage(String subReturnMessage) {
        this.subReturnMessage = subReturnMessage;
    }
}