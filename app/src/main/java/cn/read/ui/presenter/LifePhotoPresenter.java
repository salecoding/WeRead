package cn.read.ui.presenter;

/**
 * Created by lw on 2017/1/17.
 */

public interface LifePhotoPresenter extends BasePresenter {
    void setId(String setId);

    void refreshData();

    void loadMore();
}
