package cn.read.ui.presenter.impl;


import java.util.List;

import javax.inject.Inject;

import cn.read.bean.PhotoInfo;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.LifePhotoInteractor;
import cn.read.ui.interactor.impl.LifePhotoInteractorImpl;
import cn.read.ui.presenter.LifePhotoPresenter;
import cn.read.ui.view.LifePhotoView;

/**
 * Created by lw on 2017/1/17.
 */

public class LifePhotoPresenterImpl extends BasePresenterImpl<LifePhotoView, List<PhotoInfo>>
        implements LifePhotoPresenter, RequestCallBack<List<PhotoInfo>> {

    private LifePhotoInteractor<List<PhotoInfo>> mLifePhotoInteractor;
    private boolean misFirstLoad;
    private boolean mIsRefresh = true;
    private String setId;

    @Inject
    public LifePhotoPresenterImpl(LifePhotoInteractorImpl lifePhotoInteractor) {
        mLifePhotoInteractor = lifePhotoInteractor;
    }

    @Override
    public void onCreate() {
        if (mView != null) {
            loadLifePhotoData();
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
            mView.setLiftPhotoList(null, loadType);
        }
    }

    @Override
    public void success(List<PhotoInfo> items) {
        misFirstLoad = true;
        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setLiftPhotoList(items, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void setId(String setId) {
        this.setId = setId;
    }

    @Override
    public void refreshData() {
        mIsRefresh = true;
        loadLifePhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        mLifePhotoInteractor.loadMoreLifePhotos(this, setId);
    }

    private void loadLifePhotoData() {
        mSubscription = mLifePhotoInteractor.loadLifePhotos(this);
    }
}
