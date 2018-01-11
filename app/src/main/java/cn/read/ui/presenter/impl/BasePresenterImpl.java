package cn.read.ui.presenter.impl;

import android.support.annotation.NonNull;

import cn.read.listener.RequestCallBack;
import cn.read.ui.presenter.BasePresenter;
import cn.read.ui.view.BaseView;
import cn.read.utils.MyUtils;
import rx.Subscription;

/**
 * Created by lw on 2017/1/17.
 */
public class BasePresenterImpl<T extends BaseView, E> implements BasePresenter, RequestCallBack<E> {
    protected T mView;
    protected Subscription mSubscription;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        MyUtils.cancelSubscription(mSubscription);
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (T) view;
    }

    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @Override
    public void success(E data) {
        mView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
        mView.hideProgress();
        mView.showMsg(errorMsg);
    }
}
