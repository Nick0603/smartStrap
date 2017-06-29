package com.smartstrap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstrap.bluetooth.BluetoothInfo;
import com.smartstrap.bluetooth.BluetoothService;
import com.smartstrap.bluetooth.DeviceListActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class FragmentSetting extends Fragment {
    private static final String TAG = "FragmentSetting";
    HomeActivity Activity;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private EditText mOutEditText;
    private static Button mSendButton, Btn_connectBT;
    private static TextView TV_connectStatus, TV_deviceName, TV_receiveMsg, TV_reciveTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initElement(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        Activity = (HomeActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("FRAG", "onStart");
        if (!HomeActivity.mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
    }

    void initElement(View view) {
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
        Btn_connectBT = (Button) view.findViewById(R.id.Btn_connectBT);
        TV_receiveMsg = (TextView) view.findViewById(R.id.TV_receiveMsg);
        TV_reciveTime = (TextView) view.findViewById(R.id.TV_receiveTime);
        TV_connectStatus = (TextView) view.findViewById(R.id.TV_connectStatus);
        TV_deviceName = (TextView) view.findViewById(R.id.TV_deviceName);
        updateStatus();

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                String message = mOutEditText.getText().toString();
                sendMessage(message);
                mOutEditText.setText("");
            }
        });

        Btn_connectBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(Activity, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == android.app.Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == android.app.Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
        }
    }


    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (HomeActivity.mBlueToothService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(Activity, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send = message.getBytes();
            HomeActivity.mBlueToothService.write(send);
        }
    }


    /**
     * Establish connection with other device
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

//        HomeActivity.editor.clear();
//        HomeActivity.editor.putBoolean(HomeActivity.SharePreSecure, secure);
//        HomeActivity.editor.putString(HomeActivity.SharePreAddress, address);
//        HomeActivity.editor.commit();

        // Get the BluetoothDevice object
        BluetoothDevice device = HomeActivity.mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        HomeActivity.mBlueToothService.connect(device, secure);
    }

    public void updateStatus(){
        if (HomeActivity.mBlueToothService.getState() == BluetoothService.STATE_CONNECTED) {
            TV_connectStatus.setText(R.string.title_connected);
            TV_deviceName.setText(BluetoothInfo.DEVICE_NAME);
        }else if(HomeActivity.mBlueToothService.getState() == BluetoothService.STATE_CONNECTED){
            TV_connectStatus.setText(R.string.title_connecting);
            TV_deviceName.setText("");
        }else{
            TV_connectStatus.setText(R.string.title_disConnected);
            TV_deviceName.setText("");
        }
    }

    public void updateReadStatus(String readString){
        TV_receiveMsg.setText(readString);

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);

        TV_reciveTime.setText(strDate);
    }
}
