package com.makasart.kpirozklad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Maxim on 24.11.2016.
 */

public class SettingsFragment extends Fragment{
    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.settings_fragment, container, false);
        RelativeLayout mChooseGroup = (RelativeLayout)mView.findViewById(R.id.choose_group);
        mChooseGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on this
            }
        });
        return mView;
    }
}
