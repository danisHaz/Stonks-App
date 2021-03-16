package com.example.stonksapp.UI.Activities;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Fragments.ManageFavouriteStonksFragment;
import com.example.stonksapp.UI.Fragments.WatchCurrentStonksFragment;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Components.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static byte defaultFragment = 0;
    private WatchCurrentStonksFragment watchCurrentStonksFragment;
    private ManageFavouriteStonksFragment manageFavouriteStonksFragment;

    public static int firstLaunchOrNot = 1;

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
            WatchingStocks.define(this);
            FavouriteStock.defineDB(this);

            watchCurrentStonksFragment =
                    WatchCurrentStonksFragment.createInstance(null, this);

            manageFavouriteStonksFragment =
                    ManageFavouriteStonksFragment.createInstance(null, this);

            this.setDefaultFragment();
            Toolbar mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
            setSupportActionBar(mToolbar);

            BackgroundTaskHandler.subscribeOnLastPriceUpdates(this, Constants.CURRENT_CLASS_IS_WATCHING);
            BackgroundTaskHandler.subscribeOnLastPriceUpdates(this, Constants.CURRENT_CLASS_IS_FAVOURITE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(this, Constants.CURRENT_CLASS_IS_WATCHING);
        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(this, Constants.CURRENT_CLASS_IS_FAVOURITE);
        FavouriteStock.destroy();
        WatchingStocks.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item.getActionView();
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setIconified(false);
        searchView.setSearchableInfo(manager.getSearchableInfo(new ComponentName(
                this, SearchableActivity.class)));

        return super.onCreateOptionsMenu(menu);
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
                .replace(R.id.frag, manageFavouriteStonksFragment,
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