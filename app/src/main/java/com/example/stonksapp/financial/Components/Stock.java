package com.example.stonksapp.financial.Components;

import com.example.stonksapp.financial.TradesPrices;
import com.example.stonksapp.financial.TradesData;
import com.example.stonksapp.financial.Components.SymbolQuery.SingleResult;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.lang.Comparable;

@Entity
public class Stock implements Comparable<Stock> {
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

    public Stock(@NonNull String symbol, @Nullable String country) {
        this.symbol = symbol;
        this.country = country;
        this.price = "N/A";
    }

    @Ignore
    private Stock() { }

    @Deprecated
    @Ignore
    public Stock(@NonNull String symbol, @Nullable String country, @Nullable String price) {
        this.symbol = symbol;
        this.country = country;
        this.price = price;
    }

    @Ignore
    public Stock(@NonNull String symbol, String name, @Nullable String country, @Nullable String price) {
        this.symbol = symbol;
        this.name = name;
        this.country = country;
        this.price = price;
    }

    @Ignore
    public void mergeNonNull(Stock stock) {
        this.capitalization = stock.capitalization == null ? this.capitalization : stock.capitalization;
        this.country = stock.country == null ? this.country : stock.country;
        this.currency = stock.currency == null ? this.currency : stock.currency;
        this.exchange = stock.exchange == null ? this.exchange : stock.exchange;
        this.finnhubIndustry = stock.finnhubIndustry == null ? this.finnhubIndustry : stock.finnhubIndustry;
        this.ipo = stock.ipo == null ? this.ipo : stock.ipo;
        this.logo = stock.logo == null ? this.logo : stock.logo;
        this.name = stock.name == null ? this.name : stock.name;
        this.outstanding = stock.outstanding == null ? this.outstanding : stock.outstanding;
        this.phone = stock.phone == null ? this.phone : stock.phone;
        this.price = stock.price == null ? this.price : stock.price;
        this.symbol = stock.symbol;
        this.url = stock.url == null ? this.url : stock.url;
    }

    @Ignore
    public static Stock from(TradesPrices trades) {
        TradesData data = trades.data[trades.data.length - 1];
        Stock newStock = new Stock(data.symbol, null, null, data.price);
        return newStock;
    }

    @Ignore
    public static Stock from(SingleResult res) {
        return new Stock(res.symbol, res.description, "US", null);
    }

    @Ignore
    public static Stock from(DefaultStock stock) {
        Stock defStock = new Stock();
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
    public int compareTo(Stock newStock) {
        return this.symbol.compareTo(newStock.symbol);
    }
}
