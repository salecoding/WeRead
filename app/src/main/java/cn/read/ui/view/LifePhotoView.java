package cn.read.ui.view;

import java.util.List;

import cn.read.bean.PhotoInfo;
import cn.read.bean.WelfarePhotoList;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface LifePhotoView extends BaseView {
    void setLiftPhotoList(List<PhotoInfo> photoInfos, @LoadNewsType.checker int loadType);
}
