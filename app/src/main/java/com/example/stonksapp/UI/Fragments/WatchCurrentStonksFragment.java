package com.example.stonksapp.UI.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.CheckBox;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.WatchingStocks;

import java.lang.NullPointerException;

public class WatchCurrentStonksFragment extends Fragment {
    private static AppCompatActivity innerContext;
    private static WatchCurrentStonksFragment frag;

    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    public static synchronized WatchCurrentStonksFragment createInstance(@Nullable Bundle bundle,
                                                            @NonNull AppCompatActivity context) {
        if (frag == null)
            frag =  new WatchCurrentStonksFragment();

        innerContext = context;

        frag.setArguments(bundle);
        return frag;
    }

    public int updateAndRefresh() {
        FragmentTransaction transaction = innerContext
                .getSupportFragmentManager().beginTransaction();
        try {
            Fragment fragg = frag;
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

        CustomItemAdapter adapter = new CustomItemAdapter(getArguments());
        recyclerView.setAdapter(adapter);
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;
        final TextView priceCell;
        final CheckBox button;
        final TextView description;
        final ConstraintLayout linearLayout;
        final TextView percents;

        CustomViewHolder(@NonNull View v) {
            super(v);
            cell = (TextView) v.findViewById(R.id.simpleTextView);
            priceCell = (TextView) v.findViewById(R.id.priceTextView);
            button = (CheckBox) v.findViewById(R.id.setFavouriteButton);
            description = (TextView) v.findViewById(R.id.descriptionTextView);
            linearLayout = (ConstraintLayout) v.findViewById(R.id.simpleLinearLayout);
            percents = (TextView) v.findViewById(R.id.changePriceTextView);
        }
    }

    private static class CustomItemAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount;

        CustomItemAdapter(Bundle bundle) {
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
                if (pos % 2 == 1)
                    holder.linearLayout.setBackgroundResource(R.drawable.simple_text_view_background);
                else
                    holder.linearLayout.setBackgroundResource(R.drawable.simple_text_view_default);
                holder.cell.setText(WatchingStocks.watchingStocks.get(pos).symbol);
                holder.description.setText(WatchingStocks.watchingStocks.get(pos).name == null ?
                        "Company Name": WatchingStocks.watchingStocks.get(pos).name);

                holder.priceCell.setText(WatchingStocks.watchingStocks.get(pos).price);
                holder.button.setChecked(FavouriteStock.isInFavourites(
                        WatchingStocks.watchingStocks.get(pos)) != -1);
                if (WatchingStocks.watchingStocks.get(pos).percents != null
                        && Double.parseDouble(WatchingStocks.watchingStocks.get(pos).percents) < 0) {
                    holder.percents.setText(String.format("%s%%",
                            WatchingStocks.watchingStocks.get(pos).percents));
                    holder.percents
                            .setTextColor(innerContext.getResources().getColor(R.color.myRed));
                } else if (WatchingStocks.watchingStocks.get(pos).percents != null) {
                    holder.percents.setText(String.format("+%s%%",
                            WatchingStocks.watchingStocks.get(pos).percents));
                    holder.percents
                            .setTextColor(innerContext.getResources().getColor(R.color.myGreen));
                }
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                Log.d("WatchCurrentStonksFrag", "index out of bound when set text to (price)cell");
                e.printStackTrace();
            } catch (java.lang.NullPointerException e) {
                Log.d("WatchCurrentStonksFrag", "holder.(price)cell is null");
                e.printStackTrace();
            }

            holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if (isChecked) {
                        FavouriteStock.addToFavourites(WatchingStocks.watchingStocks.get(pos));
                    } else {
                        FavouriteStock.deleteFromFavourites(WatchingStocks.watchingStocks.get(pos));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}