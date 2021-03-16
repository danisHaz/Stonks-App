package com.example.stonksapp.financial.Components;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DefaultStock {
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
    @NonNull
    public String symbol = "";

    @ColumnInfo(name="url")
    @SerializedName("weburl")
    public String url;

    @ColumnInfo(name="logo")
    @SerializedName("logo")
    public String logo;

    @ColumnInfo(name="price")
    public String price;

    // ???
    @ColumnInfo(name="finnhubIndustry")
    @SerializedName("finnhubIndustry")
    public String finnhubIndustry;

    @Ignore
    public static DefaultStock from(Stock stock) {
        DefaultStock defStock = new DefaultStock();
        defStock.capitalization = stock.capitalization;
        defStock.country = stock.country;
        defStock.currency = stock.currency;
        defStock.exchange = stock.exchange;
        defStock.finnhubIndustry = stock.finnhubIndustry;
        defStock.ipo = stock.ipo;
        defStock.logo = stock.logo;
        defStock.name = stock.name;
        defStock.outstanding = stock.outstanding;
        defStock.phone = stock.phone;
        defStock.price = stock.price;
        defStock.symbol = stock.symbol;
        defStock.url = stock.url;

        return defStock;
    }

    @Ignore
    private DefaultStock() { }

    public DefaultStock(@NonNull String symbol, @NonNull String country) {
        this.symbol = symbol;
        this.country = country;
    }

}
