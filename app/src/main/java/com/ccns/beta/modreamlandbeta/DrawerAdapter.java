package com.ccns.beta.modreamlandbeta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 2015/2/12.
 */
public class DrawerAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<DrawerItem> list;

    public DrawerAdapter(Context context, List<DrawerItem> list){
        myInflater = LayoutInflater.from(context);
        this.list = list;
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
            convertView = myInflater.inflate(R.layout.drawer_list_item, null);

        TextView hotTV = (TextView) convertView.findViewById(R.id.board_list_hot);
        TextView codeTV = (TextView) convertView.findViewById(R.id.board_list_code);
        TextView titleTV = (TextView) convertView.findViewById(R.id.board_list_title);
        TextView drawerTitleTV = (TextView) convertView.findViewById(R.id.drawer_title);
        LinearLayout itemsLL = (LinearLayout) convertView.findViewById(R.id.board_list_items_layout);
        LinearLayout titleLL = (LinearLayout) convertView.findViewById(R.id.drawer_title_layout);

        DrawerItem data = list.get(position);

        if (data.getDrawerTitle() != null) {
            titleLL.setVisibility(LinearLayout.VISIBLE);
            itemsLL.setVisibility(LinearLayout.GONE);
            drawerTitleTV.setText(data.getDrawerTitle());
            convertView.setClickable(false);
        } else {
            titleLL.setVisibility(LinearLayout.GONE);
            itemsLL.setVisibility(LinearLayout.VISIBLE);
            titleTV.setText(data.getTitle());
            hotTV.setText(data.getHot());
            codeTV.setText(data.getCode());
        }

        return convertView;
    }
}
