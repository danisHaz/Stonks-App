package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;

public class SimpleStockTransporter {
    @SerializedName(value="symbol")
    public String symbol;

    @SerializedName(value="description")
    public String description;

    public SimpleStockTransporter(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }
}
