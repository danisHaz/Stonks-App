package com.example.stonksapp.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stonksapp.R;

public class ManageFavouriteStonksFragment extends Fragment {

    public ManageFavouriteStonksFragment() {
        // Required empty public constructor
    }

    public static ManageFavouriteStonksFragment createInstance() {
        return new ManageFavouriteStonksFragment();
    }

    // TODO: Rename and change types and number of parameters
    public static ManageFavouriteStonksFragment newInstance(String param1, String param2) {
        ManageFavouriteStonksFragment fragment = new ManageFavouriteStonksFragment();
        Bundle args = new Bundle();
        // add args
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
}