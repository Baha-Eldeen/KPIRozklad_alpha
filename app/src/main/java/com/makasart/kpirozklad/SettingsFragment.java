package com.makasart.kpirozklad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Maxim on 24.11.2016.
 */

public class SettingsFragment extends Fragment{
    View mView;
    GroupParser mGroupParser;
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.settings_fragment, container, false);
        RelativeLayout mChooseGroup = (RelativeLayout)mView.findViewById(R.id.choose_group);
        Log.d("IN Settings", "Create");
        TextView txtView = (TextView)mView.findViewById(R.id.display_group_settings);
        txtView.setText(SettingsListActivity.searchWhatGroupNow(getActivity().getApplicationContext()));
        mChooseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("IN Settings", "onClick: ");
                mGroupParser = new GroupParser(getActivity().getApplicationContext());
                mHandler = new mOwnHandler(getActivity().getApplicationContext());
                firstLoadJson();
            }
        });
        return mView;
    }

    private void firstLoadJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                createHandleMessage("Started load json! Please wait to next toast!");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout fm = (FrameLayout) mView.findViewById(R.id.frame_for_loading);
                        fm.setVisibility(View.VISIBLE);
                    }
                });
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_for_loading,
                        new LoadingFragment())
                        .commit();
                mGroupParser.loadJsonFile();


                //wait 40 second in maximum
                for (int i = 0; i < 160; i++) {  //thread where we wait to load json and then saved it
                    if (mGroupParser.mWrongConnection) {
                        createHandleMessage("Sorry, but you have wrong Internet connection! Check and try again!");
                        i = 159; //break the cycle
                        HideLoading();
                        break;
                        //   fm.setVisibility(View.INVISIBLE);
                    }
                    if (mGroupParser.mLoad) {  //check that json loaded
                        createHandleMessage("Json loaded! Please wait to next toast!");
                        try {  //saved json in file if that saved
                            mGroupParser.saveJsonFile();
                            i = 159; //break the cycle

                            Intent mIntent = new Intent(getActivity().getApplicationContext(), SettingsListActivity.class);
                            startActivity(mIntent);
                            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                            HideLoading();
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(250);  //wait 80*250 mls in total 20 sec in maximum
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!mGroupParser.mLoad) {  //if in result json don't save show this toast as excuse
                    try {
                        createHandleMessage("Sorry, but json didn't load! Please, check your internet connection and try again!");
                        HideLoading();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void HideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FrameLayout fm = (FrameLayout) mView.findViewById(R.id.frame_for_loading);
                fm.setVisibility(View.INVISIBLE);
            }
        });
    }

    private static class mOwnHandler extends Handler {
        Context appContext;
        mOwnHandler(Context c) {
            appContext = c;
        }
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(appContext,
                    (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    }

    private void createHandleMessage(String message) {
        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = message; // Put the string into Message, into "obj" field.
        msg.setTarget(mHandler); // Set the Handler
        msg.sendToTarget(); //Send the message
    }
}
