package com.ccns.beta.modreamlandbeta;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Daniel on 2015/2/12.
 */
public class ArticleListAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private ArticleList articles;

    public ArticleListAdapter(Context context, ArticleList articles){
        myInflater = LayoutInflater.from(context);
        this.articles = articles;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return articles.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = myInflater.inflate(R.layout.article_list_item, null);

        TextView hotTV = (TextView) convertView.findViewById(R.id.article_list_hot);
        TextView titleTV = (TextView) convertView.findViewById(R.id.article_list_title);
        TextView timeTV = (TextView) convertView.findViewById(R.id.article_list_time);
        TextView authorTV = (TextView) convertView.findViewById(R.id.article_list_author);

        Article article = articles.get(position);

        titleTV.setText(article.getTitle());
        timeTV.setText(article.getTime());
        authorTV.setText(article.getAuthor());
        hotTV.setText(article.getHot());
        try {
            int hot = Integer.valueOf(article.getHot());
            if(hot >= 10)
                hotTV.setTextColor(Color.parseColor("#FFA500"));
        } catch (NumberFormatException e) {
            if(article.getHot() == "爆")
                hotTV.setTextColor(Color.parseColor("#FFA500"));
        }

        return convertView;
    }
}
