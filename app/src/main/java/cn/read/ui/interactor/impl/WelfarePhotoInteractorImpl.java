package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.base.RetrofitManager;
import cn.read.bean.WelfarePhotoList;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.HostType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BeautyPhotoInteractor;
import cn.read.ui.interactor.WelfarePhotoInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by lw on 2017/1/17.
 */

public class WelfarePhotoInteractorImpl implements WelfarePhotoInteractor<List<WelfarePhotoList.ResultsBean>> {

    //    private boolean mIsNetError;
    @Inject
    public WelfarePhotoInteractorImpl() {
    }

    @Override
    public Subscription loadWelfarePhotos(final RequestCallBack<List<WelfarePhotoList.ResultsBean>> listener, int offset) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getWelfarePhotoObservable(offset)
                .flatMap(new Func1<WelfarePhotoList, Observable<WelfarePhotoList.ResultsBean>>() {
                    @Override
                    public Observable<WelfarePhotoList.ResultsBean> call(WelfarePhotoList welfarePhotoList) {
                        return Observable.from(welfarePhotoList.getResults());
                    }
                })
                .distinct()
                .toList()
                .compose(TransformUtils.<List<WelfarePhotoList.ResultsBean>>defaultSchedulers())
                .subscribe(new Subscriber<List<WelfarePhotoList.ResultsBean>>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(List<WelfarePhotoList.ResultsBean> resultsBeen) {
                        KLog.d();
                        listener.success(resultsBeen);
                    }
                });
    }
}
