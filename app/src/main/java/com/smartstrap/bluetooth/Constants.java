package com.smartstrap.bluetooth;

public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNLOST = 6;
    public static final int MESSAGE_CONNFAIL = 7;

    // Key names received from the BluetoothChatService Handler
    public static String DEVICE_NAME = "device_name";
    public static String TOAST = "toast";
}
