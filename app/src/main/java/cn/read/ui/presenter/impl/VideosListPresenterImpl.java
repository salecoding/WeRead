package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.NewsSummary;
import cn.read.bean.VideosSummary;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.VideosListInteractor;
import cn.read.ui.interactor.impl.VideosListInteractorImpl;
import cn.read.ui.presenter.VideosListPresenter;
import cn.read.ui.view.VideosListView;

/**
 * Created by lw on 2017/1/17.
 */

public class VideosListPresenterImpl extends BasePresenterImpl<VideosListView, List<VideosSummary>>
        implements VideosListPresenter, RequestCallBack<List<VideosSummary>> {

    private VideosListInteractor<List<VideosSummary>> mVideosListInteractor;
    private String mVideosType;
    private String mVideosId;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public VideosListPresenterImpl(VideosListInteractorImpl videosListInteractor) {
        mVideosListInteractor = videosListInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadVideosData();
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
            mView.setVideosList(null, loadType);
        }
    }

    @Override
    public void success(List<VideosSummary> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage += 20;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setVideosList(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void setVideosTypeAndId(String videosType, String videosId) {
        mVideosType = videosType;
        mVideosId = videosId;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadVideosData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadVideosData();
    }

    private void loadVideosData() {
        mSubscription = mVideosListInteractor.loadVideos(this, mVideosType, mVideosId, mStartPage);
    }
}
