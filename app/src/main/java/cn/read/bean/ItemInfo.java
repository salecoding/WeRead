package cn.read.bean;

/**
 * Created by lw on 2017-03-16.
 */

public class ItemInfo {
    private String id;
    private int icon;
    private String desc;

    public ItemInfo(String id, int icon, String desc) {
        this.id = id;
        this.icon = icon;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
