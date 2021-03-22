package com.example.stonksapp.financial.Components;

import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Quote;
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

import android.util.Log;

@Entity
public class Stock implements Comparable<Stock> {
    @Ignore
    private static HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();

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

    @ColumnInfo(name="percents")
    public String percents;

    // ???
    @ColumnInfo(name="finnhubIndustry")
    @SerializedName("finnhubIndustry")
    public String finnhubIndustry;

    public Stock(@NonNull String symbol, @Nullable String country) {
        this.symbol = symbol;
        this.country = country;
        this.price = "N/A";

        countPercentage(getter);
    }

    @Ignore
    private Stock() { }

    @Deprecated
    @Ignore
    public Stock(@NonNull String symbol, @Nullable String country, @Nullable String price) {
        this.symbol = symbol;
        this.country = country;
        this.price = price;

        countPercentage(getter);
    }

    @Ignore
    public Stock(@NonNull String symbol, String name, @Nullable String country, @Nullable String price) {
        this.symbol = symbol;
        this.name = name;
        this.country = country;
        this.price = price;

        countPercentage(getter);
    }

    @Ignore
    public void countPercentage(final HTTPSRequestClient.GET getter) {
        if (getter == null) {
            Log.e("Err", "Request client is null when count percentage");
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    Quote quote = getter.quote(String.format(
                            Constants.GET_QUOTE_TEMPLATE, symbol, Constants.API_TOKEN));
                    double myPercents =
                            ((Double.parseDouble(quote.current)) / (Double.parseDouble(quote.previous)) - (double) 1) * 100;
                    percents = String.format("%.2f", myPercents);
                    price = quote.current;
                } catch (NullPointerException e) {
                    Log.e("Err", "Some symbol not found");
                }
            }
        });

        thread.start();
    }

    @Ignore
    public void mergeNonNull(Stock stock) {
        this.capitalization = stock.capitalization == null ?
                this.capitalization : stock.capitalization;
        this.country = stock.country == null ? this.country : stock.country;
        this.currency = stock.currency == null ? this.currency : stock.currency;
        this.exchange = stock.exchange == null ? this.exchange : stock.exchange;
        this.finnhubIndustry = stock.finnhubIndustry == null ?
                this.finnhubIndustry : stock.finnhubIndustry;
        this.ipo = stock.ipo == null ? this.ipo : stock.ipo;
        this.logo = stock.logo == null ? this.logo : stock.logo;
        this.name = stock.name == null ? this.name : stock.name;
        this.outstanding = stock.outstanding == null ? this.outstanding : stock.outstanding;
        this.phone = stock.phone == null ? this.phone : stock.phone;
        this.price = stock.price == null ? this.price : stock.price;
        this.symbol = stock.symbol;
        this.url = stock.url == null ? this.url : stock.url;
        this.percents = stock.percents == null ? this.percents : stock.percents;
    }

    @Ignore
    public static Stock from(TradesPrices trades) {
        TradesData data = trades.data[trades.data.length - 1];
//        Stock newStock = new Stock(data.symbol, null, null, data.price);
        Stock newStock = new Stock();
        newStock.symbol = data.symbol;
        newStock.price = data.price;
        return newStock;
    }

    @Ignore
    public static Stock from(SingleResult res) {
//        return new Stock(res.symbol, res.description, "US", null);
        Stock stockie = new Stock();
        stockie.symbol = res.symbol;
        stockie.name = res.description;
        stockie.exchange = "US";
        return stockie;
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
        if (stock.percents == null)
            defStock.countPercentage(getter);
        defStock.percents = stock.percents;

        return defStock;
    }

    @Ignore
    public int compareTo(Stock newStock) {
        return this.symbol.compareTo(newStock.symbol);
    }
}
