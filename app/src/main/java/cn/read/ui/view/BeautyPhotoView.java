package cn.read.ui.view;

import java.util.List;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.VideosSummary;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface BeautyPhotoView extends BaseView {
    void setBeautyPhotoList(List<BeautyPhotoInfo> beautyPhotoInfos, @LoadNewsType.checker int loadType);
}
