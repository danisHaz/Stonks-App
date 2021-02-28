package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stonksapp.R;

public class WatchCurrentStonksFragment extends Fragment {

    public WatchCurrentStonksFragment() {
        // Required empty public constructor
    }

    public static WatchCurrentStonksFragment createInstance() {
        return new WatchCurrentStonksFragment();
    }

    // TODO: Rename and change types and number of parameters
    public static WatchCurrentStonksFragment newInstance(String param1, String param2) {
        WatchCurrentStonksFragment fragment = new WatchCurrentStonksFragment();
        Bundle args = new Bundle();
        // set args
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // gerArgs
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_current_stonks, container, false);
    }
}