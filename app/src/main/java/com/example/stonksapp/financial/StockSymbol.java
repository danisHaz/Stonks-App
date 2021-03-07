package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;

public class StockSymbol {
    @SerializedName("currency")
    public String currency;

    @SerializedName("description")
    public String description;

    @SerializedName("displaySymbol")
    public String displaySymbol;

    @SerializedName("figi")
    public String figi;

    @SerializedName("mic")
    public String MICCode;

    @SerializedName("symbol")
    public String symbol;

    @SerializedName("type")
    public String type;
}
