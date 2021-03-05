package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
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


public class WatchCurrentStonksFragment extends Fragment {

    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    public static WatchCurrentStonksFragment createInstance(Bundle bundle) {
        WatchCurrentStonksFragment frag =  new WatchCurrentStonksFragment();
        frag.setArguments(bundle);
        return frag;
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

        recyclerView.setAdapter(new CustomItemAdapter(getArguments()));
    }


    private static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView cell;

        CustomViewHolder(@NonNull View v) {
            super(v);
            cell = (TextView) v.findViewById(R.id.simpleTextView);
            Log.d("tag", "tagtagtag");
        }
    }

    private static class CustomItemAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private int argCount = 1;

        CustomItemAdapter(Bundle bundle) {
            // add code
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
                holder.cell.setText("Some data about Stonks");
            } catch (java.lang.ArrayIndexOutOfBoundsException
                    | java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return argCount;
        }
    }
}