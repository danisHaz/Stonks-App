package com.example.stonksapp.UI.Activities;
import com.example.stonksapp.Constants;
import com.example.stonksapp.R;
import com.example.stonksapp.UI.Fragments.ManageFavouriteStonksFragment;
import com.example.stonksapp.UI.Fragments.WatchCurrentStonksFragment;
import com.example.stonksapp.financial.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    private static byte defaultFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.setDefaultFragment();
        }

        // TODO: make some defines of static things

        // TODO: rewrite to RxJava

    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO: start some background work here
    }

    private void setDefaultFragment() {
        switch (defaultFragment) {
            case 0:
                this.setWatchCurrentStonksFragment();
                break;
            case 1:
                this.setManageFavouritesStonksFragment();
                break;
            default:
                // nothing to do with it
                break;
        }
    }

    // TODO: rewrite createInstance
    private void setWatchCurrentStonksFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, WatchCurrentStonksFragment.createInstance(),
                        Constants.WATCH_STONKS_TAG)
                .commit();
    }

    private void setManageFavouritesStonksFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag, ManageFavouriteStonksFragment.createInstance(),
                        Constants.MANAGE_YOUR_FAVOURITES_TAG)
                .commit();
    }

    public void onWatchCurrentStonksClick(View view) {
        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);
        if (fragment.getTag() != Constants.WATCH_STONKS_TAG)
            this.setWatchCurrentStonksFragment();
    }

    public void onManageFavouriteStonksClick(View view) {
        FragmentContainerView fragment = (FragmentContainerView) findViewById(R.id.frag);
        if (fragment.getTag() != Constants.MANAGE_YOUR_FAVOURITES_TAG)
            this.setManageFavouritesStonksFragment();
    }
}