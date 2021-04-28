package com.ttdevs.android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class TempActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        View root_view = findViewById(R.id.activity_temp);
        while (root_view != null) {
            print(root_view.getClass().toString());
            if (root_view instanceof ViewGroup) {
                int child_count = ((ViewGroup) root_view).getChildCount();
                for (int i = 0; i < child_count; i++) {
                    View v = ((ViewGroup) root_view).getChildAt(i);
                    print("\t child:" + v.getClass().toString());
                }
            }
            root_view = (View) root_view.getParent();
        }
    }

    private void print(String msg){
        System.out.println(">>>>>" + msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btSet:
//
//                break;

            default:
                break;
        }
    }
}
