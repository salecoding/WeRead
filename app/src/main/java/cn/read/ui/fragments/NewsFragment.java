package cn.read.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.read.BaseFragment;
import cn.read.R;
import cn.read.bean.NewsChannelTable;
import cn.read.common.Constants;
import cn.read.event.ChannelChangeEvent;
import cn.read.ui.activities.NewsChannelActivity;
import cn.read.ui.adapter.NewsFragmentPagerAdapter;
import cn.read.ui.presenter.impl.NewsPresenterImpl;
import cn.read.ui.view.NewsView;
import cn.read.utils.MyUtils;
import cn.read.utils.RxBus;
import rx.functions.Action1;

/**
 * Created by lw on 2017-03-06.
 * 新闻
 */

public class NewsFragment extends BaseFragment implements NewsView {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.add_channel_iv)
    ImageView addChannelIv;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private String mCurrentViewPagerName;
    private List<String> mChannelNames;
    @Inject
    NewsPresenterImpl mNewsPresenter;

    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelChangeEvent.class)
                .subscribe(new Action1<ChannelChangeEvent>() {
                    @Override
                    public void call(ChannelChangeEvent channelChangeEvent) {
                        mNewsPresenter.onChannelDbChanged();
                    }
                });
    }

    @Override
    public void initViews(View view) {
        mPresenter = mNewsPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @OnClick(R.id.add_channel_iv)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_channel_iv:
                Intent intent = new Intent(getContext(), NewsChannelActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }


    @Override
    public void initViewPager(List<NewsChannelTable> newsChannelTables) {
        final List<String> channelNames = new ArrayList<>();
        if (newsChannelTables != null) {
            setNewsList(newsChannelTables, channelNames);
            setViewPager(channelNames);
        }
    }

    private void setNewsList(List<NewsChannelTable> newsChannels, List<String> channelNames) {
        mNewsFragmentList.clear();
        for (NewsChannelTable newsChannel : newsChannels) {
            NewsListFragment newsListFragment = createListFragments(newsChannel);
            mNewsFragmentList.add(newsListFragment);
            channelNames.add(newsChannel.getNewsChannelName());
            KLog.i(newsChannel.getTableId());
        }
    }

    private NewsListFragment createListFragments(NewsChannelTable newsChannel) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constants.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(Constants.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getChildFragmentManager(), channelNames, mNewsFragmentList);
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

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
    }
}
