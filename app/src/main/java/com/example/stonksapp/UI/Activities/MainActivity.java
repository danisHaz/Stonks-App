package com.example.stonksapp.UI.Activities;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.UI.Components.WorkDoneListener;
import com.example.stonksapp.UI.Fragments.ManageFavouriteStonksFragment;
import com.example.stonksapp.UI.Fragments.WatchCurrentStonksFragment;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Components.*;
import com.example.stonksapp.UI.Fragments.LoadingFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static byte defaultFragment = 0;
    private WatchCurrentStonksFragment watchCurrentStonksFragment;
    private ManageFavouriteStonksFragment manageFavouriteStonksFragment;
    private static volatile int definitionWorksDone = 0;
    public static boolean ifNetworkProvided;
    private static MainActivity currentActivity;

    public static synchronized void definitionWorksListener() {
        if (++definitionWorksDone == 2) {
            if (ifNetworkProvided) {
                try {
                    WorkDoneListener.complete(Constants.DO_DAILY_WORK, OnCompleteListener.Result.SUCCESS);
                } catch (NullPointerException e) {
//                    pass
                }
            }
            if (currentActivity == null)
                return;

            currentActivity.setDefaultFragment();

            if (ifNetworkProvided) {
                BackgroundTaskHandler.createConnection(currentActivity);
                BackgroundTaskHandler.subscribeOnLastPriceUpdates(
                        Constants.toStringArray(WatchingStocks.getSymbols()), currentActivity);
                BackgroundTaskHandler.subscribeOnLastPriceUpdates(
                        Constants.toStringArray(FavouriteStock.getSymbols()), currentActivity);
            }
        }
    }

    public WatchCurrentStonksFragment getWatchStonksFragment() {
        return watchCurrentStonksFragment;
    }

    public ManageFavouriteStonksFragment getManageFavouriteFragment() {
        return manageFavouriteStonksFragment;
    }

    public static String attachedFragmentTag = Constants.WATCH_STONKS_TAG;
    public static boolean ifNothingAttached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ifNetworkProvided = Constants.isNetworkConnectionProvided(this);

        setContentView(R.layout.activity_main);
        currentActivity = this;

        if (BackgroundTaskHandler.myDb != null)
            return;

        BackgroundTaskHandler.defineDB(this);
        WatchingStocks.define(this);
        FavouriteStock.define(this);

        watchCurrentStonksFragment =
                WatchCurrentStonksFragment.createInstance(null, this);

        manageFavouriteStonksFragment =
                ManageFavouriteStonksFragment.createInstance(null, this);

        LoadingFragment loading = LoadingFragment.createInstance();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, loading, "Loading")
                .commit();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!ifNetworkProvided)
            Toast.makeText(this, "Network is not provided. Please reload app",
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(
                Constants.toStringArray(FavouriteStock.getSymbols()), this);
        BackgroundTaskHandler.unsubscribeFromLastPriceUpdates(
                Constants.toStringArray(WatchingStocks.getSymbols()), this);
        BackgroundTaskHandler.destroyConnection();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (!ifNetworkProvided)
            return super.onCreateOptionsMenu(menu);

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
        Button button = (Button) view;
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_trending_up_36, 0, 0);

        ((Button)findViewById(R.id.manageFavourites))
                .setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_star_36, 0, 0);

        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);

        if (definitionWorksDone != 2) {
            defaultFragment = 0;
            return;
        }

        if (fragment.getTag() != Constants.WATCH_STONKS_TAG)
            this.setWatchCurrentStonksFragment();
    }

    public void onManageFavouriteStonksClick(View view) {
        Button button = (Button) view;
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                R.drawable.ic_baseline_star_36_enabled, 0, 0);

        ((Button)findViewById(R.id.currentStonks))
                .setCompoundDrawablesRelativeWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_trending_up_36_disabled, 0, 0);

        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);

        if (definitionWorksDone != 2) {
            defaultFragment = 1;
            return;
        }

        if (fragment.getTag() != Constants.MANAGE_YOUR_FAVOURITES_TAG)
            this.setManageFavouritesStonksFragment();
    }
}