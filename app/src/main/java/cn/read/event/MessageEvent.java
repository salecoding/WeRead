package cn.read.event;

/**
 * Created by lw on 2017-03-09.
 */

public class MessageEvent {
    private String tag;
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public MessageEvent(String tag) {
        this.tag = tag;
    }

    public MessageEvent(String tag, Object message) {
        this.tag = tag;
        this.message = message;
    }
}
