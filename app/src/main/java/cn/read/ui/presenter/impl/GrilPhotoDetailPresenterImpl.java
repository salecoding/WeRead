package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.PhotoInfo;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.GrilPhotoDetailInteractor;
import cn.read.ui.interactor.impl.GrilPhotoDetailInteractorImpl;
import cn.read.ui.presenter.GrilPhotoDetailPresenter;
import cn.read.ui.view.GrilPhotoDetailView;

/**
 * Created by lw on 2017/1/17.
 */

public class GrilPhotoDetailPresenterImpl extends BasePresenterImpl<GrilPhotoDetailView, List<BeautyPhotoInfo>>
        implements GrilPhotoDetailPresenter, RequestCallBack<List<BeautyPhotoInfo>> {

    private GrilPhotoDetailInteractor<List<BeautyPhotoInfo>> mGrilPhotoDetailInteractor;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;
    private List<BeautyPhotoInfo> mBeautyPhotoInfos;

    @Inject
    public GrilPhotoDetailPresenterImpl(GrilPhotoDetailInteractorImpl grilPhotoDetailInteractor) {
        mGrilPhotoDetailInteractor = grilPhotoDetailInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadFirstGrilPhotosData();
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
            mView.setBeautyPhotoInfos(null, loadType);
        }
    }

    @Override
    public void success(List<BeautyPhotoInfo> items) {
        misFirstLoad = true;
        if (items != null) {
            mStartPage++;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setBeautyPhotoInfos(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void setBeautyPhotoInfos(List<BeautyPhotoInfo> beautyPhotoInfos) {
        this.mBeautyPhotoInfos = beautyPhotoInfos;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadGrilPhotosData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadGrilPhotosData();
    }

    private void loadGrilPhotosData() {
        mSubscription = mGrilPhotoDetailInteractor.lodeGrilPhotos(this, mStartPage);
    }

    private void loadFirstGrilPhotosData() {
        mSubscription = mGrilPhotoDetailInteractor.lodeFirstGrilPhotos(this, mBeautyPhotoInfos);
    }
}
