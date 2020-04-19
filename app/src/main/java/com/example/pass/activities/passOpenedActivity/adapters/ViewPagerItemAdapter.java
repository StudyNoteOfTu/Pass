package com.example.pass.activities.passOpenedActivity.adapters;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.pass.activities.passOpenedActivity.fragments.ViewPagerItemFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerItemAdapter extends FragmentPagerAdapter {

    private int numOfData;
    private List<ViewPagerItemFragment> fragmentList = new ArrayList<>();

    public ViewPagerItemAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerItemAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPagerItemAdapter(@NonNull FragmentManager fm,List<ViewPagerItemFragment> viewPagerItemFragments,int numOfData){
        super(fm);
        this.fragmentList.clear();
        this.fragmentList.addAll(viewPagerItemFragments);
        this.numOfData = numOfData;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return numOfData;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        //处理position。让数组下标落在[0,fragmentList.size)中，防止越界
        position = position % fragmentList.size();

        return super.instantiateItem(container, position);
    }


    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
//        super.restoreState(state, loader);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return super.isViewFromObject(view, object);
    }
}
