package cn.read.ui.presenter.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.bean.VideosChannelTable;
import cn.read.common.Constants;
import cn.read.event.ChannelChangeEvent;
import cn.read.ui.interactor.impl.VideosChannelInteractorImpl;
import cn.read.ui.presenter.VideosChannelPresenter;
import cn.read.ui.view.VideosChannelView;
import cn.read.utils.RxBus;

/**
 * Created by lw on 2017/1/19.
 */

public class VideosChannelPresenterImpl extends BasePresenterImpl<VideosChannelView,
        Map<Integer, List<VideosChannelTable>>> implements VideosChannelPresenter {

    private VideosChannelInteractorImpl mVideosChannelInteractor;
    private boolean mIsChannelChanged;

    @Inject
    public VideosChannelPresenterImpl(VideosChannelInteractorImpl videosChannelInteractor) {
        mVideosChannelInteractor = videosChannelInteractor;
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
        mVideosChannelInteractor.lodeVideosChannels(this);
    }

    @Override
    public void success(Map<Integer, List<VideosChannelTable>> data) {
        super.success(data);
        mView.initRecyclerViews(data.get(Constants.VIDEOS_CHANNEL_MINE), data.get(Constants.VIDEOS_CHANNEL_MORE));
    }

    @Override
    public void onItemSwap(int fromPosition, int toPosition) {
        mVideosChannelInteractor.swapDb(fromPosition, toPosition);
        mIsChannelChanged = true;
    }

    @Override
    public void onItemAddOrRemove(VideosChannelTable videosChannelTable, boolean isChannelMine) {
        mVideosChannelInteractor.updateDb(videosChannelTable, isChannelMine);
        mIsChannelChanged = true;
    }

}
