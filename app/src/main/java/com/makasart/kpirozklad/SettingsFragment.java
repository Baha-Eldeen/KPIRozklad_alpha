package com.makasart.kpirozklad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxim on 31.10.2016.
 */

public class SettingsFragment extends ListFragment {
    View mView;

    private ArrayList<SettingsItems> mSettingsItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  //now fragment have options menu
        mSettingsItems = SettingsLab.get(getActivity())  //get the general list to output on screen
                .getScheduleItems();
        setRetainInstance(true);  //saved instance of list fragment
        SettingsAdapter adapter = new SettingsAdapter(mSettingsItems);
        setListAdapter(adapter);  //set view adapter
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, parent, savedInstanceState);
        return mView;
    }

    private class SettingsAdapter extends ArrayAdapter<SettingsItems> {
        public SettingsAdapter (ArrayList<SettingsItems> settings) {
            super(getActivity(), 0, settings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SettingsItems c = getItem(position);  //get item from position
            convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_group, null);
            TextView txtView = (TextView)convertView.findViewById(R.id.textViewGroup);
            txtView.setText(c.getGroupName());

            return convertView;
        }
    }

}