package com.makasart.kpirozklad;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleLab {
    private ArrayList<ScheduleItems> mScheduleItems = new ArrayList<ScheduleItems>();
    private ArrayList<ScheduleItems> mParsedAlready = null;
    private Context mAppContext;
    private int mNumberWhatBlock = 0;
    private boolean mAlreadyCreate = false;
    private Vector<Integer> circleTime = new Vector<>();

    private static ScheduleLab mScheduleLab;

    public void addScheduleItem(ScheduleItems c) {
        mScheduleItems.add(c);
    }

    public void deleteScheduleItem(ScheduleItems c) {
        mScheduleItems.remove(c);
    }

    public ScheduleLab(Context appContext) {
        JsonParser jsPars = new JsonParser(appContext);
        try {
            JSONObject jsOBJ = jsPars.readJsonFile();
            jsPars.someParsing(jsOBJ);
            if (!jsPars.getScheduleItems().isEmpty()) {
                mParsedAlready = jsPars.getScheduleItems();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //in this place i write read json file and serialize this one
        mAppContext = appContext;
    }

 /*   public static ScheduleLab get(Context c) {
        if(mScheduleLab == null) {
            mScheduleLab = new ScheduleLab(c.getApplicationContext());
        }
        return mScheduleLab;
    }   */

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
                        int numberof = 0;
                        for (int j = 0; j < 7; j++) {
                            ScheduleItems c2 = mParsedAlready.get(flag2);
                            if (c2.getDayOfWeek() == mDay && c2.getWeek() == 1) {
                                c2.setTextureBlock(mSetTexture());  //set Texture of 2 variant
                                c2.setThereSupport(false);  //set that block be general block
                                mScheduleItems.add(c2);  //added to list
                                flag2++;
                                numberof++;
                            }
                        }
                        circleTime.add(numberof);
                    }
                }
                mAlreadyCreate = true;  //set flag that list be already created
            }
            setCircle();
            return mScheduleItems;    //return list
        }
        else {
            return null;    //and return null
        }
    }

    private void setCircle() {
        int flag1 = 1;  //flag of general mass
        for(int i = 0; i < circleTime.size(); i++) {
            if(circleTime.get(i) != 0) {
                mScheduleItems.get(flag1).setSetCircle(1);
                if(circleTime.get(i) == 1) {
                    break;
                }
                mScheduleItems.get(flag1+circleTime.get(i)-1).setSetCircle(3);
                flag1 += circleTime.get(i) + 1;
            }
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

