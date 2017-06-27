package com.smartstrap;

import android.content.Context;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
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
        tv_output.setText(Acivity.getName());
    }

}