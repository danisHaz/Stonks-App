package com.example.stonksapp.financial.Components;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Quote;
import com.google.gson.annotations.SerializedName;

@Entity
public class DefaultStock {
    @Ignore
    public static HTTPSRequestClient.GET getter = new HTTPSRequestClient.GET();

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
        defStock.percents = stock.percents;
        if (stock.percents == null)
            defStock.countPercentage(getter);

        return defStock;
    }

    @Ignore
    private DefaultStock() { }

    public DefaultStock(@NonNull String symbol, @NonNull String country) {
        this.symbol = symbol;
        this.country = country;
        countPercentage(getter);
    }

    @Ignore
    public void countPercentage(HTTPSRequestClient.GET getter) {
        if (getter == null) {
            Log.e("Err", "Request client is null when count percentage");
            return;
        }

        if (!MainActivity.ifNetworkProvided)
            return;

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

}
