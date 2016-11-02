package com.makasart.kpirozklad;

import java.util.UUID;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleItems {

    private boolean mIsThereListItem; //List Item or interval

    private UUID mID;  //Personal  id
    private String mTitle;  //Title of Para
    private String mTeacherName;  //teacher name
    private int mWhatIsNow; //Practice, Lection ...
    private int mNumberOfPara;  //Number of "Para"
    private String mLocation;  //Location, building and room

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

    public int getWhatIsNow() {
        return mWhatIsNow;
    }

    public void setWhatIsNow(int whatIsNow) {
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

    public boolean isThereListItem() {
        return mIsThereListItem;
    }

    public void setThereListItem(boolean thereListItem) {
        mIsThereListItem = thereListItem;
    }
}
