/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.endless;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ttdevs.android.R;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutFragment extends Fragment {

    private List<String> mDataList = new ArrayList<>();
    private EndlessAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView rvMain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 100; i++) {
            mDataList.add(String.format("index: %d", i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_endless_linear, container, false);
        srlRefresh = (SwipeRefreshLayout) inflate.findViewById(R.id.srl_refresh);
        rvMain = (RecyclerView) inflate.findViewById(R.id.rv_main);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMain.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvMain.setLayoutManager(mLayoutManager);
        mAdapter = new EndlessAdapter(mDataList);
        rvMain.setAdapter(mAdapter);

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlRefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        rvMain.addOnScrollListener(new OnRecyclerLoadMoreListener() {
            @Override
            public void onLoadMore() {
                int index = mDataList.size();
                for (int i = 1; i < 21; i++) {
                    mDataList.add(String.format("index: %d", index + i));
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
