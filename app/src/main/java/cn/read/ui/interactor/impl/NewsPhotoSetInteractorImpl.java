package cn.read.ui.interactor.impl;

import com.socks.library.KLog;
import javax.inject.Inject;
import cn.read.base.RetrofitManager;
import cn.read.bean.PhotoSetInfo;
import cn.read.common.HostType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.NewsPhotoSetInteractor;
import cn.read.utils.MyUtils;
import cn.read.utils.TransformUtils;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by lw on 2017/1/17.
 */

public class NewsPhotoSetInteractorImpl implements NewsPhotoSetInteractor<PhotoSetInfo> {

    //    private boolean mIsNetError;
    @Inject
    public NewsPhotoSetInteractorImpl() {
    }

    @Override
    public Subscription lodeNewsPhotoSetInfos(final RequestCallBack<PhotoSetInfo> callBack, String photoSetId) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getPhotoSetInfoObservable(photoSetId)
                .compose(TransformUtils.<PhotoSetInfo>defaultSchedulers())
                .subscribe(new Subscriber<PhotoSetInfo>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(PhotoSetInfo resultsBeen) {
                        KLog.d();
                        callBack.success(resultsBeen);
                    }
                });
    }
}
