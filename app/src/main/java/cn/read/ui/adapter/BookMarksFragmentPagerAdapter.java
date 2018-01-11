package cn.read.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lw on 2017/1/19.
 */

public class BookMarksFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mTitles;
    private List<Fragment> mBookMarksFragmentList = new ArrayList<>();

    public BookMarksFragmentPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> newsFragmentList) {
        super(fm);
        mTitles = titles;
        mBookMarksFragmentList = newsFragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mBookMarksFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mBookMarksFragmentList.size();
    }

    @Override
    public long getItemId(int position) {
        super.getItemId(position);
        if (mBookMarksFragmentList != null) {
            if (position < mBookMarksFragmentList.size()) {
                //不同的Fragment分配的HashCode不同，从而实现刷新adapter中的fragment
                return mBookMarksFragmentList.get(position).hashCode();
            }
        }
        return super.getItemId(position);
    }
}
