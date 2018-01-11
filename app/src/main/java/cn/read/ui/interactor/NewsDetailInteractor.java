package cn.read.ui.interactor;

import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/18.
 */

public interface NewsDetailInteractor<T> {
    Subscription loadNewsDetail(RequestCallBack<T> callBack, String postId);
}
