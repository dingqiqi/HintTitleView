package com.dingqiqi.hinttitlerecycleview;

/**
 * Created by Administrator on 2016/12/2.
 */
public class Mode {

    private String name;
    private String title;
    private String tag;

    public Mode() {
    }

    public Mode(String name, String title, String tag) {
        this.name = name;
        this.title = title;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
