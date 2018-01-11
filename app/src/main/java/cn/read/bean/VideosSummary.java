package cn.read.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lw on 2017-03-08.
 */
@Entity
public class VideosSummary implements Parcelable {
    /**
     * "topicImg": "http://vimg3.ws.126.net/image/snapshot/2017/2/V/6/VCCAHE6V6.jpg",
     * "videosource": "新媒体",
     * "mp4Hd_url": null,
     * "topicDesc": "每日更新搞笑、奇葩、监控、电视剧与电影片段剪辑等精彩视频，给您带来意想不到的快乐。",
     * "topicSid": "VCCAHE6US",
     * "cover": "http://vimg1.ws.126.net/image/snapshot/2017/3/A/0/VCE3208A0.jpg",
     * "title": "美女被歹徒们侮辱，同事们却见死不救，太可怜了！",
     * "playCount": 231,
     * "replyBoard": "video_bbs",
     * "videoTopic": {
     * "alias": "每日更新各种精彩视频。",
     * "tname": "掌中视频",
     * "ename": "T1487055580093",
     * "tid": "T1487055580093",
     * "topic_icons": "http://dingyue.nosdn.127.net/4NZReSvnqifjF9wnICZpTiXmbd7gzzBLRAwh0UHjPYUyO1487055579654.jpg"
     * },
     * "sectiontitle": "",
     * "replyid": "CE0HOR72008535RB",
     * "description": "",
     * "mp4_url": "http://flv2.bn.netease.com/videolib3/1703/07/iZMjf7817/SD/iZMjf7817-mobile.mp4",
     * "length": 143,
     * "playersize": 1,
     * "m3u8Hd_url": null,
     * "vid": "VCE0HOR72",
     * "m3u8_url": "http://flv2.bn.netease.com/videolib3/1703/07/iZMjf7817/SD/movie_index.m3u8",
     * "ptime": "2017-03-07 14:24:46",
     * "topicName": "掌中视频"
     */
    private String topicImg;

    private String videosource;

    private String mp4Hd_url;

    private String topicDesc;

    private String topicSid;

    private String cover;

    private String title;

    private int playCount;

    private String replyBoard;

    @Transient
    private VideoTopic videoTopic;

    private String sectiontitle;

    private String replyid;

    private String description;

    @Id
    private String mp4_url;

    private int length;

    private int playersize;

    private String m3u8Hd_url;

    private String vid;

    private String m3u8_url;

    private String ptime;

    private String topicName;

    private boolean isFavorites;

    @Generated(hash = 410056180)
    public VideosSummary(String topicImg, String videosource, String mp4Hd_url, String topicDesc, String topicSid, String cover, String title,
                         int playCount, String replyBoard, String sectiontitle, String replyid, String description, String mp4_url, int length,
                         int playersize, String m3u8Hd_url, String vid, String m3u8_url, String ptime, String topicName, boolean isFavorites) {
        this.topicImg = topicImg;
        this.videosource = videosource;
        this.mp4Hd_url = mp4Hd_url;
        this.topicDesc = topicDesc;
        this.topicSid = topicSid;
        this.cover = cover;
        this.title = title;
        this.playCount = playCount;
        this.replyBoard = replyBoard;
        this.sectiontitle = sectiontitle;
        this.replyid = replyid;
        this.description = description;
        this.mp4_url = mp4_url;
        this.length = length;
        this.playersize = playersize;
        this.m3u8Hd_url = m3u8Hd_url;
        this.vid = vid;
        this.m3u8_url = m3u8_url;
        this.ptime = ptime;
        this.topicName = topicName;
        this.isFavorites = isFavorites;
    }

    @Generated(hash = 1172211060)
    public VideosSummary() {
    }

    public String getTopicImg() {
        return topicImg;
    }

    public void setTopicImg(String topicImg) {
        this.topicImg = topicImg;
    }

    public String getVideosource() {
        return videosource;
    }

    public void setVideosource(String videosource) {
        this.videosource = videosource;
    }

    public String getMp4Hd_url() {
        return mp4Hd_url;
    }

    public void setMp4Hd_url(String mp4Hd_url) {
        this.mp4Hd_url = mp4Hd_url;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }

    public String getTopicSid() {
        return topicSid;
    }

    public void setTopicSid(String topicSid) {
        this.topicSid = topicSid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getReplyBoard() {
        return replyBoard;
    }

    public void setReplyBoard(String replyBoard) {
        this.replyBoard = replyBoard;
    }

    public VideoTopic getVideoTopic() {
        return videoTopic;
    }

    public void setVideoTopic(VideoTopic videoTopic) {
        this.videoTopic = videoTopic;
    }

    public String getSectiontitle() {
        return sectiontitle;
    }

    public void setSectiontitle(String sectiontitle) {
        this.sectiontitle = sectiontitle;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPlayersize() {
        return playersize;
    }

    public void setPlayersize(int playersize) {
        this.playersize = playersize;
    }

    public String getM3u8Hd_url() {
        return m3u8Hd_url;
    }

    public void setM3u8Hd_url(String m3u8Hd_url) {
        this.m3u8Hd_url = m3u8Hd_url;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getM3u8_url() {
        return m3u8_url;
    }

    public void setM3u8_url(String m3u8_url) {
        this.m3u8_url = m3u8_url;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public class VideoTopic implements Parcelable {
        private String alias;

        private String tname;

        private String ename;

        private String tid;

        private String topic_icons;

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getAlias() {
            return alias;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTopic_icons() {
            return topic_icons;
        }

        public void setTopic_icons(String topic_icons) {
            this.topic_icons = topic_icons;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.alias);
            dest.writeString(this.tname);
            dest.writeString(this.ename);
            dest.writeString(this.tid);
            dest.writeString(this.topic_icons);
        }

        public VideoTopic() {
        }

        protected VideoTopic(Parcel in) {
            this.alias = in.readString();
            this.tname = in.readString();
            this.ename = in.readString();
            this.tid = in.readString();
            this.topic_icons = in.readString();
        }

        public final Creator<VideoTopic> CREATOR = new Creator<VideoTopic>() {
            @Override
            public VideoTopic createFromParcel(Parcel source) {
                return new VideoTopic(source);
            }

            @Override
            public VideoTopic[] newArray(int size) {
                return new VideoTopic[size];
            }
        };
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VideosSummary)) {
            return false;
        }
        VideosSummary other = (VideosSummary) o;
        if (mp4_url.equals(other.getMp4_url())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return mp4_url.hashCode();
    }

    public boolean getIsFavorites() {
        return this.isFavorites;
    }

    public void setIsFavorites(boolean isFavorites) {
        this.isFavorites = isFavorites;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topicImg);
        dest.writeString(this.videosource);
        dest.writeString(this.mp4Hd_url);
        dest.writeString(this.topicDesc);
        dest.writeString(this.topicSid);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeInt(this.playCount);
        dest.writeString(this.replyBoard);
        dest.writeParcelable(this.videoTopic, flags);
        dest.writeString(this.sectiontitle);
        dest.writeString(this.replyid);
        dest.writeString(this.description);
        dest.writeString(this.mp4_url);
        dest.writeInt(this.length);
        dest.writeInt(this.playersize);
        dest.writeString(this.m3u8Hd_url);
        dest.writeString(this.vid);
        dest.writeString(this.m3u8_url);
        dest.writeString(this.ptime);
        dest.writeString(this.topicName);
        dest.writeByte(this.isFavorites ? (byte) 1 : (byte) 0);
    }

    protected VideosSummary(Parcel in) {
        this.topicImg = in.readString();
        this.videosource = in.readString();
        this.mp4Hd_url = in.readString();
        this.topicDesc = in.readString();
        this.topicSid = in.readString();
        this.cover = in.readString();
        this.title = in.readString();
        this.playCount = in.readInt();
        this.replyBoard = in.readString();
        this.videoTopic = in.readParcelable(VideoTopic.class.getClassLoader());
        this.sectiontitle = in.readString();
        this.replyid = in.readString();
        this.description = in.readString();
        this.mp4_url = in.readString();
        this.length = in.readInt();
        this.playersize = in.readInt();
        this.m3u8Hd_url = in.readString();
        this.vid = in.readString();
        this.m3u8_url = in.readString();
        this.ptime = in.readString();
        this.topicName = in.readString();
        this.isFavorites = in.readByte() != 0;
    }

    @Transient
    public static final Parcelable.Creator<VideosSummary> CREATOR = new Parcelable.Creator<VideosSummary>() {
        @Override
        public VideosSummary createFromParcel(Parcel source) {
            return new VideosSummary(source);
        }

        @Override
        public VideosSummary[] newArray(int size) {
            return new VideosSummary[size];
        }
    };
}
