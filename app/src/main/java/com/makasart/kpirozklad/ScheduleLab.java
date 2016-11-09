package com.makasart.kpirozklad;

import android.content.Context;
import android.util.Log;

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
        JsonParser preJson = new JsonParser();
        //////////////////////////////////////////////////fix later
        for ( ; ; ) {
            mParsedAlready = preJson.getScheduleItems();
            if (preJson.mWait)
                break;
        }
        //////////////////////////////////////////////////fix later
        mAppContext = appContext;
    }

    public static ScheduleLab get(Context c) {
        if(mScheduleLab == null) {
            mScheduleLab = new ScheduleLab(c.getApplicationContext());
        }
        return mScheduleLab;
    }

    public ArrayList<ScheduleItems> getScheduleItems() {
        //@KOSTIL
            if(!mAlreadyCreate) {
                int flag2 = -1;
                for (int i = 0; i < 5; i++) {
                    int flag1 = 0;
                    //Creating support blocks
                    ScheduleItems c1 = new ScheduleItems();
                    c1.setThereSupport(true);
                    c1.setDayName(i);
                    Log.d("CR", "+support" + Integer.toString(i));
                    mScheduleItems.add(c1);
                    for (int j = 0; j < 6; j++) {
                        flag2++;
                        ScheduleItems c = mParsedAlready.get(flag2);
                        if (c.getNumberOfPara() < flag1) {  //Stop output elements of day if new day started
                            flag2--;
                            break;
                        }
                        flag1 = c.getNumberOfPara();
                        c.setTextureBlock(mSetTexture());
                        c.setThereSupport(false);
                        Log.d("CR", "+general" + Integer.toString(i));
                        mScheduleItems.add(c);
                    }

                }
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
                mAlreadyCreate = true;
            }
        return mScheduleItems;
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
