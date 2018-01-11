package cn.read.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by lw on 2017/2/20.
 */
public class BookMarks {
    private String id;
    private int type;    //1 news 2 photo 3 video

    private Map<String, List<NewsSummary>> newsSummaryListMap;
    private Map<String, List<BeautyPhotoInfo>> beautyPhotoInfoListMap;
    private Map<String, List<VideosSummary>> videosSummaryListMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, List<NewsSummary>> getNewsSummaryListMap() {
        return newsSummaryListMap;
    }

    public void setNewsSummaryListMap(Map<String, List<NewsSummary>> newsSummaryListMap) {
        this.newsSummaryListMap = newsSummaryListMap;
    }

    public Map<String, List<BeautyPhotoInfo>> getBeautyPhotoInfoListMap() {
        return beautyPhotoInfoListMap;
    }

    public void setBeautyPhotoInfoListMap(Map<String, List<BeautyPhotoInfo>> beautyPhotoInfoListMap) {
        this.beautyPhotoInfoListMap = beautyPhotoInfoListMap;
    }

    public Map<String, List<VideosSummary>> getVideosSummaryListMap() {
        return videosSummaryListMap;
    }

    public void setVideosSummaryListMap(Map<String, List<VideosSummary>> videosSummaryListMap) {
        this.videosSummaryListMap = videosSummaryListMap;
    }
}
