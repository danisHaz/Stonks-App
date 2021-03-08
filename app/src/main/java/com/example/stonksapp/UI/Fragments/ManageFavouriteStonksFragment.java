package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stonksapp.R;

public class ManageFavouriteStonksFragment extends Fragment {

    public ManageFavouriteStonksFragment() {
        // Required empty public constructor
    }

    public static ManageFavouriteStonksFragment createInstance(@Nullable Bundle bundle) {
        ManageFavouriteStonksFragment fragment = new ManageFavouriteStonksFragment();
        fragment.setArguments(bundle);
        return fragment;
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

        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, final int pos) {

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