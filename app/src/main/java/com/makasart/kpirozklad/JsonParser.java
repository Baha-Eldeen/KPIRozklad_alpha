package com.makasart.kpirozklad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.text.RuleBasedCollator;
import java.util.ArrayList;

/**
 * Created by Maxim on 08.11.2016.
 */

public class JsonParser {
    private String jsonString = null;   //in this line json saved
    private ArrayList<ScheduleItems> mScheduleItems = new ArrayList<ScheduleItems>();   //pre-json list
    public static final String mFileName = "kpi_ip_63";  //name of group (in future be dynamic)
    public boolean mLoad = false;  //load json flag
    public boolean mSave = false;  //save json flag
    private Context appContext;  //app context need to save json file in working directory

    public ArrayList<ScheduleItems> getScheduleItems() {  //return serialized array list (pre-json)
        return mScheduleItems;
    }

//this is function to read Json File from local directory
    public JSONObject readJsonFile() throws IOException, JSONException {
        BufferedReader reader = null;  //buffered reader need to read information
        JSONObject jsObj = null;  //returned json object
        try {
            InputStream input = appContext.openFileInput(mFileName);  //open file in working directory
            reader = new BufferedReader(new InputStreamReader(input));  //input stream if json file
            StringBuilder mjsonString = new StringBuilder();  //string builder need to build string line from json file
            String line = null;  //supporting to read line
            while ((line = reader.readLine()) != null) {
                mjsonString.append(line);  //while not reach to last symbol read
            }
            jsObj = new JSONObject(mjsonString.toString());  //parsed string to json
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {    //closed input streams
                reader.close();
            }
        }
        return jsObj;   //returned final parsed object
    }

    //this function need to save json after read from url
    public void saveJsonFile() throws JSONException, IOException {
        Writer writer = null;   //init writer
        try {
            //open private file in working directory
            OutputStream out = appContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonString);
        } finally {
            if (writer != null) {    //closed output streams
                writer.close();
            }
            mSave = true;  //to report that json saved
        }
    }

    //this function need to download json from url
    public void loadJsonFile() {
        new Thread(new Runnable() {  //init stream
            @Override
            public void run() {
                try {
                    jsonString = readUrl("http://api.rozklad.hub.kpi.ua/groups/497/timetable.json");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mLoad = true;  //to report that json load
            }
        }).start();   //started already

    }

    //constructor have a context of activity to say where is working directory
    public JsonParser(Context aContext) {
        appContext = aContext;
    }

    //this is general function to parse the json file to normal string line
    public void someParsing (JSONObject jsonObj) {
        try {
          //  JSONObject js_allNew = new JSONObject(jsonString);  //parse string line to json object
            JSONObject js_all = (JSONObject) jsonObj.get("data");  //get json obj from "data"
            for (int WEEK = 1; WEEK < 3; WEEK++) {  //wee for default have 2 weeks
                JSONObject js_week = (JSONObject) js_all.get(Integer.toString(WEEK)); //pull week
                for (int DAY = 1; DAY < 6; DAY++) {  //wee must read every day
                    JSONObject js_day = (JSONObject) js_week.get(Integer.toString(DAY)); //pull day
                    for (int LES = 1; LES < 6; LES++) {  //we must read every lesson
                        if (!js_day.isNull(Integer.toString(LES))) {  //check that day object have lessons
                           // Log.d("NOP", Integer.toString(mNumberofPara));
                            JSONObject js_less = (JSONObject) js_day.get(Integer.toString(LES)); //pull less
                            ScheduleItems object1;  //init object that int future be putted in array list
                            object1 = mDiscipline_Name(js_less, LES);  //put discipline name
                            object1 = mTeacher_Name(js_less, LES, object1);  //put teacher name
                            object1 = mBuilding(js_less, LES, object1);  //put name of building
                            object1.setNumberOfPara(LES);  //set number of para
                            object1.setDayOfWeek(DAY);  //set number of day
                            object1.setWeek(WEEK);  //set number of week
                            if (object1.getTitle() != null) { //if no title then item no add in array list
                              //  Log.d("UUUI", "someParsing: new item");
                                mScheduleItems.add(object1);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static ScheduleItems mBuilding(JSONObject js_less, int LES, ScheduleItems object1) throws JSONException {
        if (!js_less.isNull("rooms")){  //check that lessons obkect have rooms
            JSONArray js_rooms = (JSONArray) js_less.get("rooms"); //pull rooms
            if (js_rooms.length() > 0) {
                JSONObject js_room_name = (JSONObject) js_rooms.get(0); //pull rooms na
                String room_name = (String) js_room_name.get("name"); //get room name(number of room)
                JSONObject js_camp = (JSONObject) js_room_name.get("building"); //get number of building (1)
                String camp_name = (String) js_camp.get("name");  //get number of building (2)
                object1.setLocation(camp_name + "-" + room_name); //set location to object
            } else {
                object1.setLocation(null);
            }
        } else {
            object1.setLocation(null);
        }
        return object1;
    }

    private static ScheduleItems mTeacher_Name(JSONObject js_less, int LES, ScheduleItems object1) throws JSONException {
        if (!js_less.isNull("teachers")){  //check that lessons object have teachers
                JSONArray js_teachers = (JSONArray) js_less.get("teachers"); //pull teachers
                if (js_teachers.length() > 0) {
                    JSONObject js_teacher_sh_name = (JSONObject) js_teachers.get(0); //pull teacher na
                    object1.setTeacherName((String) js_teacher_sh_name.get("short_name"));  //set teachers name
                } else {
                    object1.setTeacherName(null);
                }
            } else {
                object1.setTeacherName(null);
            }
        return object1;
    }

    private static ScheduleItems mDiscipline_Name(JSONObject js_less, int LES) throws JSONException {
        ScheduleItems object1 = new ScheduleItems();  //first init
        if (!js_less.isNull("discipline")){  //check that lessons object have discipline
                JSONObject js_discipline = (JSONObject) js_less.get("discipline"); //pull discipline
                int be_para = -1;
                if (!js_less.isNull("type")) {   //check that less have type
                    be_para = js_less.getInt("type"); //get type
                }
                String bePara;
                switch (be_para) {
                    case 0:
                        bePara = "Лекція";
                        break;
                    case 1:
                        bePara = "Практ.";
                        break;
                    case 2:
                        bePara = "Лабор.";
                        break;
                    default:
                        bePara = "-";
                        break;
                }
                String discipline_name = (String) js_discipline.get("name"); //pull name
                object1.setWhatIsNow(bePara);  //set Lecture or ...
                object1.setTitle(discipline_name);  //set Title
            } else {
            //set null if have not anything
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
