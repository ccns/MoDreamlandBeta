package com.ccns.beta.modreamlandbeta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ArticleListFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ARTICLE_LIST = "article_list";

    public static final int STATE_REFRESH = 1;
    public static final int STATE_ITEM_CLICKED = 0;

    private int mSection;
    private ArticleList articles;
    private ArticleListAdapter adapter;

    private OnFragmentInteractionListener mListener;

    private PullToRefreshLayout mPullToRefreshLayout;

    public static ArticleListFragment newInstance(int sectionNumber, ArticleList articles) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_ARTICLE_LIST, articles);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleListFragment() {
    }

    public PullToRefreshLayout getPullToRefreshLayout() {
        return mPullToRefreshLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSection = getArguments().getInt(ARG_SECTION_NUMBER);
            articles = new ArticleList((ArticleList)getArguments().getSerializable(ARG_ARTICLE_LIST));
        }

        adapter = new ArticleListAdapter(getActivity(), articles);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        if (null != mListener) {
                            setListShown(false);
                            mListener.onFragmentInteraction(0,STATE_REFRESH);
                        }
                    }
                })
                .setup(mPullToRefreshLayout);
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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(position,STATE_ITEM_CLICKED);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int position,int state);
    }

    public void updateList(ArticleList list) {
        articles.remove(articles.size()-1);
        articles.addAll(list.subList(articles.size(),list.size()));
        adapter.notifyDataSetChanged();
    }

    public void refreshList(ArticleList list) {
        articles.clear();
        articles.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
