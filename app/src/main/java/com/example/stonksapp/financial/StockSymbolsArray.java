package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;
import com.example.stonksapp.financial.StockSymbol;

public class StockSymbolsArray {
    @SerializedName("type")
    public StockSymbol[] arr;
}
