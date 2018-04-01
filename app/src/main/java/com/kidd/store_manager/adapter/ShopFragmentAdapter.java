package com.kidd.store_manager.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.kidd.store_manager.R;
import com.kidd.store_manager.view.book.BookFragment;
import com.kidd.store_manager.view.category.CategoryFragment;
import com.kidd.store_manager.view.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TuanAnhKid on 3/26/2018.
 */

public class ShopFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> lsFragment;
    Context context;

    public ShopFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        lsFragment = new ArrayList<>();
        lsFragment.add(new CategoryFragment());
        lsFragment.add(new BookFragment());
        lsFragment.add(new SearchFragment());
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return lsFragment.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "Category";
            }
            case 1: {
                return "Book";
            }
            case 2: {
                return "Search";
            }
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return lsFragment.size();
    }
}
