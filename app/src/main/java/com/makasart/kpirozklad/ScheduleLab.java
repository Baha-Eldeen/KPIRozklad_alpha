package com.makasart.kpirozklad;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleLab {
    private ArrayList<ScheduleItems> mScheduleItems = new ArrayList<ScheduleItems>();
    private Context mAppContext;

    //WARNING. Check whether the necessary statics method
    private static ScheduleLab mScheduleLab;

    public void addScheduleItem(ScheduleItems c) {
        mScheduleItems.add(c);
    }

    public void deleteScheduleItem(ScheduleItems c) {
        mScheduleItems.remove(c);
    }

    private ScheduleLab(Context appContext) {
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
            for (int i = 0; i < 20; i++) {
                ScheduleItems c = new ScheduleItems();
                c.setTitle("Subject name "+Integer.toString(i));
                c.setTeacherName("Teacher name "+Integer.toString(i));
                c.setLocation("301-18 "+Integer.toString(i));
                mScheduleItems.add(c);
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
}
