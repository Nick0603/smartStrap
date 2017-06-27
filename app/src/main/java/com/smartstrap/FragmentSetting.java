package com.smartstrap;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstrap.bluetooth.BluetoothService;
import com.smartstrap.bluetooth.Constants;
import com.smartstrap.bluetooth.DeviceListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        HomeActivity.mBlueToothService.mHandler = mHandler;
    }

    void initElement(View view) {
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
        Btn_connectBT = (Button) view.findViewById(R.id.Btn_connectBT);
        TV_receiveMsg = (TextView) view.findViewById(R.id.TV_receiveMsg);
        TV_reciveTime = (TextView) view.findViewById(R.id.TV_receiveTime);
        TV_connectStatus = (TextView) view.findViewById(R.id.TV_connectStatus);
        TV_deviceName = (TextView) view.findViewById(R.id.TV_deviceName);
        if (HomeActivity.mBlueToothService.getState() == BluetoothService.STATE_CONNECTED) {
            TV_connectStatus.setText("已連線");
            TV_deviceName.setText(HomeActivity.mConnectedDeviceName);
        } else {
            TV_connectStatus.setText("未連線");
            TV_deviceName.setText("");
        }

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            TV_connectStatus.setText(R.string.title_connected);
                            TV_deviceName.setText(HomeActivity.mConnectedDeviceName);
                            Toast.makeText(Activity, "與" + HomeActivity.mConnectedDeviceName + "裝置連線成功", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            TV_connectStatus.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            TV_connectStatus.setText(R.string.title_disConnected);
                            TV_deviceName.setText("");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:


                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer

                    String readMessage = new String(readBuf, 0 , msg.arg1);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    TV_receiveMsg.setText(readMessage);
                    TV_reciveTime.setText(formattedDate);

                    if(readMessage.equals(HomeActivity.AlertACondition) && HomeActivity.isAlertDialog == false && System.currentTimeMillis() - HomeActivity.lastAlertTime > HomeActivity.alertDelayTime){
                        HomeActivity.alertStart();
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.alertATitle)
                                .setMessage(R.string.alertAContent)
                                .setPositiveButton(R.string.alertAPositiveBtn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
//                                                                              startActivity(new Intent(getApplicationContext(),CPRActivity.class));
                                        Toast.makeText(Activity, "正向按鈕", Toast.LENGTH_SHORT).show();
                                        HomeActivity.alertStop();
                                    }
                                })
                                .setNegativeButton(R.string.alertANegativeBtn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HomeActivity.alertStop();
                                    }
                                })
                                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        HomeActivity.alertStop();
                                    }
                                })
                                .show();
                    }else if(readMessage.equals(HomeActivity.AlertBCondition) && HomeActivity.isAlertDialog == false && System.currentTimeMillis() - HomeActivity.lastAlertTime > HomeActivity.alertDelayTime){
                        HomeActivity.alertStart();
                        new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.alertBTitle)
                                .setMessage(R.string.alertBContent)
                                .setPositiveButton(R.string.alertBPositiveBtn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HomeActivity.alertStop();
                                    }
                                })

                                .setOnCancelListener(new DialogInterface.OnCancelListener(){
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        HomeActivity.alertStop();
                                    }
                                })
                                .show();
                    }


                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    HomeActivity.mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    break;
                case Constants.MESSAGE_CONNLOST:
                    HomeActivity.alertStart();
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.alertBTDisConnTitle)
                            .setMessage(R.string.alertBTDisConnContent)
                            .setPositiveButton(R.string.alertBTPositiveBtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                //                                    getActivity().finish();
                //                                    startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                                    Toast.makeText(Activity, "正面按鈕", Toast.LENGTH_SHORT).show();
                                    HomeActivity.alertStop();
                                }
                            })
                            .setNegativeButton(R.string.alertBTNegativeBtn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HomeActivity.alertStop();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener(){
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    HomeActivity.alertStop();
                                }
                            })
                            .show();
                    break;
                case Constants.MESSAGE_CONNFAIL:
                    String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(Activity,"與" + deviceName + "裝置連線失敗", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    String strToast = msg.getData().getString(Constants.TOAST);
                    Toast.makeText(Activity,strToast, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
