package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.VideosChannelTable;
import cn.read.ui.interactor.VideosInteractor;
import cn.read.ui.interactor.impl.VideosInteractorImpl;
import cn.read.ui.presenter.VideosPresenter;
import cn.read.ui.view.VideosView;

/**
 * Created by lw on 2017/1/16.
 */
public class VideosPresenterImpl extends BasePresenterImpl<VideosView, List<VideosChannelTable>>
        implements VideosPresenter {

    private VideosInteractor<List<VideosChannelTable>> mVideosInteractor;

    @Inject
    public VideosPresenterImpl(VideosInteractorImpl videosInteractor) {
        mVideosInteractor = videosInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadVideosChannels();
    }

    private void loadVideosChannels() {
        mSubscription = mVideosInteractor.lodeVideosChannels(this);
    }

    @Override
    public void success(List<VideosChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {
        loadVideosChannels();
    }
}
