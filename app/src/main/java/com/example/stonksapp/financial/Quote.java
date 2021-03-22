package com.example.stonksapp.financial;

import com.google.gson.annotations.SerializedName;

public class Quote {
    @SerializedName(value="c")
    public String current;

    @SerializedName(value="h")
    public String high;

    @SerializedName(value="l")
    public String low;

    @SerializedName(value="pc")
    public String previous;

    @SerializedName(value="o")
    public String open;
}
