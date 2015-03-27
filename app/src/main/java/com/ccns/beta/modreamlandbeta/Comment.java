package com.ccns.beta.modreamlandbeta;

import java.io.Serializable;

/**
 * Created by Daniel on 2015/2/13.
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = -70602112300464481L;

    private String push;
    private String id;
    private String comment;
    private String time;

    public Comment(String push, String id, String comment, String time){
        this.push = push;
        this.id = id;
        this.comment = comment;
        this.time = time;
    }

    public Comment(char[] str){
        String push = new String(str,0,2).replaceAll("\u0000","");
        String id = new String(str,3,12).replaceAll("\u0000","").replaceAll(" ","");
        String comment = new String(str,17,52).replaceAll("\u0000", "");
        String time = new String(str,72,5).replaceAll("\u0000", "");
        this.push = push;
        this.id = id;
        this.comment = comment;
        this.time = time;
    }

    public String getPush() {return push;}
    public String getID() {return id;}
    public String getComment() {return comment;}
    public String getTime() {return time;}

}
