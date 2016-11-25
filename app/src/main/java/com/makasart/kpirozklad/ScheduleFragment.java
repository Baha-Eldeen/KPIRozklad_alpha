package com.makasart.kpirozklad;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxim on 31.10.2016.
 */

public class ScheduleFragment extends ListFragment {
    View mView;

    LinearLayout General;
    TextView mSubjectName, mTeacherName, mIsLection;

    private ArrayList<ScheduleItems> mScheduleItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  //now fragment have options menu
        ScheduleLab sl = new ScheduleLab(getActivity().getApplicationContext());  //get the general list to output on screen
        mScheduleItems = sl.getScheduleItems();
      //  mScheduleItems = ScheduleLab.get(getActivity())
      //          .getScheduleItems();
        setRetainInstance(true);  //saved instance of list fragment
        ScheduleAdapter adapter = new ScheduleAdapter(mScheduleItems);
        setListAdapter(adapter);  //set view adapter
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
            ScheduleItems c = getItem(position);  //get item from position
        //    Log.d("FUCK", Boolean.toString(c.isThereSupport()));
            if (c.isThereSupport()) {  //check if there support then show support block
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_support_schedule, null);
                TextView mDayName = (TextView)convertView.findViewById(R.id.nameOfday);  //set name of day
                mDayName.setText(c.getDayName());
        //        Log.d("FUCK", "Create support");
                return convertView;
            } else {  //if there is not support block then show general block
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_schedule, null);
                General = (LinearLayout) convertView.findViewById(R.id.general_list_layout);  //check and set texture of block from two template
                if (c.isTextureBlock()) {
                    General.setBackgroundResource(R.drawable.rec1);
                } else  {
                    General.setBackgroundResource(R.drawable.rec2);
                }
                //this block is setting current text in blocks
                //set Title and number
                mSubjectName = (TextView) convertView.findViewById(R.id.list_subject_name);
                mSubjectName.setText(Integer.toString(c.getNumberOfPara()) + " " + c.getTitle());
                //set Teacher name
                mTeacherName = (TextView) convertView.findViewById(R.id.list_teacher_name);
                mTeacherName.setText(c.getTeacherName());
                //set type of lesson
                mIsLection = (TextView) convertView.findViewById(R.id.list_is_practice);
                mIsLection.setText(c.getWhatIsNow());
                //set Location of lesson
                TextView mLocation = (TextView) convertView.findViewById(R.id.list_where_is);
                mLocation.setText(c.getLocation());
                //set Time
                TextView mTime = (TextView) convertView.findViewById(R.id.list_time);
                mTime.setText(c.getTime(c.getNumberOfPara()));
            //    Log.d("FUCK", "Create general");
            }
            return convertView;
        }
    }

}
