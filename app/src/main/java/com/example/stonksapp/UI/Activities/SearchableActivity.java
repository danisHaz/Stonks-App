package com.example.stonksapp.UI.Activities;

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

import com.example.stonksapp.R;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.Constants;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Log.d("DEbuG", "Eboyyy");

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
            SymbolQuery result = client.symbolLookup(String.format(
                    Constants.GET_SYMBOL_LOOKUP_TEMPLATE, query, Constants.API_TOKEN
            ));

            ArrayList<String> list = new ArrayList<>();
            for (SymbolQuery.SingleResult res: result.resultArray) {
                list.add(res.symbol);
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
                    String mSymbol = (String) adap.getItemAtPosition(pos);
                    FavouriteStock.addToFavourites(new Stock(mSymbol, "US"));
                    Toast.makeText(SearchableActivity.this, String.format(
                            "%s added to favourites", mSymbol), Toast.LENGTH_LONG).show();

                }
            });
            listView.setAdapter(mAdapter);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String tup = "AAPL";
            try {
                tup = intent.getDataString();
            } catch (java.lang.IllegalStateException e) {
                Log.d("Err", "jopa");
            }
            if (tup == null) {
                Log.e("Err", "Chosen suggestion provided null symbol");
                finish();
            }
            FavouriteStock.addToFavourites(new Stock(tup, "US"));
            finish();
        } else {
            Log.e("Err", "Wrong query to SearchableActivity");
            onDestroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}