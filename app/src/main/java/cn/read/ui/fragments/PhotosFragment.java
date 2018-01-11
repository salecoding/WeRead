package cn.read.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.NewsChannelTable;
import cn.read.event.ChannelChangeEvent;
import cn.read.ui.adapter.NewsFragmentPagerAdapter;
import cn.read.ui.adapter.PhotosFragmentPagerAdapter;
import cn.read.ui.presenter.impl.NewsPresenterImpl;
import cn.read.ui.presenter.impl.PhotosPresenterImpl;
import cn.read.ui.view.PhotosView;
import cn.read.utils.MyUtils;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-06.
 * 图片
 */

public class PhotosFragment extends BaseFragment implements PhotosView {
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.add_channel_iv)
    ImageView addChannelIv;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;
    @Inject
    PhotosPresenterImpl mPhotosPresenter;

    private List<Fragment> mPhotosFragmentList = new ArrayList<>();

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews(View view) {
        mPresenter = mPhotosPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photos;
    }

    @Override
    public void initViewPager(List<String> photosChannelNames) {
        final List<String> channelNames = new ArrayList<>();
        if (photosChannelNames != null) {
            setPhotosList(photosChannelNames, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setPhotosList(List<String> photosChannelNames, List<String> channelNames) {
        mPhotosFragmentList.clear();
        for (String channelName : photosChannelNames) {
            channelNames.add(channelName);
        }
        mPhotosFragmentList.add(BeautyPhotoFragment.newInstance());
        mPhotosFragmentList.add(WelfarePhotoFragment.newInstance());
        mPhotosFragmentList.add(LifePhotoFragment.newInstance());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }

    @OnClick(R.id.add_channel_iv)
    public void onClick() {
    }

    private void setViewPager(List<String> channelNames) {
        PhotosFragmentPagerAdapter adapter = new PhotosFragmentPagerAdapter(
                getChildFragmentManager(), channelNames, mPhotosFragmentList);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        MyUtils.dynamicSetTabLayoutMode(tabs);
//        mTabs.setTabsFromPagerAdapter(adapter);
        setPageChangeListener();

        mChannelNames = channelNames;
        int currentViewPagerPosition = getCurrentViewPagerPosition();
        viewPager.setCurrentItem(currentViewPagerPosition, false);
    }

    private int getCurrentViewPagerPosition() {
        int position = 0;
        if (mCurrentViewPagerName != null) {
            for (int i = 0; i < mChannelNames.size(); i++) {
                if (mCurrentViewPagerName.equals(mChannelNames.get(i))) {
                    position = i;
                }
            }
        }
        return position;
    }

    private void setPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewPagerName = mChannelNames.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
