package cn.read.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by lw on 2017/2/3.
 */
@Entity
public class VideosChannelTable {
    @Id
    private String tableId;
    @NotNull
    private String videosChannelName;
    @NotNull
    private String videosChannelId;
    @NotNull
    private String videosChannelType;
    @NotNull
    private boolean videosChannelSelect;
    @NotNull
    private int videosChannelIndex;

    private Boolean videosChannelFixed;

    public Boolean getVideosChannelFixed() {
        return this.videosChannelFixed;
    }

    public void setVideosChannelFixed(Boolean videosChannelFixed) {
        this.videosChannelFixed = videosChannelFixed;
    }

    public int getVideosChannelIndex() {
        return this.videosChannelIndex;
    }

    public void setVideosChannelIndex(int videosChannelIndex) {
        this.videosChannelIndex = videosChannelIndex;
    }

    public boolean getVideosChannelSelect() {
        return this.videosChannelSelect;
    }

    public void setVideosChannelSelect(boolean videosChannelSelect) {
        this.videosChannelSelect = videosChannelSelect;
    }

    public String getVideosChannelType() {
        return this.videosChannelType;
    }

    public void setVideosChannelType(String videosChannelType) {
        this.videosChannelType = videosChannelType;
    }

    public String getVideosChannelId() {
        return this.videosChannelId;
    }

    public void setVideosChannelId(String videosChannelId) {
        this.videosChannelId = videosChannelId;
    }

    public String getVideosChannelName() {
        return this.videosChannelName;
    }

    public void setVideosChannelName(String videosChannelName) {
        this.videosChannelName = videosChannelName;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Generated(hash = 583316491)
    public VideosChannelTable(String tableId, @NotNull String videosChannelName,
            @NotNull String videosChannelId, @NotNull String videosChannelType,
            boolean videosChannelSelect, int videosChannelIndex,
            Boolean videosChannelFixed) {
        this.tableId = tableId;
        this.videosChannelName = videosChannelName;
        this.videosChannelId = videosChannelId;
        this.videosChannelType = videosChannelType;
        this.videosChannelSelect = videosChannelSelect;
        this.videosChannelIndex = videosChannelIndex;
        this.videosChannelFixed = videosChannelFixed;
    }

    @Generated(hash = 164821763)
    public VideosChannelTable() {
    }

}
