package com.ccns.beta.modreamlandbeta;

import java.util.ArrayList;

/**
 * Created by Daniel on 2015/2/13.
 */
public class BoardList extends ArrayList<ArticleList>{
    public BoardList() {

    }
    public ArticleList getArticlesByCode(String code) {
        for(int i=0;i<this.size();i++) {
            if(this.get(i).getBoardCode() == code)
                return this.get(i);
        }
        return null;
    }
    public void addArticleListByCode(String code) {
        this.add(new ArticleList(code));
    }
    public void clearArticleByCode(String code) {
        for(int i=0;i<this.size();i++)
            if (this.get(i).getBoardCode() == code)
                this.get(i).clear();
    }
}
