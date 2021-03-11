package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.WatchingStocks;
import com.example.stonksapp.financial.TradesPrices;

import java.util.ArrayList;
import java.lang.NullPointerException;

public class WatchCurrentStonksFragment extends Fragment {
    private AppCompatActivity innerContext;

    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    public static WatchCurrentStonksFragment createInstance(@Nullable Bundle bundle,
                                                            @NonNull AppCompatActivity context) {
        WatchCurrentStonksFragment frag =  new WatchCurrentStonksFragment();
        frag.innerContext = context;

        frag.setArguments(bundle);
        return frag;
    }

    public int updateAndRefresh() {
        FragmentTransaction transaction = innerContext.getSupportFragmentManager().beginTransaction();
        try {
            Fragment fragg = innerContext.getSupportFragmentManager()
                    .findFragmentByTag(Constants.WATCH_STONKS_TAG);
            transaction.detach(fragg);
            transaction.attach(fragg);
            transaction.commit();
            return Constants.SUCCESS;
        } catch (NullPointerException e) {
            Log.d("Err", "Watch stonks fragment not found");
            e.printStackTrace();
            return Constants.FAILURE;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_watch_current_stonks, container,
                false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle bundle) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new CustomItemAdapter(getArguments()));
    }


    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;
        final TextView priceCell;

        CustomViewHolder(@NonNull View v) {
            super(v);
            cell = (TextView) v.findViewById(R.id.simpleTextView);
            priceCell = (TextView) v.findViewById(R.id.priceTextView);
        }
    }

    private static class CustomItemAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount;

        CustomItemAdapter(Bundle bundle) {
            // provide some code
            argCount = WatchingStocks.watchingStocks.size();
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
                holder.cell.setText(WatchingStocks.watchingStocks.get(pos).symbol);
                holder.priceCell.setText(WatchingStocks.watchingStocks.get(pos).price);
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
}