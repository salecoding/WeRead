package cn.read.ui.interactor;

import java.util.List;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.PhotoInfo;
import cn.read.listener.RequestCallBack;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lw on 2017/1/15.
 */

public interface GrilPhotoDetailInteractor<T> {
    Subscription lodeFirstGrilPhotos(RequestCallBack<T> callBack, List<BeautyPhotoInfo> beautyPhotoInfos);

    Subscription lodeGrilPhotos(RequestCallBack<T> callBack, int offset);
}
