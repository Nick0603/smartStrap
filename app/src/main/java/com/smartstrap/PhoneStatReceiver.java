package com.smartstrap;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.smartstrap.bluetooth.BluetoothService;
import com.smartstrap.contact.Person;

import java.util.ArrayList;


public class PhoneStatReceiver extends BroadcastReceiver{


    final static int PhoneCallHigh = 2;
    final static int PhoneCallMiddle  = 1;
    final static int PhoneCallNormal = 0;

    final static String sendWatchPhoneCallHigh = "NR"; //N 紅燈閃爍     R 開震動
    final static String sendWatchPhoneCallMiddle  = "MR"; //M 黃燈閃爍 R 開震動
    final static String sendWatchPhoneCallNormal = "LR"; //L 綠燈閃爍 R 開震動
    final static String sendWatchPhoneCallEnd = "JS";// J 關燈    S  關震動

    String TAG = "tag";
    static String LinePhoneNumber = "";
    TelephonyManager telMgr;
    @Override
    public void onReceive(Context context, Intent intent) {
        telMgr = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        HomeActivity.phoneState = telMgr.getCallState();
        switch (telMgr.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.v(TAG,"number:"+number);
                LinePhoneNumber = number;
//                Toast.makeText(context, "來電為"+number, Toast.LENGTH_SHORT).show();
                //  將來電格式  改為 0000 000 000
                String numberAddSpace = number.substring(0,0+4) + ' ' + number.substring(4,4+3)  + ' '  + number.substring(7,7+3);
//                if (!getPhoneNum(context).contains(number) && !getPhoneNum(context).contains(numberAddSpace)) {
//                    SharedPreferences phonenumSP = context.getSharedPreferences("in_phone_num", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = phonenumSP.edit();
//                    editor.putString(number,number);
//                    editor.commit();
//                    endCall();
//                }

                ArrayList<Person> PersonArrayList = FragmentPhone.contactsDBArrayList ;
                int level = getPersonLevel(PersonArrayList,number);
                if(level == PhoneCallHigh){
                    sendMessage(sendWatchPhoneCallHigh);
                    Toast.makeText(context, "緊急來電", Toast.LENGTH_SHORT).show();
                }else if(level == PhoneCallMiddle){
                    sendMessage(sendWatchPhoneCallMiddle);
                    Toast.makeText(context, "中級來電", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(sendWatchPhoneCallNormal);
                    Toast.makeText(context, "一般來電", Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                sendMessage(sendWatchPhoneCallEnd);
                break;
        }

    }
    /**
     * 掛斷電話
     */
//    private void endCall()
//    {
//        Class<TelephonyManager> c = TelephonyManager.class;
//        try
//        {
//            Method getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[]) null);
//            getITelephonyMethod.setAccessible(true);
//            ITelephony iTelephony = null;
//            Log.e(TAG, "End call.");
//            iTelephony = (ITelephony ) getITelephonyMethod.invoke(telMgr, (Object[]) null);
//            iTelephony.endCall();
//        }
//        catch (Exception e)
//        {
//            Log.e(TAG, "Fail to answer ring call.", e);
//        }
//    }

    private ArrayList<String>  getPhoneNum(Context context) {
        ArrayList<String> numList = new ArrayList<String>();
        //得到ContentResolver對象
        ContentResolver cr = context.getContentResolver();
        //取得電話本中開始一項的光標
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            // 取得聯系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

            // 取得電話號碼(可能存在多個號碼)
            while (phone.moveToNext())
            {
                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                numList.add(strPhoneNumber);
                Log.v("tag","strPhoneNumber:"+strPhoneNumber);
            }

            phone.close();
        }
        cursor.close();
        return numList;
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

    public int getPersonLevel(ArrayList<Person> arrayList, String number){
        for(int i=0;i<arrayList.size();i++){
            Person targetPerson = arrayList.get(i);
            String targetNumber = targetPerson.phone;

            // 去除空白 例: 0900 000 000
            if(number.length() > 10){
                number = number.replaceAll("\\s+", "");
            }
            // 去除開頭為886   例: +886900000000
            if(number.length() > 10){
                number = '0' + number.substring(4,9);
            }

            if(targetNumber.equals(number)){
                Log.i(TAG,targetPerson.name);
                return targetPerson.levelColor;
            }
        }
        return 0;
    }
}