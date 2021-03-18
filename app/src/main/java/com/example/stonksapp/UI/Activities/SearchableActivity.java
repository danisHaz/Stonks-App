package com.example.stonksapp.UI.Activities;

import com.example.stonksapp.R;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.SimpleStockTransporter;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.ifNothingAttached = true;

        setContentView(R.layout.activity_searchable);

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Toolbar mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            String query = intent.getStringExtra(SearchManager.QUERY);

            HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
            final SymbolQuery result = client.symbolLookup(String.format(
                    Constants.GET_SYMBOL_LOOKUP_TEMPLATE, query, Constants.API_TOKEN
            ));

            ArrayList<String> list = new ArrayList<>();
            for (SymbolQuery.SingleResult res: result.resultArray) {
                list.add(res.description);
            }

            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);

            ListView listView  = (ListView) findViewById(R.id.searchableView);
            if (mAdapter.isEmpty()) {
                mAdapter.add(getResources().getString(R.string.helloThere));
                listView.setAdapter(mAdapter);
                return;
            }

            ((TextView) findViewById(R.id.yourListView)).setText(R.string.yourResults);

            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adap, View view, int pos, long id) {
                    FavouriteStock.addToFavourites(new Stock(result.resultArray[pos].symbol,
                            result.resultArray[pos].description,"US", null));

                    BackgroundTaskHandler.subscribeOnLastPriceUpdates(
                            new String[]{result.resultArray[pos].symbol});

                    Toast.makeText(SearchableActivity.this, String.format(
                            "%s added to favourites", result.resultArray[pos].symbol),
                            Toast.LENGTH_LONG).show();

                }
            });
            listView.setAdapter(mAdapter);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String tup = "AAPL";
            try {
                tup = intent.getDataString();
            } catch (java.lang.IllegalStateException e) {
                Log.e("Err", "Illegal State Exception in ACTION_VIEW");
            }

            if (tup == null) {
                Log.e("Err", "Chosen suggestion provided null symbol");
                finish();
            }

            SimpleStockTransporter transporter = (new GsonBuilder().create())
                    .fromJson(tup, SimpleStockTransporter.class);

            try {
                FavouriteStock.addToFavourites(new Stock(transporter.symbol, transporter.description, "US", null));
                BackgroundTaskHandler.subscribeOnLastPriceUpdates(new String[]{tup});
            } catch (java.lang.NullPointerException e) {
                Log.e("Err", "Chosen symbol in suggestions is null");
            }
            finish();
        } else {
            Log.e("Err", "Wrong query to SearchableActivity");
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.ifNothingAttached = false;
    }
}