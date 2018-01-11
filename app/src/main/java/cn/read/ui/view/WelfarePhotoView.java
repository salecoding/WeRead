package cn.read.ui.view;

import java.util.List;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface WelfarePhotoView extends BaseView {
    void setWelfarePhotoList(List<WelfarePhotoList.ResultsBean> welfarePhotoLists, @LoadNewsType.checker int loadType);
}
