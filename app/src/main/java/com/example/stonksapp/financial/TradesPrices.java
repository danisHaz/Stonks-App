package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;

public class TradesPrices {
    @SerializedName("type")
    public String type;

    @SerializedName("data")
    public TradesData[] data;
}
