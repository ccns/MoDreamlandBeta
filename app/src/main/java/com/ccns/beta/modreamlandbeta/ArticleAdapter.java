package com.ccns.beta.modreamlandbeta;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 2015/2/12.
 */
public class ArticleAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<ArticleItem> list;
    private ArticleFragment.OnFragmentInteractionListener mListener;

    public ArticleAdapter(Context context, List<ArticleItem> list,ArticleFragment.OnFragmentInteractionListener listener){
        myInflater = LayoutInflater.from(context);
        this.list = list;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = myInflater.inflate(R.layout.article_item, null);

        LinearLayout headerLL = (LinearLayout) convertView.findViewById(R.id.article_header);
        TextView contentTV = (TextView) convertView.findViewById(R.id.article_content);
        LinearLayout commentLL = (LinearLayout) convertView.findViewById(R.id.article_comment);
        LinearLayout pageLL = (LinearLayout) convertView.findViewById(R.id.article_page);
        TextView authorTV = (TextView) convertView.findViewById(R.id.article_author);
        TextView titleTV = (TextView) convertView.findViewById(R.id.article_title);
        TextView timeTV = (TextView) convertView.findViewById(R.id.article_time);
        TextView pageTV = (TextView) convertView.findViewById(R.id.page);
        Button prevBT = (Button) convertView.findViewById(R.id.prev);
        Button nextBT = (Button) convertView.findViewById(R.id.next);
        TextView c_pushTV = (TextView) convertView.findViewById(R.id.comment_push);
        TextView c_idTV = (TextView) convertView.findViewById(R.id.comment_id);
        TextView c_commentTV = (TextView) convertView.findViewById(R.id.comment_comment);
        TextView c_timeTV = (TextView) convertView.findViewById(R.id.comment_time);

        ArticleItem data = list.get(position);

        if (data.getArticle() != null) {
            headerLL.setVisibility(View.VISIBLE);
            contentTV.setVisibility(View.GONE);
            commentLL.setVisibility(View.GONE);
            pageLL.setVisibility(View.GONE);
            authorTV.setText(data.getArticle().getAuthor());
            titleTV.setText(data.getArticle().getTitle());
            timeTV.setText(data.getArticle().getTime());
        } else if (data.getContent() != null){
            headerLL.setVisibility(View.GONE);
            contentTV.setVisibility(View.VISIBLE);
            commentLL.setVisibility(View.GONE);
            pageLL.setVisibility(View.GONE);
            contentTV.setText(data.getContent());
        } else if (data.getPage() != null){
            headerLL.setVisibility(View.GONE);
            contentTV.setVisibility(View.GONE);
            commentLL.setVisibility(View.GONE);
            pageLL.setVisibility(View.VISIBLE);
            int[] page = data.getPage();
            pageTV.setText(String.valueOf(page[0]) + "/" + String.valueOf(page[1]));
            prevBT.setEnabled(page[0] != 1);
            nextBT.setEnabled(page[0] != page[1]);
            prevBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                    if (mListener != null) {
                        mListener.onArticleFragmentInteraction(-1);
                    }
                }
            });
            nextBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                    if (mListener != null) {
                        mListener.onArticleFragmentInteraction(1);
                    }
                }
            });
        } else if (data.getComment() != null){
            headerLL.setVisibility(View.GONE);
            contentTV.setVisibility(View.GONE);
            commentLL.setVisibility(View.VISIBLE);
            pageLL.setVisibility(View.GONE);
            Comment c = data.getComment();
            c_pushTV.setText(c.getPush());
            if(c.getPush() == "推")
                c_pushTV.setTextColor(Color.parseColor("#FFA500"));
            else if(c.getPush() == "噓")
                c_pushTV.setTextColor(Color.RED);
            c_idTV.setText(c.getID());
            c_commentTV.setText(c.getComment());
            c_timeTV.setText(c.getTime());
        }

        return convertView;
    }
}
