package cn.read.ui.presenter;

/**
 * Created by lw on 2017/1/17.
 */

public interface NewsListPresenter extends BasePresenter {
    void setNewsTypeAndId(String newsType, String newsId);

    void refreshData();

    void loadMore();
}
