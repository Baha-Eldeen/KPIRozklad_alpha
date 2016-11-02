package com.makasart.kpirozklad;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxim on 31.10.2016.
 */

public class ScheduleFragment extends ListFragment {
    View mView;

    private ArrayList<ScheduleItems> mScheduleItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  //now fragment have options menu
        mScheduleItems = ScheduleLab.get(getActivity())
                .getScheduleItems();
        setRetainInstance(true);  //saved instance of list fragment
        ScheduleAdapter adapter = new ScheduleAdapter(mScheduleItems);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mView = super.onCreateView(inflater, parent, savedInstanceState);
        return mView;
    }

    private class ScheduleAdapter extends ArrayAdapter<ScheduleItems> {
        public ScheduleAdapter (ArrayList<ScheduleItems> schedule) {
            super(getActivity(), 0, schedule);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_schedule, null);
            }
            ScheduleItems c = getItem(position);
            TextView mSubjectName = (TextView)convertView.findViewById(R.id.list_subject_name);
            mSubjectName.setText(c.getTitle());
            TextView mTeacherName = (TextView)convertView.findViewById(R.id.list_teacher_name);
            mTeacherName.setText(c.getTeacherName());
            TextView mIsLection = (TextView)convertView.findViewById(R.id.list_is_practice);
            //@KOSTIL
            mIsLection.setText("Lection");
            TextView mLocation = (TextView)convertView.findViewById(R.id.list_where_is);
            mLocation.setText(c.getLocation());
            TextView mTime = (TextView)convertView.findViewById(R.id.list_time);
            //@KOSTIL
            mTime.setText("9:30 - 10:45");

            return convertView;
        }
    }

}
