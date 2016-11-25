package com.makasart.kpirozklad;

import android.util.Log;

import java.util.UUID;

/**
 * Created by Maxim on 02.11.2016.
 */

public class SettingsItems {

    private UUID mID;  //Personal  id
    private String mGroupName;  //Name of a group
    private int mGroupId; //Id of a group
    private int mOkr; //academic degree
    private int mType;  //type of learning
    private boolean isChecked = false;  //show checked or not

    SettingsItems() {
        mID = UUID.randomUUID();
    }

    public UUID getID() {
        return mID;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public int getOkr() {
        return mOkr;
    }

    public void setOkr(int okr) {
        mOkr = okr;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
