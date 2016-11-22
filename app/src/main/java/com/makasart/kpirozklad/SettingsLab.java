package com.makasart.kpirozklad;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Maxim on 02.11.2016.
 */

public class SettingsLab {
    private ArrayList<SettingsItems> mSettingsItems = new ArrayList<SettingsItems>();
    private Context mAppContext;
    private boolean mAlreadyCreate = false;

    private static SettingsLab mSettingsLab;

    private SettingsLab(Context appContext) {
        mAppContext = appContext;
    }

    public static SettingsLab get(Context c) {
        if(mSettingsLab == null) {
            mSettingsLab = new SettingsLab(c.getApplicationContext());
        }
        return mSettingsLab;
    }

    public ArrayList<SettingsItems> getScheduleItems() {
        if (!mAlreadyCreate) {
            GroupParser gp = new GroupParser(mAppContext);
            try {
                JSONArray newJS = gp.readJsonFile();
                gp.someParsing(newJS);
                mSettingsItems = gp.getSettingsItems();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAlreadyCreate = true;
        }
        return mSettingsItems;
    }
}

