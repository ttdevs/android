package com.ttdevs.android.transformers;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.ttdevs.android.utils.LogUtils;

public class ScaleYTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;
    @Override
    public void transformPage(View page, float position) {
        LogUtils.debug(String.format("Position: %f", position));

        if(position < -1){
            page.setScaleY(MIN_SCALE);
        }else if(position<= 1){
            //
            float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
            page.setScaleY(scale);
            /*page.setScaleX(scale);
            if(position<0){
                page.setTranslationX(width * (1 - scale) /2);
            }else{
                page.setTranslationX(-width * (1 - scale) /2);
            }*/

        }else{
            page.setScaleY(MIN_SCALE);
        }
    }

}