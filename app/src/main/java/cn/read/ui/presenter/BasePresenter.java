package cn.read.ui.presenter;

import android.support.annotation.NonNull;

import cn.read.ui.view.BaseView;

/**
 * Created by lw on 2017/1/16.
 */

public interface BasePresenter {
//    void onResume();

    void onCreate();

    void attachView(@NonNull BaseView view);

    void onDestroy();
}
