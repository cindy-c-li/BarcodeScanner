package com.example.chen.barcodescanner.model;

/**
 * Created by Aditya on 3/28/2017.
 */

public class Model {

    private int itemId;
    private String name;
    private String salePrice;
    private String thumbnailImage;
    private String itemPurchaseURL;

    public String getItemPurchaseURL() {
        return itemPurchaseURL;
    }

    public void setItemPurchaseURL(String itemPurchaseURL) {
        this.itemPurchaseURL = itemPurchaseURL;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
