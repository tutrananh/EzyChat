package com.ptit.ezychat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ptit.ezychat.fragment.ContactListFragment;
import com.ptit.ezychat.fragment.OnlineContactListFragment;
import com.ptit.ezychat.fragment.SearchContactFragment;


public class FragmentAdapter extends FragmentPagerAdapter {
    private int pageNum;
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNum = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 1:
                return new OnlineContactListFragment();
            case 2:
                return new SearchContactFragment();
            default:
                return new ContactListFragment();
        }
    }

    @Override
    public int getCount() {
        return pageNum;
    }


}
