package com.development.abdulraheedautos;

public class Constants {
    static final String ROW_ID="serialNo";
    static final String PRODUCT="product";
    static final String CATEGORY="category";
    static final String PRICE="price";
    static final String DB_NAME="items_Data";
    static final String TB_NAME="items";
    static final int DB_VERSION=1;
    static final String CREATE_TB="CREATE TABLE IF NOT EXISTS "+TB_NAME+"(serialNo INTEGER PRIMARY KEY,product varchar,category varchar,price int(4));";
    static final String DROP_TB="DROP TABLE IF EXISTS "+TB_NAME;

}
