package com.nabto.api;

public class UrlFetchResult {
    private byte[] result;
    private String mimeType;
    private NabtoStatus nabtoStatus;

    public UrlFetchResult(byte[] result, String mimeType, int nabtoStatus) {
        this.result = result;
        this.mimeType = mimeType;
        this.nabtoStatus = NabtoStatus.fromInteger(nabtoStatus);
    }

    public byte[] getResult() {
        return result;
    }

    public String getMimetype() {
        return mimeType;
    }

    public NabtoStatus getNabtoStatus() {
        return nabtoStatus;
    }
}