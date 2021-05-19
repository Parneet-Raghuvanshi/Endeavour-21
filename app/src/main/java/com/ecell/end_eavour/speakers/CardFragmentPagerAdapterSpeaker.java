package com.ecell.end_eavour.speakers;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapterSpeaker extends FragmentStatePagerAdapter implements CardAdapterSpeaker {

    private List<CardFragmentSpeaker> fragments;
    private float baseElevation;

    public CardFragmentPagerAdapterSpeaker(final FragmentManager fm, float baseElevation, int x) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int i = 0; i< x; i++){
            addCardFragment(new CardFragmentSpeaker());
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
        return CardFragmentSpeaker.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragmentSpeaker) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragmentSpeaker fragment) {
        fragments.add(fragment);
    }
}
