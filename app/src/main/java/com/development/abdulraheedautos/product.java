package com.development.abdulraheedautos;

import org.json.JSONObject;

public class product {
    private String serialNo;
    private String productName;
    private String category;
    private int price;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("SERIAL : "+this.serialNo);
        sb.append(", PRODUCT : "+this.productName);
        sb.append(", CATEGORY : "+this.category);
        sb.append(", PRICE : "+this.price);
       return sb.toString();
    }
}
