package com.smartstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class FragmentHome extends Fragment
{

    private static final String TAG = "FragmentHome";
    Button btn;
    TextView tv_output;
    HomeActivity Acivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        btn = (Button)view.findViewById(R.id.button);
        tv_output = (TextView)view.findViewById(R.id.textView1);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        Acivity = (HomeActivity)getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}