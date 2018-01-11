package cn.read.ui.interactor;

import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/17.
 */

public interface LifePhotoInteractor<T> {
    Subscription loadLifePhotos(RequestCallBack<T> listener);

    Subscription loadMoreLifePhotos(RequestCallBack<T> listener, String setId);
}
