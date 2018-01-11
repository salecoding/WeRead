package cn.read.ui.view;

import java.util.List;

import cn.read.bean.BeautyPhotoInfo;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-07.
 */

public interface GrilPhotoDetailView extends BaseView {
    void setBeautyPhotoInfos(List<BeautyPhotoInfo> beautyPhotoInfos, @LoadNewsType.checker int loadType);
}
