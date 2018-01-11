package cn.read.ui.presenter.impl;

import javax.inject.Inject;

import cn.read.bean.NewsDetail;
import cn.read.ui.interactor.NewsDetailInteractor;
import cn.read.ui.interactor.impl.NewsDetailInteractorImpl;
import cn.read.ui.presenter.NewsDetailPresenter;
import cn.read.ui.view.NewsDetailView;


/**
 * Created by lw on 2017/1/18.
 */

public class NewsDetailPresenterImpl extends BasePresenterImpl<NewsDetailView, NewsDetail>
        implements NewsDetailPresenter {

    private NewsDetailInteractor<NewsDetail> mNewsDetailInteractor;
    private String mPostId;

    @Inject
    public NewsDetailPresenterImpl(NewsDetailInteractorImpl newsDetailInteractor) {
        mNewsDetailInteractor = newsDetailInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscription = mNewsDetailInteractor.loadNewsDetail(this, mPostId);
    }

    @Override
    public void success(NewsDetail data) {
        super.success(data);
        mView.setNewsDetail(data);
    }

    @Override
    public void setPosId(String postId) {
        mPostId = postId;
    }
}
