package cn.read.ui.interactor;

import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/17.
 */

public interface WelfarePhotoInteractor<T> {
    Subscription loadWelfarePhotos(RequestCallBack<T> listener, int offset);
}
