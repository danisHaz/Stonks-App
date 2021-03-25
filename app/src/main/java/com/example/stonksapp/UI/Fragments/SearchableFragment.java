package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Activities.MainActivity;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.UI.Components.WorkDoneListener;
import com.example.stonksapp.UI.Activities.SearchableActivity;
import com.example.stonksapp.financial.Background.BackgroundTaskHandler;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.SymbolQuery;
import com.example.stonksapp.financial.Network.HTTPSRequestClient;

import java.util.ArrayList;

public class SearchableFragment extends Fragment {
    private static SearchableFragment fragment;
    private static final HTTPSRequestClient.GET client = new HTTPSRequestClient.GET();
    private static ArrayList<Stock> list = new ArrayList<>();
    private static SearchableActivity activity;
    private String searchQuery;

    private SearchableFragment() { }

    public static synchronized SearchableFragment createInstance(
            final Bundle searchInfo, SearchableActivity mActivity) {
        if (fragment == null)
            fragment = new SearchableFragment();

        activity = mActivity;
        fragment.setArguments(searchInfo);
        fragment.searchQuery = searchInfo.getString("QUERY");

        Thread thready = new Thread(new Runnable() {
            @Override
            public void run() {
                final SymbolQuery result = client.symbolLookup(String.format(
                        Constants.GET_SYMBOL_LOOKUP_TEMPLATE,
                        searchInfo.getString("QUERY"), Constants.API_TOKEN));

                for (SymbolQuery.SingleResult res : result.resultArray) {
                    list.add(Stock.from(res));
                }

                if (!SearchableActivity.isStopped) {
                    try {
                        WorkDoneListener.complete(Constants.DO_SEARCH_WORK, OnCompleteListener.Result.SUCCESS);
                    } catch (NullPointerException e) {
                        Log.e("Searchable Fragment", "Work is not set");
                    }
                } else {
                    WorkDoneListener.setListenerWaiting(Constants.DO_SEARCH_WORK);
                }
            }
        });

        thready.start();

        return fragment;
    }

    public void refresh() {
        if (fragment != null) {
            list.clear();
        } else {
            Log.e("SearchableFragment", "Fragment is never created but refreshed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searchable_0, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle bundle) {
        RecyclerView recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (list.isEmpty()) {
            list.add(new Stock("No results", "", "", ""));
            recView.setAdapter(new CustomItemAdapter(null));
            return;
        }

        ((TextView) view.findViewById(R.id.yourListView)).setText(String.format(Constants.SEARCH_TEMPLATE, searchQuery));
        recView.setAdapter(new CustomItemAdapter(null));
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
                                new String[]{list.get(pos).symbol}, activity);
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
                Log.e("Err", "index out of bound when set text to (price)cell");
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                Log.e("Err", "holder.(price)cell is null");
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