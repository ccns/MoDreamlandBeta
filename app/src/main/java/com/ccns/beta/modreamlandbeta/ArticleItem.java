package com.ccns.beta.modreamlandbeta;

/**
 * Created by Daniel on 2015/2/12.
 */
public class ArticleItem {

    private Article article;
    private String content;
    private int[] page;
    private Comment comment;

    public ArticleItem(Article article) {
        super();
        this.article = article;
    }
    public ArticleItem(String content) {
        super();
        this.content = content;
    }
    public ArticleItem(int[] page) {
        super();
        this.page = page;
    }
    public ArticleItem(Comment comment) {
        super();
        this.comment = comment;
    }

    public Article getArticle() {
        return article;
    }
    public String getContent() {
        return content;
    }
    public int[] getPage() {
        return page;
    }
    public Comment getComment() {
        return comment;
    }

    public static boolean isComment(char[] str) {
        if(str.length>74)
            return (str[16]=='：' && str[74]=='/');
        else
            return false;
    }
}
