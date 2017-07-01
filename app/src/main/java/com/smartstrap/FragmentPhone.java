package com.smartstrap;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.smartstrap.contact.ContactAdapter;
import com.smartstrap.contact.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carson_Ho on 16/5/23.
 */
public class FragmentPhone extends Fragment
{
    private static final int REQUEST_CONTACTS = 1;
    private static final int[] levelColors = {R.color.phoneGreen,R.color.phoneOrange,R.color.phoneRed};

    private ArrayList<Person> contactsArrayList;
    private ContactAdapter adapter;
    private ListView listView;

    private static final String TAG = "FragmentPhone";
    ListView list;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone, null);

//        int permission = ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.READ_CONTACTS);
//        if (permission != PackageManager.PERMISSION_GRANTED ||
//
//                ActivityCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
//
//                ActivityCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            //未取得權限，向使用者要求允許權限
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{READ_CONTACTS, WRITE_CONTACTS, CALL_PHONE},
//                    REQUEST_CONTACTS);
//        } else {
//            //已有權限，可進行檔案存取
////            readContacts();
//        }

        initData();
        initView();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ArrayList<String> contactsContractList = new ArrayList<String>();
//        //得到ContentResolver對象
//        ContentResolver cr = HomeActivity.contextOfApplication.getContentResolver();
//        //取得電話本中開始一項的光標
//        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        while (cursor.moveToNext())
//        {
//            // 取得聯系人ID
//            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//
//            // 取得電話號碼(可能存在多個號碼)
//            while (phone.moveToNext())
//            {
//                String _ID = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//                String displyName = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String strPhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                contactsContractList.add(displyName  + "\n" + strPhoneNumber);
//                Log.v("tag","ID:"+_ID + " ,Name:" + displyName + ",PhoneNumber:" + strPhoneNumber);
//            }
//
//            phone.close();
//
//        }

        Log.d(TAG, "onActivityCreated");
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        ContentResolver resolver  = HomeActivity.contextOfApplication.getContentResolver();
//        Cursor cursor = resolver.query(
//                ContactsContract.Contacts.CONTENT_URI,
//                null,
//                null,
//                null,
//                null );
//        while(cursor.moveToNext()){
//            //處理每一筆資料
//            int id = cursor.getInt(cursor.getColumnIndex(
//                    ContactsContract.Contacts._ID));
//            String name = cursor.getString(cursor.getColumnIndex(
//                    ContactsContract.Contacts.DISPLAY_NAME));
//            Log.d("RECORD", id+"/"+name );
//        }
//
//        list = (ListView)view.findViewById(R.id.list);
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
//                getContext(),
//                android.R.layout.simple_list_item_1,
//                cursor,
//                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
//                new int[] {android.R.id.text1},
//                1);
//        list.setAdapter(adapter);

//
//        cursor.close();
//        Toast.makeText(getActivity(), "完成聯絡人收尋", Toast.LENGTH_SHORT).show();
//    }

    private void initView() {

        adapter = new ContactAdapter(getActivity(), R.layout.item_contact, contactsArrayList);

        listView = (ListView)view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Person person = contactsArrayList.get(position);
                ImageView levelColor = (ImageView)view.findViewById(R.id.item_levelColor);
                person.levelColor++;
                if (person.levelColor == levelColors.length) {
                    person.levelColor = 0;
                }
                contactsArrayList.set(position, person);
                levelColor.setImageResource(levelColors[person.levelColor]);
            }
        });
    }

    private void initData(){
        getPhoneBookData();
    }

    public void getPhoneBookData(){
        contactsArrayList = new ArrayList<Person>();
        Cursor contacts_name = HomeActivity.contextOfApplication.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (contacts_name.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            String phoneNumber = "";
            long id = contacts_name.getLong(
                    contacts_name.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor contacts_number = HomeActivity.contextOfApplication.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + "=" + Long.toString(id),
                    null,
                    null);

            while (contacts_number.moveToNext()) {
                phoneNumber = contacts_number
                        .getString(contacts_number.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            contacts_number.close();
            String name = contacts_name.getString(contacts_name
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            contactsArrayList.add(new Person(0,name,phoneNumber));
        }
    }
}