package cn.read.bean;

/**
 * Created by lw on 2017/2/18.
 */

public class Chat {
    private int type;
    private String content;

    public Chat(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
