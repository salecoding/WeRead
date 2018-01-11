package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.PhotoSetInfo;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.GrilPhotoDetailInteractor;
import cn.read.ui.interactor.NewsPhotoSetInteractor;
import cn.read.ui.interactor.impl.GrilPhotoDetailInteractorImpl;
import cn.read.ui.interactor.impl.NewsPhotoSetInteractorImpl;
import cn.read.ui.presenter.GrilPhotoDetailPresenter;
import cn.read.ui.presenter.NewsPhotoSetPresenter;
import cn.read.ui.view.GrilPhotoDetailView;
import cn.read.ui.view.NewsPhotoSetView;

/**
 * Created by lw on 2017/1/17.
 */

public class NewsPhotoSetPresenterImpl extends BasePresenterImpl<NewsPhotoSetView, PhotoSetInfo>
        implements NewsPhotoSetPresenter, RequestCallBack<PhotoSetInfo> {

    private NewsPhotoSetInteractor<PhotoSetInfo> mNewsPhotoSetInteractor;
    private boolean misFirstLoad;
    private boolean mIsRefresh = true;
    private String mPhotoSetId;

    @Inject
    public NewsPhotoSetPresenterImpl(NewsPhotoSetInteractorImpl newsPhotoSetInteractor) {
        mNewsPhotoSetInteractor = newsPhotoSetInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadNewsPhotoSetData();
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
            mView.setNewsPhotoSetInfos(null, loadType);
        }
    }

    @Override
    public void success(PhotoSetInfo items) {
        misFirstLoad = true;
        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setNewsPhotoSetInfos(items, loadType);
            mView.hideProgress();
        }

    }

    private void loadNewsPhotoSetData() {
        mSubscription = mNewsPhotoSetInteractor.lodeNewsPhotoSetInfos(this, mPhotoSetId);
    }

    @Override
    public void setPhotoSetId(String photoSetId) {
        mPhotoSetId = photoSetId;
    }
}
