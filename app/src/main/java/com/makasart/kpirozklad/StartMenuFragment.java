package com.makasart.kpirozklad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private RelativeLayout circle1, circle2, seeall;
    private LinearLayout circle5, ln1, ln2, ln3, gen1;
    private boolean mIsPrepare = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.start_menu_fragment, container, false);
        mIsPrepare = false;
        PrepareBeforeStart();
        seeall = (RelativeLayout)mView.findViewById(R.id.relativelayout_seeall);
        seeall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                                R.anim.see_all_start);
                      //  animation.setFillAfter(true);
                        seeall.startAnimation(animation);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        Animation animation2 = AnimationUtils.loadAnimation(getActivity(),
                                R.anim.see_all_end);
                     //   animation2.setFillAfter(true);
                        Intent mIntent = new Intent(getActivity(), ScheduleActivity.class);
                        startActivity(mIntent);
                        getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
                        seeall.startAnimation(animation2);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
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
