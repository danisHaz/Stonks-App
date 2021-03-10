package com.example.stonksapp.UI.Fragments;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.FavouriteStock;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;
import java.lang.NullPointerException;

public class ManageFavouriteStonksFragment extends Fragment {
    private ArrayList<Stock> stockList;
    private CustomAdapter adapter;

    public ManageFavouriteStonksFragment() {
        // Required empty public constructor
    }

    public static ManageFavouriteStonksFragment createInstance(@Nullable Bundle bundle) {
        ManageFavouriteStonksFragment fragment = new ManageFavouriteStonksFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void updateAndRefresh(@NonNull AppCompatActivity activity) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        try {
            Fragment usedFragment = activity.getSupportFragmentManager()
                    .findFragmentByTag(Constants.MANAGE_YOUR_FAVOURITES_TAG);

            transaction.detach(usedFragment);
            transaction.attach(usedFragment).commit();
        } catch (NullPointerException e) {
            Log.e("Err", "Manage Favourites fragment not found");
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // get args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_favourite_stonks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        RecyclerView recView = (RecyclerView) view;
        adapter = new CustomAdapter(bundle);
        recView.setAdapter(adapter);
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;
        final TextView priceCell;

        CustomViewHolder(@NonNull View view) {
            super(view);
            cell = (TextView) view.findViewById(R.id.simpleTextView);
            priceCell = (TextView) view.findViewById(R.id.priceTextView);
        }
    }

    private static class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount;

        CustomAdapter(@Nullable Bundle bundle) {
            argCount = FavouriteStock.currentFavourites.size();

        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {
            try {
                holder.cell.setText(FavouriteStock.currentFavourites.get(pos).symbol);
                holder.priceCell.setText(FavouriteStock.currentFavourites.get(pos).price);
            } catch (NullPointerException e) {
                Log.e("Err", "Current favourites is not valid");
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup container, final int type) {
            return new CustomViewHolder(LayoutInflater.from(container.getContext())
                    .inflate(R.layout.simple_text_view, container, false));
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}