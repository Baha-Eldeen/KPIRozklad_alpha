package com.makasart.kpirozklad;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Maxim on 31.10.2016.
 */

public class StartMenuFragment extends Fragment {
    View mView;
    private RelativeLayout circle1, circle2, seeall;
    private LinearLayout circle5, ln1, ln2, ln3, gen1;
    private boolean mIsPrepare = false;
    private JsonParser jsonParser;
    Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.start_menu_fragment, container, false);
        mIsPrepare = false;
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getActivity().getApplicationContext(),
                        (String) msg.obj, Toast.LENGTH_LONG).show();
            }

        };
        PrepareBeforeStart();
        firstLoadJson();
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
                        if (jsonParser.mLoad && jsonParser.mSave) {  //load only if json loaded and save
                            Intent mIntent = new Intent(getActivity(), ScheduleActivity.class);
                            //   mIntent.putExtra("Json_Parser", (Serializable) jsonParser);
                            startActivity(mIntent);
                            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json don't loaded! Please wait to load json!", Toast.LENGTH_SHORT).show();
                        }
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

    private void firstLoadJson() {
        //Create custom json parser
        jsonParser = new JsonParser(getActivity().getApplicationContext());
        String message = "Started load json! Please wait to next toast!";
        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = message; // Put the string into Message, into "obj" field.
        msg.setTarget(mHandler); // Set the Handler
        msg.sendToTarget(); //Send the message
      //  mHandler.sendMessage("Started load json! Please wait to next toast!");
     //   Toast.makeText(getActivity().getApplicationContext(),
     //           "Started load json! Please wait to next toast!", Toast.LENGTH_LONG).show();
        jsonParser.loadJsonFile();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 80; i++) {  //thread where we wait to load json and then saved it
                    if (jsonParser.mLoad) {  //check that json loaded
                        String message = "Json loaded! Please wait to next toast!";
                        Message msg = Message.obtain(); // Creates an new Message instance
                        msg.obj = message; // Put the string into Message, into "obj" field.
                        msg.setTarget(mHandler); // Set the Handler
                        msg.sendToTarget(); //Send the message
                      //  Toast.makeText(getActivity().getApplicationContext(),
                      //          "Json loaded! Please wait to next toast!", Toast.LENGTH_SHORT).show();
                        try {  //saved json in file if that saved
                            jsonParser.saveJsonFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    try {
                        Thread.sleep(250);  //wait 80*250 mls in total 20 sec in maximum
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!jsonParser.mLoad) {  //if in result json don't save show this toast as excuse
                    String message = "Sorry, but json don't load! Please, check your internet connection and try again!";
                    Message msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = message; // Put the string into Message, into "obj" field.
                    msg.setTarget(mHandler); // Set the Handler
                    msg.sendToTarget(); //Send the message
                   // Toast.makeText(getActivity().getApplicationContext(),
                   //         "Sorry, but json don't load! Please, check your internet connection and try again!",
                   //         Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        //
    }
}
