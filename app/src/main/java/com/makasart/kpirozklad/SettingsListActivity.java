package com.makasart.kpirozklad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Maxim on 22.11.2016.
 */

public class SettingsListActivity extends AppCompatActivity {

    private ArrayList<SettingsItems> mSettingsItems;
    private ArrayList<SettingsItems> mSettingsItemses;
    private String Input = "";
    ListView listView;
    EditText editText;
    SettingsAdapters mAdapter;
    private boolean isLogged = false;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_list_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.listview);
        editText = (EditText)findViewById(R.id.txtsearch);
        someNewParse();
        initList();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    //reset listView
                    initList();
                    Input = "";
                }
                else {
                    //perform search
                    searchItem(charSequence.toString());
                }
            }

            public void searchItem(String textToSearch) {
                if (Input.length() > textToSearch.length()) {
                    initList();
                }
                Input = textToSearch;

                for (Iterator<SettingsItems> iterator = mSettingsItems.iterator(); iterator.hasNext(); ) {
                    SettingsItems value = iterator.next();
                    if (!value.getGroupName().toUpperCase().startsWith(textToSearch.toUpperCase())) {
                        iterator.remove();
                    }
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static String searchWhatGroupNow(Context c) {
        String groupName = "";
        //read file with group name
        try {
            InputStream inputStream = c.openFileInput("config.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                groupName = stringBuilder.toString();
                Log.d("SettingsRewriteGroup", "Normal read");
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groupName;
    }

    private void setGroupName(String data,Context context) {
        //set file with group name
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d("SettingsRewriteGroup", "Normal set");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if (id == android.R.id.home) {
            Intent mIntent = new Intent(getApplicationContext(), StartMenuActivity.class);
            startActivity(mIntent);
            overridePendingTransition(R.anim.slide_from_up, R.anim.slide_from_down);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void someNewParse() {
        GroupParser gp = new GroupParser(getApplicationContext());
        try {
            JSONArray newJS = gp.readJsonFile();
            gp.someParsing(newJS);
            mSettingsItemses = gp.getSettingsItems();
            groupName = searchWhatGroupNow(getApplicationContext());
            if (!groupName.equals("")) {
                for (int i = 0; i < mSettingsItemses.size(); i++) {
                    if (mSettingsItemses.get(i).getGroupName().equals(groupName)) {
                        mSettingsItemses.get(i).setChecked(true);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initList() {
        mSettingsItems = new ArrayList<>(mSettingsItemses);
        mAdapter = new SettingsAdapters(mSettingsItems);
        listView.setAdapter(mAdapter);
        if (isLogged) {
            Log.d("PONI", Integer.toString(mSettingsItems.size()));
            Log.d("PONI", "initLIST");
        }
    }

    private class SettingsAdapters extends ArrayAdapter<SettingsItems> {
        public SettingsAdapters (ArrayList<SettingsItems> settings) {
            super(getApplicationContext(), 0, settings);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final SettingsItems c = getItem(position);  //get item from position
            convertView = getLayoutInflater().inflate(R.layout.list_item_group, null);
            TextView txtView = (TextView)convertView.findViewById(R.id.textViewGroup);
            txtView.setText(c.getGroupName());
            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkbox_group);
            if(c.isChecked()) {
                checkBox.setChecked(true);
                Log.d("SettingsRewriteGroup", "checked " + c.getGroupId());
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i = 0; i < mSettingsItemses.size(); i++) {
                        if (mSettingsItemses.get(i).getID().equals(c.getID())) {
                            mSettingsItemses.get(i).setChecked(true);
                            setGroupName(c.getGroupName(), getApplicationContext());
                        } else {
                            mSettingsItemses.get(i).setChecked(false);
                        }
                    }
                    Log.d("SettingsRewriteGroup", "Normal checked");
                    mAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
