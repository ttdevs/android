/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.android.endless;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EndlessAdapter extends RecyclerView.Adapter<EndlessAdapter.ViewHolder> {

    private List<String> mDataList;

    public EndlessAdapter(List<String> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        TextView view = new TextView(viewGroup.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        // viewHolder.setIsRecyclable(false); // TODO

        final String content = mDataList.get(index);

        viewHolder.tv_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;

        public ViewHolder(TextView view) {
            super(view);

            tv_content = view;
        }
    }
}
