package com.example.webservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand Rawat on 06-04-2017.
 */

public class RequestDispatcher {
    private static RequestDispatcher instance;

    private RequestDispatcher() {
    }

    public static RequestDispatcher getInstance() {
        if (instance == null)
            instance = new RequestDispatcher();
        return instance;
    }

    public List<Model> SendRquests(String upc) {
        List<Model> responses = null;
        if (upc != null && !upc.isEmpty()) {
            responses = new ArrayList<>();
            Service walmart = new WalmartService();
            walmart.setUpc(upc);
            Model walmartRes = walmart.dispatchRequest();
            if (walmartRes != null)
                responses.add(walmartRes);
            Service amazon = new AmazonService();
            amazon.setUpc(upc);
            Model amazonRes = amazon.dispatchRequest();
            if (amazonRes != null)
                responses.add(amazonRes);
        }
        return responses;
    }
}
