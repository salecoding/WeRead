package cn.read.ui.interactor;

import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/17.
 */

public interface BookMarksListInteractor<T> {
    Subscription loadBookMarks(RequestCallBack<T> listener, String type, String id, int startPage);
}
