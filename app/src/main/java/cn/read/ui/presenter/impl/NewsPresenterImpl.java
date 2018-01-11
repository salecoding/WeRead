package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.NewsChannelTable;
import cn.read.ui.interactor.NewsInteractor;
import cn.read.ui.interactor.impl.NewsInteractorImpl;
import cn.read.ui.presenter.NewsPresenter;
import cn.read.ui.view.NewsView;

/**
 * Created by lw on 2017/1/16.
 */
public class NewsPresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannelTable>>
        implements NewsPresenter {

    private NewsInteractor<List<NewsChannelTable>> mNewsInteractor;

    @Inject
    public NewsPresenterImpl(NewsInteractorImpl newsInteractor) {
        mNewsInteractor = newsInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    private void loadNewsChannels() {
        mSubscription = mNewsInteractor.lodeNewsChannels(this);
    }

    @Override
    public void success(List<NewsChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {
        loadNewsChannels();
    }
}
