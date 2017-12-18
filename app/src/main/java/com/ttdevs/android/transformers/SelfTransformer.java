package com.ttdevs.android.transformers;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by ttdevs on 17/12/2017.
 */

public class SelfTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.8F;

    @Override
    public void transformPage(@NonNull View page, float position) {
        int width = page.getContext().getResources().getDisplayMetrics().widthPixels;

        if (position < -1 || position > 1) {
            page.setScaleY(MIN_SCALE);
            return;
        }

        float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
        page.setScaleY(scale);
        page.setScaleX(scale);
        if (position < 0) {
            page.setTranslationX(width * (1 - scale) / 2);
        } else {
            page.setTranslationX(-width * (1 - scale) / 2);
        }
    }
}
