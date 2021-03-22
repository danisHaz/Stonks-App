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

import android.app.SearchManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    private static final ArrayList<Stock> list = new ArrayList<>();
    protected static SearchableActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.ifNothingAttached = true;

        setContentView(R.layout.activity_searchable);

        activity = this;
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

            for (SymbolQuery.SingleResult res: result.resultArray) {
                list.add(Stock.from(res));
            }

            RecyclerView recView = findViewById(R.id.recView);
            recView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            if (list.isEmpty()) {
                list.add(new Stock("No results", "", "", ""));
                recView.setAdapter(new CustomItemAdapter(null));
                return;
            }

            ((TextView) findViewById(R.id.yourListView)).setText(R.string.yourResults);
            recView.setAdapter(new CustomItemAdapter(null));

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
                BackgroundTaskHandler.subscribeOnLastPriceUpdates(new String[]{tup}, this);
            } catch (java.lang.NullPointerException e) {
                Log.e("Err", "Chosen symbol in suggestions is null");
            }
            finish();
        } else {
            Log.e("Err", "Wrong query to SearchableActivity");
            onDestroy();
        }
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;
        final TextView priceCell;
        final CheckBox button;
        final TextView description;
        final ConstraintLayout linearLayout;

        CustomViewHolder(@NonNull View v) {
            super(v);
            cell = (TextView) v.findViewById(R.id.simpleTextView);
            priceCell = (TextView) v.findViewById(R.id.priceTextView);
            button = (CheckBox) v.findViewById(R.id.setFavouriteButton);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            linearLayout = (ConstraintLayout) v.findViewById(R.id.simpleLinearLayout);

        }
    }


    private static class CustomItemAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount;

        CustomItemAdapter(Bundle bundle) {
            // provide some code
            argCount = list.size();
        }

        @Override
        @NonNull
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup container, int type) {
            return new CustomViewHolder(LayoutInflater.from(container.getContext())
                    .inflate(R.layout.simple_text_view, container, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {
            try {
                if (getItemCount() == 1) {
                    holder.linearLayout.setBackgroundResource(R.drawable.simple_text_view_default);
                    return;
                }

                if (pos % 2 == 1)
                    holder.linearLayout.setBackgroundResource(R.drawable.simple_text_view_background);
                else
                    holder.linearLayout.setBackgroundResource(R.drawable.simple_text_view_default);

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FavouriteStock.addToFavourites(new Stock(list.get(pos).symbol,
                                list.get(pos).name,"US", null));

                        BackgroundTaskHandler.subscribeOnLastPriceUpdates(
                                new String[]{list.get(pos).symbol}, SearchableActivity.activity);
                        activity.finish();
                    }
                });

                holder.cell.setText(list.get(pos).symbol);
                holder.description.setText(list.get(pos).name == null ?
                        "Company Name": list.get(pos).name);
                holder.priceCell.setText(list.get(pos).price);
                holder.button.setChecked(FavouriteStock.isInFavourites(
                        list.get(pos)) != -1);
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                Log.d("Err", "index out of bound when set text to (price)cell");
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                Log.d("Err", "holder.(price)cell is null");
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.ifNothingAttached = false;
    }
}