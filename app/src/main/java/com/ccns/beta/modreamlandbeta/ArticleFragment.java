package com.ccns.beta.modreamlandbeta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ArticleFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARTICLE = "article";
    private static final String ARG_PAGE = "page";

    private ListView mListView;

    private Article article;
    private int[] page;

    private ArticleAdapter adapter;
    private ArrayList<ArticleItem> articleItems;

    private OnFragmentInteractionListener mListener;

    public static ArticleFragment newInstance(Bundle info) {
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(info);
        return fragment;
    }

    public ArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            article = (Article)getArguments().getSerializable(ARG_ARTICLE);
            page = getArguments().getIntArray(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_article, container, false);
        articleItems = new ArrayList<>();
        setArticleItems();
        adapter = new ArticleAdapter(getActivity(), articleItems, mListener);
        mListView.setAdapter(adapter);
        return mListView;
    }

    private void setArticleItems() {
        if(page[0]==1)
            articleItems.add(new ArticleItem(article));
        ArrayList<char[]> content = article.getContent();
        for(int i=0;i<content.size();i++){
            char[] str = content.get(i);
            if(ArticleItem.isComment(str))
                articleItems.add(new ArticleItem(new Comment(str)));
            else
                articleItems.add(new ArticleItem(new String(str)));
        }
        articleItems.add(new ArticleItem(page));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onArticleFragmentInteraction(int move);
    }
}
