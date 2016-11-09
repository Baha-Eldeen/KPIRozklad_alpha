package com.makasart.kpirozklad;

import android.util.Log;

import java.util.UUID;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleItems {

    private UUID mID;  //Personal  id
    private String mTitle;  //Title of Para
    private String mTeacherName;  //teacher name
    private String mWhatIsNow; //Practice, Lecture ...
    private int mNumberOfPara;  //Number of "Para"
    private String mLocation;  //Location, building and room
    private boolean mTextureBlock;  //What is a texture now
    private boolean mIsThereSupport;  //This is Schedule or support block
    private String mDayName;  //If there is support block set it name "Monday, Thursday"

    public ScheduleItems() {
        mID = UUID.randomUUID();
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTeacherName() {
        return mTeacherName;
    }

    public void setTeacherName(String teacherName) {
        mTeacherName = teacherName;
    }

    public String getWhatIsNow() {
        return mWhatIsNow;
    }

    public void setWhatIsNow(String whatIsNow) {
        mWhatIsNow = whatIsNow;
    }

    public int getNumberOfPara() {
        return mNumberOfPara;
    }

    public void setNumberOfPara(int numberOfPara) {
        mNumberOfPara = numberOfPara;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public boolean isTextureBlock() {
        return mTextureBlock;
    }

    public void setTextureBlock(boolean textureBlock) {
        mTextureBlock = textureBlock;
    }

    public boolean isThereSupport() {
        return mIsThereSupport;
    }

    public void setThereSupport(boolean thereSupport) {
        mIsThereSupport = thereSupport;
        Log.d("CR", Boolean.toString(mIsThereSupport));
    }

    public String getDayName() {
        return mDayName;
    }

    public String getTime(int WhatPara) {
        switch (WhatPara) {
            case 1:
                return "8:30 - 10:05";
            case 2:
                return "10:25 - 12:00";
            case 3:
                return "12:20 - 13:55";
            case 4:
                return "14:15 - 15:50";
            case 5:
                return "16:10 - 17:45";
            default:
                return "-";
        }
    }

    public void setDayName(int dayName) {
        switch (dayName) {
            case 0:
                mDayName = "MONDAY";
                break;
            case 1:
                mDayName = "TUESDAY";
                break;
            case 2:
                mDayName = "WEDNESDAY";
                break;
            case 3:
                mDayName = "THURSDAY";
                break;
            case 4:
                mDayName = "FRIDAY";
                break;
            default:
                mDayName = "UNKNOWN";
                break;
        }
        Log.d("CR", mDayName);
    }
}
