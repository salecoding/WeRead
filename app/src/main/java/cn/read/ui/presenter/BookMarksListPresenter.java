package cn.read.ui.presenter;

/**
 * Created by lw on 2017/1/17.
 */

public interface BookMarksListPresenter extends BasePresenter {
    void setBookMarksTypeAndId(String bookMarksType, String bookMarksId);

    void refreshData();

    void loadMore();
}
