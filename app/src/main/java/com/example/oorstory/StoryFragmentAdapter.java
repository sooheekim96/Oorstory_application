package com.example.oorstory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public abstract class StoryFragmentAdapter extends FragmentStateAdapter {
    public int mCount;
    public StoryFragmentAdapter(FragmentActivity fa, int count){
        super(fa);
        mCount = count;
    }

    @NonNull
    public Fragment createFragment(int position){
        int index = getRealPosition(position);

        if(index==0) return new FragmentStory1();
        else return new FragmentStory2();

    }


    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position){
        return position % mCount;
    }
}
