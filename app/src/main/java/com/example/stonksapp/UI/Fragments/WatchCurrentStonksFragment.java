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
import com.example.stonksapp.financial.TradesPrices;

import java.util.ArrayList;
import java.lang.NullPointerException;

public class WatchCurrentStonksFragment extends Fragment {
    private CustomItemAdapter adapter;
    private AppCompatActivity innerContext;

    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    /**
     * @param bundle: Bundle should have:
     * symbolArray: ArrayList<String>
     * priceArray: ArrayList<String>
     *
     * @return
     */
    public static WatchCurrentStonksFragment createInstance(Bundle bundle, AppCompatActivity context) {
        WatchCurrentStonksFragment frag =  new WatchCurrentStonksFragment();
        frag.innerContext = context;

        frag.setArguments(bundle);
        return frag;
    }

    private void refreshFragment() {
        FragmentTransaction transaction = innerContext.getSupportFragmentManager().beginTransaction();
        try {
            Fragment fragg = innerContext.getSupportFragmentManager().findFragmentByTag(Constants.WATCH_STONKS_TAG);
            transaction.detach(fragg);
            transaction.attach(fragg);
            transaction.commit();
        } catch (NullPointerException e) {
            Log.d("Err", "Watch stonks fragment not found");
            e.printStackTrace();
        }
    }

    public int updateCurrentStonks(TradesPrices prices) {
        for (int pos = 0; pos < adapter.symbolList.size(); pos++) {
            String chosenSymbol = adapter.symbolList.get(pos);
            if (prices.data[prices.data.length - 1].symbol.equals(chosenSymbol)) {
                adapter.priceList.set(pos, prices.data[prices.data.length - 1].lastPrice);

                refreshFragment();

                return Constants.SUCCESS;
            }
        }
        return Constants.FAILURE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_current_stonks, container,
                false);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle bundle) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomItemAdapter(getArguments());

        recyclerView.setAdapter(adapter);
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
        public ArrayList<String> symbolList;
        public ArrayList<String> priceList;

        CustomItemAdapter(Bundle bundle) {
            try {
                symbolList = bundle.getStringArrayList("symbolArray");
                argCount = symbolList.size();
                priceList = bundle.getStringArrayList("priceArray");
            } catch (java.lang.NullPointerException e) {
                Log.d("Err", "symbolList not provided");
                e.printStackTrace();
            }
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
                holder.cell.setText(symbolList.get(pos));
                holder.priceCell.setText(priceList.get(pos));
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