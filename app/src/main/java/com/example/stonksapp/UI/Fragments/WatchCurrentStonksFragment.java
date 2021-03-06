package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.financial.TradesPrices;

import java.util.ArrayList;


public class WatchCurrentStonksFragment extends Fragment {
    private CustomItemAdapter adapter;


    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    /**
     * @param bundle: Bundle should have:
     * symbolArray: ArrayList<String>
     *
     * @return
     */
    public static WatchCurrentStonksFragment createInstance(Bundle bundle) {
        WatchCurrentStonksFragment frag =  new WatchCurrentStonksFragment();
        frag.setArguments(bundle);
        return frag;
    }

    public void updateCurrentStonks(TradesPrices prices) {
        // updater code here
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

        CustomViewHolder(@NonNull View v) {
            super(v);
            cell = (TextView) v.findViewById(R.id.simpleTextView);
        }
    }

    private static class CustomItemAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount;
        private ArrayList<String> symbolList;

        CustomItemAdapter(Bundle bundle) {
            try {
                symbolList = bundle.getStringArrayList("symbolArray");
                argCount = symbolList.size();
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
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                Log.d("Err", "index out of bound when set text to cell");
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                Log.d("Err", "holder.cell is null");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}