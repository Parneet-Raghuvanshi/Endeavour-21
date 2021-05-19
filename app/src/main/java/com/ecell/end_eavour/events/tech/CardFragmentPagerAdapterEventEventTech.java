package com.ecell.end_eavour.events.tech;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ecell.end_eavour.events.CardAdapterEvent;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapterEventEventTech extends FragmentStatePagerAdapter implements CardAdapterEvent {

    private List<CardFragmentEventTech> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapterEventEventTech(FragmentManager fm, float baseElevation, int x) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int i = 0; i< x; i++){
            addCardFragment(new CardFragmentEventTech());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragmentEventTech.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragmentEventTech) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragmentEventTech fragment) {
        fragments.add(fragment);
    }
}
