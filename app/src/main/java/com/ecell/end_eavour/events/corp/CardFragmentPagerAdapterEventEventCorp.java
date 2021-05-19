package com.ecell.end_eavour.events.corp;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ecell.end_eavour.events.CardAdapterEvent;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapterEventEventCorp extends FragmentStatePagerAdapter implements CardAdapterEvent {

    private List<CardFragmentEventCorp> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapterEventEventCorp(FragmentManager fm, float baseElevation, int x) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int i = 0; i< x; i++){
            addCardFragment(new CardFragmentEventCorp());
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
        return CardFragmentEventCorp.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragmentEventCorp) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragmentEventCorp fragment) {
        fragments.add(fragment);
    }
}
