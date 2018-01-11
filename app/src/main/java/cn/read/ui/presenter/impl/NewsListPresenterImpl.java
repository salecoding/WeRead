package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.NewsSummary;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.NewsListInteractor;
import cn.read.ui.interactor.impl.NewsListInteractorImpl;
import cn.read.ui.presenter.NewsListPresenter;
import cn.read.ui.view.NewsListView;

/**
 * Created by lw on 2017/1/17.
 */

public class NewsListPresenterImpl extends BasePresenterImpl<NewsListView, List<NewsSummary>>
        implements NewsListPresenter, RequestCallBack<List<NewsSummary>> {

    private NewsListInteractor<List<NewsSummary>> mNewsListInteractor;
    private String mNewsType;
    private String mNewsId;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public NewsListPresenterImpl(NewsListInteractorImpl newsListInteractor) {
        mNewsListInteractor = newsListInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadNewsData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setNewsHead(0, null);
            mView.setNewsList(null, loadType);
        }
    }

    @Override
    public void success(List<NewsSummary> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage += 20;
        }
        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            //查找新闻头
            findNewsHead(items);
            mView.setNewsList(items, loadType);
            mView.hideProgress();
        }

    }

    private void findNewsHead(List<NewsSummary> items) {
        int i = 0;
        for (NewsSummary newsSummary : items) {
            i++;
            boolean isNewsHead = newsSummary.getHasHead() == 1 &&
                    newsSummary.getAds() != null && newsSummary.getAds().size() > 1;
            if (isNewsHead) mView.setNewsHead(i, newsSummary);
        }
    }

    @Override
    public void setNewsTypeAndId(String newsType, String newsId) {
        mNewsType = newsType;
        mNewsId = newsId;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadNewsData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadNewsData();
    }

    private void loadNewsData() {
        mSubscription = mNewsListInteractor.loadNews(this, mNewsType, mNewsId, mStartPage);
    }
}
