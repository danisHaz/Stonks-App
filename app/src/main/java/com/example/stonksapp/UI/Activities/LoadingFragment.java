package com.example.stonksapp.UI.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.stonksapp.R;
import android.util.Log;

public class LoadingFragment extends Fragment {
    private static LoadingFragment fragment;

    public LoadingFragment() {
        // Required empty public constructor
    }

    public static synchronized LoadingFragment createInstance() {
        if (fragment == null)
            fragment = new LoadingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        ProgressBar progressBar
                = (ProgressBar) view.findViewById(R.id.loadingBar);

        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

}