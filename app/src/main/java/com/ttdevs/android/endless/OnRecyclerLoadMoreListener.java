/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.endless;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ttdevs.android.utils.LogUtils;

/**
 * RecyclerView加载更多的类,支持LinearLayoutManager和GridLayoutManager
 * 使用:
 * RecyclerView.addOnScrollListener(new OnRecyclerLoadMoreListener(){
 * public void onLoadMore(){...}
 * });
 */
public abstract class OnRecyclerLoadMoreListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;
    private int mItemCount, mLastCompletely, mLastLoad;

    /**
     * load more
     */
    public abstract void onLoadMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            mItemCount = mLayoutManager.getItemCount();
            mLastCompletely = mLayoutManager.findLastCompletelyVisibleItemPosition();
        } else {
            return;
        }

        if (mLastLoad != mItemCount && mItemCount == mLastCompletely + 1) {
            LogUtils.debug(String.format("****************load more****************"));
            LogUtils.debug(String.format("mItemCount:%d \t mLastCompletely:%d", mItemCount, mLastCompletely));
            LogUtils.debug(String.format("****************load more****************"));

            mLastLoad = mItemCount;
            onLoadMore();
        }
    }
}
