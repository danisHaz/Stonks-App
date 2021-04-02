package com.example.stonksapp.UI.Activities;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.UI.Fragments.LoadingFragment;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.UI.Components.WorkDoneListener;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.SimpleStockTransporter;
import com.google.gson.GsonBuilder;
import com.example.stonksapp.UI.Fragments.SearchableFragment;

import android.app.SearchManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    private static final ArrayList<Stock> list = new ArrayList<>();
    protected static SearchableActivity activity;
    private static SearchableFragment fragment;
    private static LoadingFragment loading;
    public static boolean isStopped = false;

    public void actionSearch(final String stockQuery) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loading = LoadingFragment.createInstance();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_searchable, loading, "Loading")
                .commit();

        Bundle bundle = new Bundle();
        bundle.putString("QUERY", stockQuery);
        fragment = SearchableFragment.createInstance(bundle, activity);

        WorkDoneListener.setNewListener(new OnCompleteListener() {
            @Override
            public synchronized void doWork() {
                if (!isStopped) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_searchable, fragment, "fragmentSearchable")
                            .commit();
                } else
                    this.setWaiting();
            }
        }.setTag(Constants.DO_SEARCH_WORK));
    }

    public void actionView(Intent intent) {
        String tup = "AAPL";
        try {
            tup = intent.getDataString();
        } catch (java.lang.IllegalStateException e) {
            Log.e("SearchableActivity", "Illegal State Exception in ACTION_VIEW");
        }

        if (tup == null) {
            Log.e("SearchableActivity", "Chosen suggestion provided null symbol");
            finish();
        }

        SimpleStockTransporter transporter = (new GsonBuilder().create())
                .fromJson(tup, SimpleStockTransporter.class);

        try {
            FavouriteStock.addToFavourites(new Stock(
                    transporter.symbol, transporter.description, "US", null, true));
            BackgroundTaskHandler.subscribeOnLastPriceUpdates(new String[]{tup}, this);
        } catch (java.lang.NullPointerException e) {
            Log.e("SearchableActivity", "Chosen symbol in suggestions is null");
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.ifNothingAttached = true;

        setContentView(R.layout.activity_searchable);

        activity = this;
        Intent intent = getIntent();
        final String stockQuery = intent.getStringExtra(SearchManager.QUERY);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            actionSearch(stockQuery);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            actionView(intent);
        } else {
            Log.e("SearchableActivity", "Wrong query to SearchableActivity");
            onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isStopped = false;

        try {
            if (WorkDoneListener.isListenerWaiting(Constants.DO_SEARCH_WORK))
                WorkDoneListener.complete(Constants.DO_SEARCH_WORK, OnCompleteListener.Result.SUCCESS);
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            Log.e("SearchableActivity", "Work is not set or not yet ready to be completed");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        isStopped = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            fragment.refresh();
        } catch (NullPointerException e) {
            Log.d("SearchableActivity", "quick search before initialization");
        }
    }

}