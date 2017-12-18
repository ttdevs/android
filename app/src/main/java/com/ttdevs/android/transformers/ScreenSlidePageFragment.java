package com.ttdevs.android.transformers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttdevs.android.R;

import java.util.Random;

/**
 * Created by ttdevs on 16/12/2017.
 */

public class ScreenSlidePageFragment extends Fragment {
    private static final String ARG_INDEX = "key_index";

    private int mIndex;

    private static final int[] COLORS = new int[]{Color.BLUE, Color.RED,
            Color.GRAY, Color.GREEN, Color.YELLOW, Color.CYAN};
    private Random mRandom = new Random();

    public static ScreenSlidePageFragment getInstance(int index){
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_transformer, container, false);
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(String.valueOf(mIndex));
        tvContent.setBackgroundColor(COLORS[mRandom.nextInt(COLORS.length)]);
        return view;
    }
}
