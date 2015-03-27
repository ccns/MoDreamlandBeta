package com.ccns.beta.modreamlandbeta;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {
    private static final String ARG_PB = "pb";
    private static final String ARG_TEXT = "text";

    private boolean pb;
    private String text;

    public static WelcomeFragment newInstance(boolean isPB, String text) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PB, isPB);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pb = getArguments().getBoolean(ARG_PB);
            text = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);

        TextView TV = (TextView) v.findViewById(R.id.tvw);
        ProgressBar PB = (ProgressBar) v.findViewById(R.id.pb);

        if(!pb)
            PB.setVisibility(View.GONE);
        TV.setText(text);
        return v;
    }


}
