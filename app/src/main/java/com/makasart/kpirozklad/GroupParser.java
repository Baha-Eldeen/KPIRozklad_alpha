package com.makasart.kpirozklad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.Pack200;

/**
 * Created by Maxim on 08.11.2016.
 */

public class GroupParser {
    private String jsonString = null;   //in this line json saved
    private ArrayList<SettingsItems> mSettingsItems = new ArrayList<SettingsItems>();   //pre-json list
    private JSONArray jsResultArray = new JSONArray();
    public String nextLink = "http://api.rozklad.hub.kpi.ua/groups/?limit=100";
    public static final String mFileName = "kpi_groups";  //name of group (in future be dynamic)
    public boolean mLoad = false;  //load json flag
    public boolean mSave = false;  //save json flag
    public boolean mWrongConnection = false;  //status of connection
    private Context appContext;  //app context need to save json file in working directory

    public ArrayList<SettingsItems> getSettingsItems() {  //return serialized array list (pre-json)
        return mSettingsItems;
    }

//this is function to read Json File from local directory
    public JSONArray readJsonFile() throws IOException, JSONException {
        BufferedReader reader = null;  //buffered reader need to read information
        JSONArray jsObj = null;  //returned json object
        try {
            Log.d("URLA", "Read json file");
            InputStream input = appContext.openFileInput(mFileName);  //open file in working directory
            reader = new BufferedReader(new InputStreamReader(input));  //input stream if json file
            StringBuilder mjsonString = new StringBuilder();  //string builder need to build string line from json file
            String line = null;  //supporting to read line
            while ((line = reader.readLine()) != null) {
                mjsonString.append(line);  //while not reach to last symbol read
            }
            jsObj = new JSONArray(mjsonString.toString());  //parsed string to json
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
            Log.d("URLA", "Save json file");
            //open private file in working directory
            OutputStream out = appContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsResultArray.toString());
            mSave = true;  //to report that json saved
        } catch (Exception e) {
            mSave = false;
        }
        finally {
            if (writer != null) {    //closed output streams
                writer.close();
            }
        }
    }

    //this function need to download json from url
    public void loadJsonFile() {
        new Thread(new Runnable() {  //init stream
            @Override
            public void run() {
                try {
                    jsonString = readUrl(nextLink);
                    jsResultArray.put(jsonString);
                    Log.d("EBAT!!!", jsonString);
                    Log.d("JS", jsonString);
                    try {
                        JSONObject jstest = new JSONObject(jsonString);
                       // JSONArray jstest2 = new JSONArray(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("EBAT!!!", "slovilo!");
                        jsonString = null;
                        mLoad = false;
                        mWrongConnection = true;
                    }
                    if (jsonString.charAt(0) != '{') {
                        Log.d("EBAT!!!", "Ebat kostil!");
                        mLoad = false;
                        mWrongConnection = true;
                    }
                    if (jsonString != null) {
                     //   mLoad = true;   //to report that json load
                        Log.d("URLA", "Load json file");
                    } else {
                        mLoad = false;
                        mWrongConnection = true;
                    }
                    Log.d("JS", jsonString);
                    JSONObject jsNext = new JSONObject(jsonString);
                    if (!jsNext.isNull("next") && jsonString != null) {
                        nextLink = (String) jsNext.get("next");
                    }
                    if (jsNext.isNull("next")) {
                        nextLink = null;
                        mLoad = true;
                    }
                    if (!mWrongConnection && nextLink != null) {
                        loadJsonFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mLoad = false;  //to report that json don't load
                }
            }
        }).start();   //started already
    }

    //constructor have a context of activity to say where is working directory
    public GroupParser(Context aContext) {
        appContext = aContext;
    }

    //this is general function to parse the json file to normal string line
    public void someParsing (JSONArray jsArr) {
        try {
            for (int i = 0; i < jsArr.length(); i++) {
                Log.d("MUST", jsArr.get(i).toString());
            }
            for (int i = 0; i < jsArr.length(); i++) {
                Log.d("IIIEE", jsArr.toString());
                JSONObject jsAll = new JSONObject(jsArr.get(i).toString());
                if (!jsAll.isNull("results")) {
                    JSONArray jsArrayResult = (JSONArray) jsAll.get("results");
                    for (int j = 0; j < jsArrayResult.length(); j++) {
                        SettingsItems mNewSI = new SettingsItems();
                        JSONObject SubAll = (JSONObject) jsArrayResult.get(j);
                        if (!SubAll.isNull("id")) {
                            Log.d("FFII", SubAll.get("id").toString());
                            mNewSI.setGroupId((Integer) SubAll.get("id"));
                        }
                        if (!SubAll.isNull("name")) {
                            Log.d("FFII", SubAll.get("name").toString());
                            mNewSI.setGroupName((String) SubAll.get("name"));
                        }
                        if (!SubAll.isNull("okr")) {
                            Log.d("FFII", SubAll.get("okr").toString());
                            mNewSI.setOkr((Integer) SubAll.get("okr"));
                        }
                        if (!SubAll.isNull("type")) {
                            Log.d("FFII", SubAll.get("type").toString());
                            mNewSI.setType((Integer) SubAll.get("type"));
                        }
                        mSettingsItems.add(mNewSI);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private static String readUrl(String urlString) throws Exception { //read from url
        BufferedReader reader = null; //initialize READ BUFFER
        try {
            URL url = new URL(urlString); //have url
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                Log.d("URLA", Integer.toString(code));
                reader = new BufferedReader(new InputStreamReader(url.openStream())); //read from Stream
                StringBuffer buffer = new StringBuffer(); //initialize String Buffer
                int read;
                char[] chars = new char[1024];
                while ((read = reader.read(chars)) != -1) //on null pointer stop
                    buffer.append(chars, 0, read);

                return buffer.toString(); //return String
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("URLA", e.toString());
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                reader.close(); //close reader
        }
    }
}
