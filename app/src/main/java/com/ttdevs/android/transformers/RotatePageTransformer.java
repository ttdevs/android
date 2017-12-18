package com.ttdevs.android.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;

public class RotatePageTransformer implements ViewPager.PageTransformer {
        private static float MIN_TRAN = 50f;
        private static int MIN_ROTATE = 15;

        @Override
        public void transformPage(View view, float position) {
            if (position <= 1) {
                float rotateFactor = MIN_ROTATE * position;
                float tranFactor = MIN_TRAN * position;
                if (position > 0) {
                    view.setTranslationY(tranFactor);
                } else {
                    view.setTranslationY(-tranFactor);
                }
                view.setRotation(rotateFactor);
            }
        }
    }