package com.example.stonksapp.UI.Fragments;

import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.financial.Components.Stock;
import com.example.stonksapp.financial.Components.FavouriteStock;
import com.example.stonksapp.financial.Components.WatchingStocks;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.util.Log;

import java.lang.NullPointerException;

public class ManageFavouriteStonksFragment extends Fragment {
    private CustomAdapter adapter;
    private static AppCompatActivity activity;
    private static ManageFavouriteStonksFragment fragment;

    public ManageFavouriteStonksFragment() {
        // Required empty public constructor
    }

    public static synchronized ManageFavouriteStonksFragment createInstance(
            @Nullable Bundle bundle, @NonNull AppCompatActivity mActivity) {
        if (fragment == null)
            fragment = new ManageFavouriteStonksFragment();

        activity = mActivity;
        fragment.setArguments(bundle);
        return fragment;
    }

    public int updateAndRefresh() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        try {

            transaction.detach(this);
            transaction.attach(this).commit();

            return Constants.SUCCESS;
        } catch (NullPointerException e) {
            Log.e("ManageFavouriteStonks", "Manage Favourites fragment not found");
            e.printStackTrace();
            return Constants.FAILURE;
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
        RecyclerView recView = (RecyclerView) view.findViewById(R.id.favouritesRecycler);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter(getArguments());
        recView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        FavouriteStock.deleteAllDelayed();
    }

    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;
        final TextView priceCell;
        final CheckBox button;
        final TextView description;
        final ConstraintLayout constraintLayout;
        final TextView percents;

        CustomViewHolder(@NonNull View view) {
            super(view);
            cell = (TextView) view.findViewById(R.id.simpleTextView);
            priceCell = (TextView) view.findViewById(R.id.priceTextView);
            button = (CheckBox) view.findViewById(R.id.setFavouriteButton);
            description = (TextView) view.findViewById(R.id.descriptionTextView);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.simpleLinearLayout);
            percents = (TextView) view.findViewById(R.id.changePriceTextView);
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
                if (pos % 2 == 1)
                    holder.constraintLayout.setBackgroundResource(R.drawable.simple_text_view_background);
                else
                    holder.constraintLayout.setBackgroundResource(R.drawable.simple_text_view_default);

                holder.cell.setText(FavouriteStock.currentFavourites.get(pos).symbol);
                holder.description.setText(FavouriteStock.currentFavourites.get(pos).name == null ?
                        "Company Name": FavouriteStock.currentFavourites.get(pos).name);
                holder.priceCell.setText(FavouriteStock.currentFavourites.get(pos).price);
                holder.button.setChecked(!FavouriteStock.isInDelayedDeletion(pos));
                holder.percents.setText(FavouriteStock.currentFavourites.get(pos).percents);
                if (FavouriteStock.currentFavourites.get(pos).percents != null
                        && Double.parseDouble(FavouriteStock.currentFavourites.get(pos).percents) < 0) {
                    holder.percents.setText(String.format(
                            "%s%%", FavouriteStock.currentFavourites.get(pos).percents));
                    holder.percents.setTextColor(activity.getResources().getColor(R.color.myRed));
                } else if (FavouriteStock.currentFavourites.get(pos).percents != null) {
                    holder.percents.setText(String.format(
                            "+%s%%", FavouriteStock.currentFavourites.get(pos).percents));
                    holder.percents.setTextColor(activity.getResources().getColor(R.color.myGreen));
                }
            } catch (NullPointerException e) {
                Log.e("Err", "Current favourites is not valid");
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                Log.e("Err", "Wanted position is not valid");
                e.printStackTrace();
            }

            holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    if (isChecked) {
                        Log.i("Info", "Stock which was deleted is going to be favourite again");
                        FavouriteStock.cancelDeletion(pos);
                    } else {
                        FavouriteStock.setFavouriteDelayDeleted(pos);
                    }
                }
            });
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