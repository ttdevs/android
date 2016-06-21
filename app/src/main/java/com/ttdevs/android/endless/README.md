# Endless

好久没有写博客，最近正好有时间，打算把之前很长很长一段时间落下的坑都给填了。
在Android的Support包中我们有很多新的组件可以使用，比如代替ListView的RecyclerView。使用RecyclerView和SwipeRefreshLayout组合，我们可以实现很酷炫的下拉刷新的功能。但是，事情总不是那么美好，有了下拉刷新，却找不到上拉加载更多（我们已经习惯了PullToRefresh），最终还是放弃了使用RecyclerView。现在看来，自己实在是太懒了。好了，今天就让我们的RecyclerView支持加载更多。
RecyclerView的的使用多数是与SwipeRefreshLayout进行组合，当然，你也可以算上CardView。使用SwipeRefreshLayout时，受PullToRefresh的影响，很自然的会想到给SwipeRefreshLayout加个上拉加载更多的功能。如果你也这么想，那么我们就一起来YY下google为什么没有官方支持这么做。

1. 给SwipeRefreshLayout添加加载更多功能：我们期望的(比如：SwipeRefreshLayout + RecyclerView，但是google没这么做)。如果SwipeRefreshLayout中是TextView或者自定义的View，很显然，我们并不能保证这个加载更多是可以实现的（即使实现了，他们耦合也会很高）。
2. 给RecyclerView添加加载更多功能：这个方案是可行的
3. 给其他更多SwipeRefreshLayout包含的view添加加载更多功能：第二种情况的扩展，这个根据具体的view来讨论

因此，我们应该把加载更多放到具体的view中。下面我们支持加载更多的代码：

``` java
/*
 * Created by ttdevs 9/3/15 1:31 PM. https://github.com/ttdevs
 * Copyright (c) 2015 ttdevs, Inc. All rights reserved.
 */

package com.ttdevs.support.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ttdevs.utils.LogUtils;

/**
 * RecyclerView加载更多的类,支持LinearLayoutManager和GridLayoutManager
 * 使用:
 * RecyclerView.addOnScrollListener(new OnRecyclerLoadMoreListener(){
 *  public void onLoadMore(){...}
 *  });
 */
public abstract class OnRecyclerLoadMoreListener extends RecyclerView.OnScrollListener{
        
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
```
