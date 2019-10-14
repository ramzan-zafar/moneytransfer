package com.moneytransfer.response;

public enum StatusResponse {
    SUCCESS ("Success"),
    ERROR ("Error");

    private String status;

    StatusResponse(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}