package cn.read.ui.presenter.impl;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.bean.NewsChannelTable;
import cn.read.common.Constants;
import cn.read.event.ChannelChangeEvent;
import cn.read.ui.interactor.impl.NewsChannelInteractorImpl;
import cn.read.ui.presenter.NewsChannelPresenter;
import cn.read.ui.view.NewsChannelView;
import cn.read.utils.RxBus;

/**
 * Created by lw on 2017/1/19.
 */

public class NewsChannelPresenterImpl extends BasePresenterImpl<NewsChannelView,
        Map<Integer, List<NewsChannelTable>>> implements NewsChannelPresenter {

    private NewsChannelInteractorImpl mNewsChannelInteractor;
    private boolean mIsChannelChanged;

    @Inject
    public NewsChannelPresenterImpl(NewsChannelInteractorImpl newsChannelInteractor) {
        mNewsChannelInteractor = newsChannelInteractor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsChannelChanged) {
            RxBus.getInstance().post(new ChannelChangeEvent());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNewsChannelInteractor.lodeNewsChannels(this);
    }

    @Override
    public void success(Map<Integer, List<NewsChannelTable>> data) {
        super.success(data);
        mView.initRecyclerViews(data.get(Constants.NEWS_CHANNEL_MINE), data.get(Constants.NEWS_CHANNEL_MORE));
    }

    @Override
    public void onItemSwap(int fromPosition, int toPosition) {
        mNewsChannelInteractor.swapDb(fromPosition, toPosition);
        mIsChannelChanged = true;
    }

    @Override
    public void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine) {
        mNewsChannelInteractor.updateDb(newsChannel, isChannelMine);
        mIsChannelChanged = true;
    }
}
