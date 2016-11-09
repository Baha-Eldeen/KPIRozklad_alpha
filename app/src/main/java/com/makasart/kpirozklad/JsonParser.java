package com.makasart.kpirozklad;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Maxim on 08.11.2016.
 */

public class JsonParser {
    private String jsonString = null;
    private ArrayList<ScheduleItems> mScheduleItems = new ArrayList<ScheduleItems>();
    public boolean mWait = false;

    public ArrayList<ScheduleItems> getScheduleItems() {
        Log.d("UUUI", "getScheduleItems: return arrayList");
        return mScheduleItems;
    }

    public JsonParser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (jsonString == null) {
                        jsonString = readUrl("http://api.rozklad.hub.kpi.ua/groups/497/timetable.json");
                        Log.d("ZZZ", "run: json parse " + jsonString);
                        someParsing(jsonString);
                        mWait = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void someParsing (String jsonString) {
        try {
            JSONObject js_allNew = new JSONObject(jsonString);
            JSONObject js_all = (JSONObject) js_allNew.get("data");
            for (int WEEK = 1; WEEK < 3; WEEK++) {
                JSONObject js_week = (JSONObject) js_all.get(Integer.toString(WEEK)); //pull week
                for (int DAY = 1; DAY < 6; DAY++) {
                    JSONObject js_day = (JSONObject) js_week.get(Integer.toString(DAY)); //pull day
                    int mNumberofPara = 0;
                    for (int LES = 1; LES < 6; LES++) {
                        if (!js_day.isNull(Integer.toString(LES))) {
                            mNumberofPara++;
                            JSONObject js_less = (JSONObject) js_day.get(Integer.toString(LES)); //pull less
                            ScheduleItems object1;
                            object1 = mDiscipline_Name(js_less, LES);
                            object1 = mTeacher_Name(js_less, LES, object1);
                            object1 = mBuilding(js_less, LES, object1);
                            object1.setNumberOfPara(mNumberofPara);
                            if (object1.getTitle() != null) { //if no title then item no add in array list
                                Log.d("UUUI", "someParsing: new item");
                                mScheduleItems.add(object1);
                            }
                        }
                    }
                    mNumberofPara = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static ScheduleItems mBuilding(JSONObject js_less, int LES, ScheduleItems object1) throws JSONException {
        if (!js_less.isNull("rooms")){
            JSONArray js_rooms = (JSONArray) js_less.get("rooms"); //pull rooms
            if (js_rooms.length() > 0) {
                JSONObject js_room_name = (JSONObject) js_rooms.get(0); //pull rooms na
                String room_name = (String) js_room_name.get("name");
                JSONObject js_camp = (JSONObject) js_room_name.get("building");
                String camp_name = (String) js_camp.get("name");
                object1.setLocation(camp_name + "-" + room_name);
            } else {
                object1.setLocation(null);
            }
        } else {
            object1.setLocation(null);
        }
        return object1;
    }

    private static ScheduleItems mTeacher_Name(JSONObject js_less, int LES, ScheduleItems object1) throws JSONException {
        if (!js_less.isNull("teachers")){
                JSONArray js_teachers = (JSONArray) js_less.get("teachers"); //pull teachers
                Log.d("ZZZ", Boolean.toString(js_less.isNull("teachers")));
                Log.d("ZZZ", js_teachers.toString());
                if (js_teachers.length() > 0) {
                    JSONObject js_teacher_sh_name = (JSONObject) js_teachers.get(0); //pull teacher na
                    object1.setTeacherName((String) js_teacher_sh_name.get("short_name"));
                } else {
                    object1.setTeacherName(null);
                }
            } else {
                object1.setTeacherName(null);
            }
        return object1;
    }

    private static ScheduleItems mDiscipline_Name(JSONObject js_less, int LES) throws JSONException {
        ScheduleItems object1 = new ScheduleItems();
        if (!js_less.isNull("discipline")){
                JSONObject js_discipline = (JSONObject) js_less.get("discipline"); //pull discipline
                int be_para = -1;
                if (!js_less.isNull("type")) {
                    be_para = js_less.getInt("type"); //get type
                }
                String bePara;
                switch (be_para) {
                    case 0:
                        bePara = "Лекція";
                        break;
                    case 1:
                        bePara = "Практика";
                        break;
                    case 2:
                        bePara = "Лабораторна";
                        break;
                    default:
                        bePara = "-";
                        break;
                }
                String discipline_name = (String) js_discipline.get("name"); //pull name
                object1.setWhatIsNow(bePara);
                object1.setTitle(discipline_name);
            } else {
                object1.setWhatIsNow(null);
                object1.setTitle(null);
            }
        return object1; //write discipline
    }

    @NonNull
    private static String readUrl(String urlString) throws Exception { //read from url
        BufferedReader reader = null; //initialize READ BUFFER
        try {
            URL url = new URL(urlString); //have url
            reader = new BufferedReader(new InputStreamReader(url.openStream())); //read from Stream
            StringBuffer buffer = new StringBuffer(); //initialize String Buffer
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) //on null pointer stop
                buffer.append(chars, 0, read);

            return buffer.toString(); //return String
        } finally {
            if (reader != null)
                reader.close(); //close reader
        }
    }
}
