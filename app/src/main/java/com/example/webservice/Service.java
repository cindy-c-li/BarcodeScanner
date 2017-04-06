package com.example.webservice;

/**
 * Created by Anand Rawat on 02-04-2017.
 */

public abstract class Service {
    private ServiceType type;
    private String endPointURL;
    private String upc;

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getEndPointURL() {
        return endPointURL;
    }

    public void setEndPointURL(String endPointURL) {
        this.endPointURL = endPointURL;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public abstract Model dispatchRequest();
}
