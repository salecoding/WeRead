package cn.read.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lw on 2017-03-09.
 * 聊天记录
 */
@Entity
public class ChatLog {
    @Id
    private String chatLogId;
    @NotNull
    private int type;
    @NotNull
    private String content;
    @NotNull
    private String createBy;
    @NotNull
    private Date createDate;
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return this.createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getChatLogId() {
        return this.chatLogId;
    }
    public void setChatLogId(String chatLogId) {
        this.chatLogId = chatLogId;
    }
    @Generated(hash = 1112414005)
    public ChatLog(String chatLogId, int type, @NotNull String content,
            @NotNull String createBy, @NotNull Date createDate) {
        this.chatLogId = chatLogId;
        this.type = type;
        this.content = content;
        this.createBy = createBy;
        this.createDate = createDate;
    }
    @Generated(hash = 1994978153)
    public ChatLog() {
    }
    
}
