package com.example.stonksapp.financial.Components;

import com.google.gson.annotations.SerializedName;

public class SymbolQuery {
    @SerializedName(value="count")
    public int count;

    @SerializedName(value="result")
    public SingleResult[] resultArray;

    public static class SingleResult {
        @SerializedName(value="description")
        public String description;

        @SerializedName(value="displaySymbol")
        public String displaySymbol;

        @SerializedName(value="symbol")
        public String symbol;

        @SerializedName(value="type")
        public String type;
    }
}
