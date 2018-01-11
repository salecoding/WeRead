package cn.read.ui.presenter;

/**
 * Created by lw on 2017/1/17.
 */

public interface VideosListPresenter extends BasePresenter {
    void setVideosTypeAndId(String videosType, String videosId);

    void refreshData();

    void loadMore();
}
