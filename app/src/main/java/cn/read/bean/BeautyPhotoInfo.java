package cn.read.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by lw on 2017-03-10.
 */
@Entity
public class BeautyPhotoInfo implements Parcelable {
    /**
     * adtype : 0
     * boardid : comment_bbs
     * clkNum : 0
     * digest : 这表情一定是又淘气了
     * docid : C6IOR7N19001R7N2
     * downTimes : 573
     * img : http://dmr.nosdn.127.net/oVrCGPgU7jQCWgjRJ16FFA==/6896093022718795850.jpg
     * imgType : 0
     * imgsrc : http://dmr.nosdn.127.net/oVrCGPgU7jQCWgjRJ16FFA==/6896093022718795850.jpg
     * imgsum : 0
     * picCount : 0
     * pixel : 658*790
     * program : HY
     * prompt : 成功为您推荐1条新内容
     * recNews : 0
     * recType : 0
     * refreshPrompt : 0
     * replyCount : 131
     * replyid : C6IOR7N19001R7N2
     * source : 一点资讯
     * title : 这表情一定是又淘气了
     * upTimes : 1929
     */
    private int adtype;
    private String boardid;
    private int clkNum;
    private String digest;
    private String docid;
    private int downTimes;
    private String img;
    private int imgType;
    @Id
    private String imgsrc;
    private int imgsum;
    private int picCount;
    private String pixel;
    private String program;
    private String prompt;
    private int recNews;
    private int recType;
    private int refreshPrompt;
    private int replyCount;
    private String replyid;
    private String source;
    private String title;
    private int upTimes;
    // 喜欢
    private boolean isLove;
    // 点赞
    private boolean isPraise;
    // 下载
    private boolean isDownload;

    @Generated(hash = 1378547188)
    public BeautyPhotoInfo(int adtype, String boardid, int clkNum, String digest,
                           String docid, int downTimes, String img, int imgType, String imgsrc, int imgsum,
                           int picCount, String pixel, String program, String prompt, int recNews,
                           int recType, int refreshPrompt, int replyCount, String replyid, String source,
                           String title, int upTimes, boolean isLove, boolean isPraise, boolean isDownload) {
        this.adtype = adtype;
        this.boardid = boardid;
        this.clkNum = clkNum;
        this.digest = digest;
        this.docid = docid;
        this.downTimes = downTimes;
        this.img = img;
        this.imgType = imgType;
        this.imgsrc = imgsrc;
        this.imgsum = imgsum;
        this.picCount = picCount;
        this.pixel = pixel;
        this.program = program;
        this.prompt = prompt;
        this.recNews = recNews;
        this.recType = recType;
        this.refreshPrompt = refreshPrompt;
        this.replyCount = replyCount;
        this.replyid = replyid;
        this.source = source;
        this.title = title;
        this.upTimes = upTimes;
        this.isLove = isLove;
        this.isPraise = isPraise;
        this.isDownload = isDownload;
    }

    @Generated(hash = 827125854)
    public BeautyPhotoInfo() {
    }

    public boolean isLove() {
        return isLove;
    }

    public void setLove(boolean love) {
        isLove = love;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public int getAdtype() {
        return adtype;
    }

    public void setAdtype(int adtype) {
        this.adtype = adtype;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public int getClkNum() {
        return clkNum;
    }

    public void setClkNum(int clkNum) {
        this.clkNum = clkNum;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public int getDownTimes() {
        return downTimes;
    }

    public void setDownTimes(int downTimes) {
        this.downTimes = downTimes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getImgsum() {
        return imgsum;
    }

    public void setImgsum(int imgsum) {
        this.imgsum = imgsum;
    }

    public int getPicCount() {
        return picCount;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getRecNews() {
        return recNews;
    }

    public void setRecNews(int recNews) {
        this.recNews = recNews;
    }

    public int getRecType() {
        return recType;
    }

    public void setRecType(int recType) {
        this.recType = recType;
    }

    public int getRefreshPrompt() {
        return refreshPrompt;
    }

    public void setRefreshPrompt(int refreshPrompt) {
        this.refreshPrompt = refreshPrompt;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpTimes() {
        return upTimes;
    }

    public void setUpTimes(int upTimes) {
        this.upTimes = upTimes;
    }

    public boolean getIsDownload() {
        return this.isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public boolean getIsPraise() {
        return this.isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

    public boolean getIsLove() {
        return this.isLove;
    }

    public void setIsLove(boolean isLove) {
        this.isLove = isLove;
    }

    @Override
    public String toString() {
        return "BeautyPhotoInfo{" +
                "adtype=" + adtype +
                ", boardid='" + boardid + '\'' +
                ", clkNum=" + clkNum +
                ", digest='" + digest + '\'' +
                ", docid='" + docid + '\'' +
                ", downTimes=" + downTimes +
                ", img='" + img + '\'' +
                ", imgType=" + imgType +
                ", imgsrc='" + imgsrc + '\'' +
                ", imgsum=" + imgsum +
                ", picCount=" + picCount +
                ", pixel='" + pixel + '\'' +
                ", program='" + program + '\'' +
                ", prompt='" + prompt + '\'' +
                ", recNews=" + recNews +
                ", recType=" + recType +
                ", refreshPrompt=" + refreshPrompt +
                ", replyCount=" + replyCount +
                ", replyid='" + replyid + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", upTimes=" + upTimes +
                ", isLove=" + isLove +
                ", isPraise=" + isPraise +
                ", isDownload=" + isDownload +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.adtype);
        dest.writeString(this.boardid);
        dest.writeInt(this.clkNum);
        dest.writeString(this.digest);
        dest.writeString(this.docid);
        dest.writeInt(this.downTimes);
        dest.writeString(this.img);
        dest.writeInt(this.imgType);
        dest.writeString(this.imgsrc);
        dest.writeInt(this.imgsum);
        dest.writeInt(this.picCount);
        dest.writeString(this.pixel);
        dest.writeString(this.program);
        dest.writeString(this.prompt);
        dest.writeInt(this.recNews);
        dest.writeInt(this.recType);
        dest.writeInt(this.refreshPrompt);
        dest.writeInt(this.replyCount);
        dest.writeString(this.replyid);
        dest.writeString(this.source);
        dest.writeString(this.title);
        dest.writeInt(this.upTimes);
        dest.writeByte(this.isLove ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPraise ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDownload ? (byte) 1 : (byte) 0);
    }

    protected BeautyPhotoInfo(Parcel in) {
        this.adtype = in.readInt();
        this.boardid = in.readString();
        this.clkNum = in.readInt();
        this.digest = in.readString();
        this.docid = in.readString();
        this.downTimes = in.readInt();
        this.img = in.readString();
        this.imgType = in.readInt();
        this.imgsrc = in.readString();
        this.imgsum = in.readInt();
        this.picCount = in.readInt();
        this.pixel = in.readString();
        this.program = in.readString();
        this.prompt = in.readString();
        this.recNews = in.readInt();
        this.recType = in.readInt();
        this.refreshPrompt = in.readInt();
        this.replyCount = in.readInt();
        this.replyid = in.readString();
        this.source = in.readString();
        this.title = in.readString();
        this.upTimes = in.readInt();
        this.isLove = in.readByte() != 0;
        this.isPraise = in.readByte() != 0;
        this.isDownload = in.readByte() != 0;
    }

    @Transient
    public static final Creator<BeautyPhotoInfo> CREATOR = new Creator<BeautyPhotoInfo>() {
        @Override
        public BeautyPhotoInfo createFromParcel(Parcel source) {
            return new BeautyPhotoInfo(source);
        }

        @Override
        public BeautyPhotoInfo[] newArray(int size) {
            return new BeautyPhotoInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BeautyPhotoInfo)) {
            return false;
        }
        BeautyPhotoInfo other = (BeautyPhotoInfo) o;
        if (imgsrc.equals(other.getImgsrc())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return imgsrc.hashCode();
    }
}
