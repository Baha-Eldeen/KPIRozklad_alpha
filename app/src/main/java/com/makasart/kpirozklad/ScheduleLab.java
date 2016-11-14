package com.makasart.kpirozklad;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleLab {
    private ArrayList<ScheduleItems> mScheduleItems = new ArrayList<ScheduleItems>();
    private ArrayList<ScheduleItems> mParsedAlready;
    private Context mAppContext;
    private int mNumberWhatBlock = 0;
    private boolean mAlreadyCreate = false;

    private static ScheduleLab mScheduleLab;

    public void addScheduleItem(ScheduleItems c) {
        mScheduleItems.add(c);
    }

    public void deleteScheduleItem(ScheduleItems c) {
        mScheduleItems.remove(c);
    }

    private ScheduleLab(Context appContext) {
        JsonParser jsPars = new JsonParser(appContext);
        try {
            JSONObject jsOBJ = jsPars.readJsonFile();
            jsPars.someParsing(jsOBJ);
            mParsedAlready = jsPars.getScheduleItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //in this place i write read json file and serialize this one
        mAppContext = appContext;
    }

    public static ScheduleLab get(Context c) {
        if(mScheduleLab == null) {
            mScheduleLab = new ScheduleLab(c.getApplicationContext());
        }
        return mScheduleLab;
    }

    public ArrayList<ScheduleItems> getScheduleItems() {
        if (!mParsedAlready.isEmpty()) {  //if parsed already list is empty then show excuse toast
            if (!mAlreadyCreate) {  //flag to don't create list again
                int flag2 = 0;
                int mWeek = 0, mDay = 0;
                for (int i = 0; i < 7; i++) {
                    ScheduleItems c = mParsedAlready.get(flag2);
                    //////
                    //CUSTOM SET WEEK = 1. REPLACE THIS TO DO SCHEDULE FOR A WEEK
                    //////
                    if (c.getWeek() == 1) {
                        if (c.getDayOfWeek() > mDay) {
                            mDay = c.getDayOfWeek();
                            //then create support block
                            ScheduleItems c1 = new ScheduleItems();  //create new Item
                            c1.setThereSupport(true);  //set that it's be a support block
                            c1.setDayName(mDay - 1);  //set day name
                            //   Log.d("CR", "+support" + Integer.toString(i));
                            mScheduleItems.add(c1);  //added to a list
                        }
                        for (int j = 0; j < 7; j++) {
                            ScheduleItems c2 = mParsedAlready.get(flag2);
                            if (c2.getDayOfWeek() == mDay && c2.getWeek() == 1) {
                                c2.setTextureBlock(mSetTexture());  //set Texture of 2 variant
                                c2.setThereSupport(false);  //set that block be general block
                                mScheduleItems.add(c2);  //added to list
                                flag2++;
                            }
                        }
                    }
                }
          /*      int flag2 = -1;  //flag that output only new item's in a list
                for (int i = 0; i < 7; i++) {  //output five day's
                    int flag1 = 0;  //flag that show a new day
                    //Creating support blocks
                    ScheduleItems c1 = new ScheduleItems();  //create new Item
                    c1.setThereSupport(true);  //set that it's be a support block
                    c1.setDayName(i);  //set day name
                 //   Log.d("CR", "+support" + Integer.toString(i));
                    mScheduleItems.add(c1);  //added to a list
                    for (int j = 0; j < 6; j++) {  //be maximum 6 lessons
                        flag2++;  //increase flag of number of items
                        ScheduleItems c = mParsedAlready.get(flag2);  //getted from pre-parsed list
                        if (c.getNumberOfPara() < flag1) {  //Stop output elements of day if new day started
                            flag2--;  //if not justify return
                            break;
                        }
                        flag1 = c.getNumberOfPara();  //get number of lessons
                        c.setTextureBlock(mSetTexture());  //set Texture of 2 variant
                        c.setThereSupport(false);  //set that block be general block
                   //     Log.d("CR", "+general" + Integer.toString(i));
                        mScheduleItems.add(c);  //added to list
                    }
                }  */
             /*   int flag1 = -1, flag2 = -1;
                for (int i = 0; i < 30; i++) {
                    ScheduleItems c = new ScheduleItems();
                    flag1++;
                    if (flag1 == 6) {
                        flag1 = 0;
                    }
                    if (flag1 == 0) {
                        flag2++;
                        Log.d("CR", "+support" + Integer.toString(i));
                        c.setThereSupport(true);
                        c.setDayName(flag2);
                    } else {
                        Log.d("CR", "+general" + Integer.toString(i));
                        c.setThereSupport(false);
                        c.setTitle("Subject name " + Integer.toString(i));
                        c.setTeacherName("Teacher name " + Integer.toString(i));
                        c.setLocation("301-18");
                        c.setTextureBlock(mSetTexture());
                    }
                    mScheduleItems.add(c);
                } */
                mAlreadyCreate = true;  //set flag that list be already created
            }
            return mScheduleItems;    //return list
        }
        else {
            //In this place i must do a report, that json loaded have some warnings

            //if we don't have a list make excuse toast
         //   Toast.makeText(mAppContext,
         //           "Sorry, but json don't loaded! Please check your internet connection and try again!",
         //           Toast.LENGTH_SHORT).show();
            return null;    //and return null
        }
    }

    public ScheduleItems getScheduleItem(UUID id) {
        for (ScheduleItems c : mScheduleItems) {
            if (c.getID().equals(id))
                return c;
        }
        return null;
    }

    private boolean mSetTexture() {
        mNumberWhatBlock++;
        //if multiply 2, then use second texture
        if (mNumberWhatBlock % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }
}
