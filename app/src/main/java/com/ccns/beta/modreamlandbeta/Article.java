package com.ccns.beta.modreamlandbeta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniel on 2015/2/13.
 */
public class Article implements Serializable {

    private static final long serialVersionUID = -70602112300464481L;

    private int num;
    private String title;
    private String hot;
    private String author;
    private String board;
    private String time;
    private ArrayList<char[]> content;

    public Article() {
        content = new ArrayList<>();
    }

    public Article(int num,String title){
        this.num = num;
        this.title = title;
    }

    public Article(int num, String title, String hot, String author, String time) {
        this.num = num;
        this.title = title;
        this.hot = hot;
        this.author = author;
        this.time = time;
    }

    public void setNum(int num) {this.num = num;}
    public int getNum() {return num;}
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}
    public void setAuthor(String author) { this.author = author; }
    public String getAuthor() {return author;}
    public void setBoard(String board) {this.board = board;}
    public String getBoard() {return board;}
    public void setTime(String time) {this.time = time;}
    public String getTime() {return time;}
    public void setHot(String hot) {this.hot = hot;}
    public String getHot() {return hot;}
    public void addContent(ArrayList<char[]> content) { this.content.addAll(content); }
    public ArrayList<char[]> getContent(int page) {
        int linesPerPage = 23;
        int start = (page-1)*linesPerPage;
        if(page == 1)
            linesPerPage = 20;
        return new ArrayList<>(content.subList(start, start + linesPerPage));
    }
    public ArrayList<char[]> getContent() { return content; }
}
