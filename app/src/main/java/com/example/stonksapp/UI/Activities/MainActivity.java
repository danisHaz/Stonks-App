package com.example.stonksapp.UI.Activities;
import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Fragments.ManageFavouriteStonksFragment;
import com.example.stonksapp.UI.Fragments.WatchCurrentStonksFragment;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.StockSymbol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static byte defaultFragment = 0;
    private WatchCurrentStonksFragment watchCurrentStonksFragment;
    private ManageFavouriteStonksFragment manageFavouriteStonksFragment;
    private static ArrayList<String> symbolArray = new ArrayList<String>();
    private static ArrayList<String> prices = new ArrayList<String>();

    public static boolean isAttached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int properContentView = Constants.isNetworkConnectionProvided(this)
                ? R.layout.activity_main : R.layout.activity_main_no_network;

        setContentView(properContentView);

        if (properContentView == R.layout.activity_main && savedInstanceState == null) {

            HTTPSRequestClient client = new HTTPSRequestClient();
            StockSymbol[] curArray = client.GET(String.format(
                    Constants.GET_STOCK_SYMBOLS_TEMPLATE, "US", Constants.API_TOKEN));

            for (int pos = 0; pos < 10; pos++) {
                symbolArray.add(curArray[pos].symbol);
                prices.add("N/A");
            }

            symbolArray.add("AAPL");
            prices.add("N/A");

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("symbolArray", symbolArray);
            bundle.putStringArrayList("priceArray", prices);
            watchCurrentStonksFragment =
                    WatchCurrentStonksFragment.createInstance(bundle, this);

            this.setDefaultFragment();

            BackgroundTaskHandler.subscribeOnLastPriceUpdates(watchCurrentStonksFragment,
                    this, Constants.toStringArray(symbolArray));
        }

        // TODO: make some defines of static things

    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: start some background work here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(this,
                (String[]) symbolArray.toArray(),
                (byte)0);
    }

    public void refresh() {
    }

    private void setDefaultFragment() {
        switch (defaultFragment) {
            case 0:
                this.setWatchCurrentStonksFragment();
                break;
            case 1:
                this.setManageFavouritesStonksFragment();
                break;
            default:
                // nothing to do with it
                break;
        }
    }

    private void setWatchCurrentStonksFragment() {
        Log.d("Tag", "set watch current stonks fragment");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, watchCurrentStonksFragment,
                        Constants.WATCH_STONKS_TAG)
                .commit();

        isAttached = true;
    }

    private void setManageFavouritesStonksFragment() {
        Log.d("Tag", "set manage favourites stonks fragment");
        isAttached = false;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ManageFavouriteStonksFragment.createInstance(),
                        Constants.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();
    }

    public void onWatchCurrentStonksClick(View view) {
        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);
        if (fragment.getTag() != Constants.WATCH_STONKS_TAG)
            this.setWatchCurrentStonksFragment();
    }

    public void onManageFavouriteStonksClick(View view) {
        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);
        if (fragment.getTag() != Constants.MANAGE_YOUR_FAVOURITES_TAG)
            this.setManageFavouritesStonksFragment();
    }
}