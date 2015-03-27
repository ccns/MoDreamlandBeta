package com.ccns.beta.modreamlandbeta;

import java.io.Serializable;
import java.util.ArrayList;

public class ArticleList extends ArrayList<Article> implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    public String board_code;

    public ArticleList() {

    }

    public ArticleList(String board_code) {
        this.board_code = board_code;
    }
    public ArticleList(ArticleList articles) {
        this.addAll(articles);
    }
    public void add(int num,String title) { this.add(new Article(num,title)); }
    public void setBoardCode (String board_code) { this.board_code = board_code; }
    public String getBoardCode () { return board_code; }
    public Article getLastArticle() { return this.get(this.size()-2); }
    public ArrayList<String> getTitleList() {
        ArrayList<String> titles = new ArrayList<>();
        for(int i=0;i<this.size();i++)
            titles.add(this.get(i).getTitle());
        return titles;
    }
}
