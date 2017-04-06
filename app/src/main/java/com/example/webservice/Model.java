package com.example.webservice;

/**
 * Created by Aditya on 3/28/2017.
 *
 * Updated to include the service type and itemupc
 */

public class Model {

    private String itemId;
    private String itemUPC;
    private String name;
    private String salePrice;
    private String thumbnailImage;
    private String itemPurchaseURL;
    private ServiceType type;

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemUPC() {
        return itemUPC;
    }

    public void setItemUPC(String itemUPC) {
        this.itemUPC = itemUPC;
    }

    @Override
    public String toString() {
        return "\nTitle: " + name + "\nPrice: " + salePrice + "\nUPC " + itemUPC + "\nThumbnail: " + thumbnailImage + "\nVendor ID: " + itemId;
    }
}
