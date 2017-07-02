package com.smartstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.smartstrap.bluetooth.BluetoothService;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class FragmentLocation extends Fragment
{
    final static String sendWatchAllOn = "KR";// K 白燈閃爍    R  開震動
    final static String sendWatchAllOff = "JS";//  J  燈關  S關震動
    boolean isFindingWatch = false;
    ImageButton btnFindWatch ;
    private static final String TAG = "FragmentLocation";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, null);
        btnFindWatch = (ImageButton) view.findViewById(R.id.btn_findWtach);
        btnFindWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFindingWatch){
                    isFindingWatch = false;
                }else{
                    isFindingWatch = true;
                }

                if(isFindingWatch){
                    sendMessage(sendWatchAllOn);
                    Toast.makeText(getActivity(), "找尋手錶", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(sendWatchAllOff);
                    Toast.makeText(getActivity(), "關閉找尋手錶", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (HomeActivity.mBlueToothService.getState() != BluetoothService.STATE_CONNECTED) {
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            HomeActivity.mBlueToothService.write(send);
        }
    }
}