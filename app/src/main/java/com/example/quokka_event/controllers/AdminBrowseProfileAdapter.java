package com.example.quokka_event.controllers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quokka_event.models.event.DetailsFragment;
import com.example.quokka_event.models.event.OverviewFragment;
import com.example.quokka_event.models.event.QRFragment;

/**
 * A tab adapter for fragment to show all profiles or violations.
 */
public class AdminBrowseProfileAdapter extends FragmentStateAdapter{
    public AdminBrowseProfileAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Show a fragment depending on tab
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileListFragment();
            default:
                return new ProfileListFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}