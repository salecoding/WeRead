package cn.read.ui.presenter;

import java.util.List;

import cn.read.bean.BeautyPhotoInfo;

/**
 * Created by lw on 2017/1/17.
 */

public interface GrilPhotoDetailPresenter extends BasePresenter {

    void setBeautyPhotoInfos(List<BeautyPhotoInfo> beautyPhotoInfos);

    void refreshData();

    void loadMore();
}
