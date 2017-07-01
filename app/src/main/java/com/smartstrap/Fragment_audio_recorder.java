package com.smartstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class Fragment_audio_recorder extends Fragment
{

    public Button btnStart, btnStop, btnPlay;
    private static final String TAG = "FragmentQuestion";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_audio_recorder, null);
        btnStart = (Button) view.findViewById(R.id.button1);
        btnStop = (Button) view.findViewById(R.id.button2);
        btnPlay = (Button) view.findViewById(R.id.button3);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        btnStop.setEnabled(false);
        btnPlay.setEnabled(false);

        return view;
    }


    public void start() {
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    public void stop() {
        btnStop.setEnabled(false);
        btnPlay.setEnabled(true);
    }
    public void play() {
        btnStart.setEnabled(true);
        btnStop.setEnabled(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }


}