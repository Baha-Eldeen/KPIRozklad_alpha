package com.makasart.kpirozklad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Maxim on 31.10.2016.
 */

public class StartMenuFragment extends Fragment {
    View mView;
    private RelativeLayout circle1, circle2;
    private LinearLayout circle5, ln1, ln2, ln3, gen1;
    private boolean mIsPrepare = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.start_menu_fragment, container, false);
        mIsPrepare = false;
        PrepareBeforeStart();
        return mView;
    }

    private void PrepareBeforeStart() {
        circle1 = (RelativeLayout)mView.findViewById(R.id.Relative_layout_with_number_of_tasks);
        circle2 = (RelativeLayout)mView.findViewById(R.id.Relative_layout_with_number_of_notes);
        circle5 = (LinearLayout)mView.findViewById(R.id.Relative_layout_with_circle_bottom);
        ln1 = (LinearLayout)mView.findViewById(R.id.layout_with_circle_up);
        ln2 = (LinearLayout)mView.findViewById(R.id.layout_with_circle_middle);
        ln3 = (LinearLayout)mView.findViewById(R.id.layout_with_circle_bottom);
        gen1 = (LinearLayout) mView.findViewById(R.id.general_layout);
        if (!mIsPrepare) {
            ViewTreeObserver observer = gen1.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (StartMenuActivity.orientation == 2) {
                        gen1.getLayoutParams().height = StartMenuActivity.width;
                    }
                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) circle1.getLayoutParams();
                    params1.width = circle1.getHeight();
                    circle1.setLayoutParams(params1);
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) circle2.getLayoutParams();
                    params2.width = circle2.getHeight();
                    circle2.setLayoutParams(params2);
                    LinearLayout.LayoutParams par1 = (LinearLayout.LayoutParams) ln1.getLayoutParams();
                    par1.width = circle5.getHeight();
                    ln1.setLayoutParams(par1);
                    LinearLayout.LayoutParams par2 = (LinearLayout.LayoutParams) ln2.getLayoutParams();
                    par2.width = circle5.getHeight();
                    ln2.setLayoutParams(par2);
                    LinearLayout.LayoutParams par3 = (LinearLayout.LayoutParams) ln3.getLayoutParams();
                    par3.width = circle5.getHeight();
                    ln3.setLayoutParams(par3);
                    mIsPrepare = true;
                }
            });
        }
    }
}
