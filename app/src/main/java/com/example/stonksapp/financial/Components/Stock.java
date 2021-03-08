package com.example.stonksapp.financial.Components;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class Stock {
    @ColumnInfo(name="country")
    @SerializedName("country")
    public String country;

    @ColumnInfo(name="currency")
    @SerializedName("currency")
    public String currency;

    @ColumnInfo(name="exchange")
    @SerializedName("exchange")
    public String exchange;

    @ColumnInfo(name="ipo")
    @SerializedName("ipo")
    public String ipo;

    @ColumnInfo(name="capitalization")
    @SerializedName("marketCapitalization")
    public String capitalization;

    @ColumnInfo(name="name")
    @SerializedName("name")
    public String name;

    @ColumnInfo(name="phone")
    @SerializedName("phone")
    public String phone;

    @ColumnInfo(name="outstanding")
    @SerializedName("shareOutstanding")
    public String outstanding;

    @PrimaryKey
    @SerializedName("ticker")
    public String symbol;

    @ColumnInfo(name="url")
    @SerializedName("weburl")
    public String url;

    @ColumnInfo(name="logo")
    @SerializedName("logo")
    public String logo;

    // ???
    @ColumnInfo(name="finnhubIndustry")
    @SerializedName("finnhubIndustry")
    public String finnhubIndustry;

    Stock(@NonNull String symbol, @Nullable String country) {
        this.symbol = symbol;
        this.country = country;
    }
}
