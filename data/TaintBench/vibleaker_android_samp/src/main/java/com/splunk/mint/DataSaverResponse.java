package com.splunk.mint;

public class DataSaverResponse {
    private String data;
    private Exception exception;
    private String filepath;
    private Boolean savedSuccessfully;

    protected DataSaverResponse(String data, String filepath) {
        this.data = data;
        this.filepath = filepath;
    }

    public Exception getException() {
        return this.exception;
    }

    /* access modifiers changed from: protected */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Boolean getSavedSuccessfully() {
        return this.savedSuccessfully;
    }

    /* access modifiers changed from: protected */
    public void setSavedSuccessfully(Boolean savedSuccessfully) {
        this.savedSuccessfully = savedSuccessfully;
    }

    public String getData() {
        return this.data;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public String toString() {
        return "DataSaverResponse [data=" + this.data + ", filepath=" + this.filepath + ", exception=" + this.exception + ", savedSuccessfully=" + this.savedSuccessfully + "]";
    }
}
