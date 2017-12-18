package com.ttdevs.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ttdevs.android.transformers.CoverModeTransformer;
import com.ttdevs.android.transformers.ScreenSlidePageFragment;

public class TransformerActivity extends BaseActivity {

    private ViewPager vpContent;
    private FragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformer);

        vpContent = findViewById(R.id.vpContent);
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        vpContent.setAdapter(mAdapter);
        vpContent.setOffscreenPageLimit(4);
//        vpContent.setPageTransformer(true, new ZoomOutPageTransformer());
//        vpContent.setPageTransformer(true, new DepthPageTransformer());
//        vpContent.setPageTransformer(true, new MyPageTransform());
//        vpContent.setPageTransformer(false, new ScaleYTransformer());
        vpContent.setPageTransformer(true, new CoverModeTransformer(vpContent));
//        vpContent.setPageTransformer(true, new RotatePageTransformer());
//        vpContent.setPageTransformer(true, new SelfTransformer());
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
