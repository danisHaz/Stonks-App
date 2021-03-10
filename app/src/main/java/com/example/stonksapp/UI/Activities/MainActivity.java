package com.example.stonksapp.UI.Activities;
import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Fragments.ManageFavouriteStonksFragment;
import com.example.stonksapp.UI.Fragments.WatchCurrentStonksFragment;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Components.*;

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

    public WatchCurrentStonksFragment getWatchStonksFragment() {
        return watchCurrentStonksFragment;
    }

    public ManageFavouriteStonksFragment getManageFavouriteFragment() {
        return manageFavouriteStonksFragment;
    }

    public static String attachedFragmentTag = Constants.WATCH_STONKS_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int properContentView = Constants.isNetworkConnectionProvided(this)
                ? R.layout.activity_main : R.layout.activity_main_no_network;

        setContentView(properContentView);

        if (properContentView == R.layout.activity_main && savedInstanceState == null) {

            WatchingStocks.define();

            watchCurrentStonksFragment =
                    WatchCurrentStonksFragment.createInstance(null, this);

            manageFavouriteStonksFragment =
                    ManageFavouriteStonksFragment.createInstance(null);

            this.setDefaultFragment();

        }

        // TODO: make some defines of static things

    }

    @Override
    public void onStart() {
        super.onStart();

        BackgroundTaskHandler.subscribeOnLastPriceUpdates(
                this, (String[]) WatchingStocks.getSymbols().toArray());

        FavouriteStock.defineDB(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(this,
                (String[]) WatchingStocks.getSymbols().toArray(),
                (byte)0);
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
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, watchCurrentStonksFragment,
                        Constants.WATCH_STONKS_TAG)
                .commit();

        attachedFragmentTag = Constants.WATCH_STONKS_TAG;
    }

    private void setManageFavouritesStonksFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ManageFavouriteStonksFragment.createInstance(new Bundle()),
                        Constants.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();

        attachedFragmentTag = Constants.MANAGE_YOUR_FAVOURITES_TAG;
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