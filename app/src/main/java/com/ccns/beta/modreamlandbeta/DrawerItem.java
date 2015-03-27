package com.ccns.beta.modreamlandbeta;

import java.util.HashMap;

/**
 * Created by Daniel on 2015/2/12.
 */
public class DrawerItem {

    HashMap<String,String> data;

    String title;
    boolean isRegular;

    public DrawerItem(HashMap<String,String> data) {
        super();
        this.data = data;
    }

    public DrawerItem(String title) {
        this.title = title;
    }

    public String getHot() {
        return data.get("hot");
    }
    public void setHot(String hot) {
        data.put("hot",hot);
    }
    public String getCode() {
        return data.get("code");
    }
    public void setCode(String code) {
        data.put("code",code);
    }
    public String getTitle() {
        return data.get("title");
    }
    public void setTitle(String title) {
        data.put("title",title);
    }
    public HashMap<String,String> getData() {
        return data;
    }
    public void setData(HashMap<String,String> data) {
        this.data = data;
    }

    public String getDrawerTitle() {
        return title;
    }

    public void setDrawerTitle(String title) {
        this.title = title;
    }

    public boolean isRegular() {
        return isRegular;
    }
}
