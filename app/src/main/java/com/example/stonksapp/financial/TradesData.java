package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;

public class TradesData {
    @SerializedName("p")
    public String lastPrice;

    @SerializedName("s")
    public String symbol;

    @SerializedName("t")
    public long timestamp;

    @SerializedName("v")
    public double volume;

    // ???
    @SerializedName("c")
    public String conditions;

}
