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
    private static final int[] levelColors = {R.color.phoneGreen,R.color.phoneYellow,R.color.phoneRed};

    public static ArrayList<Person> contactsArrayList =  new ArrayList<Person>(); ;
    public static ArrayList<Person> contactsDBArrayList = new ArrayList<Person>();;
    private ContactAdapter adapter;
    private ListView listView;

    private static final String TAG = "FragmentPhone";
    ListView list;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone, null);
        initData();
        initView();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");
    }

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

                contactsDBArrayList.add(person);

//                DBadd(person);
            }
        });
    }



    private void initData(){
        getPhoneBookData();
        getDBPhoneData();
        mergePhoneData();
    }

    public void getPhoneBookData(){
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

    public void  getDBPhoneData(){
//        SQLiteDatabase db = HomeActivity.DB.getReadableDatabase();
//        Cursor cursor = db.query(
//                "contact", null, null, null, null, null, null );

//        while(cursor.moveToNext()){
//            //處理每一筆資料
//            int id = cursor.getInt(cursor.getColumnIndex("_id"));
//            int level = cursor.getInt(cursor.getColumnIndex("level"));
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String phone = cursor.getString(cursor.getColumnIndex("number"));
//            contactsDBArrayList.add(new Person(level,name,phone));
//            Log.d("RECORD", id+"/"+level + " / " + name+" / " + phone );
//        }
    }

    public void mergePhoneData(){

        ArrayList<Person> DBArray = contactsDBArrayList;
        for(int index=0;index<DBArray.size();index++){
            Person DBPerson = DBArray.get(index);

            int pos = IndexOfPerson(contactsArrayList,DBPerson);
            if(pos != -1){
                Person person = contactsArrayList.get(pos);
                person.levelColor = DBPerson.levelColor;
                contactsArrayList.set(pos,person);
            }
        }

    }

    public int IndexOfPerson(ArrayList<Person> ArrayList ,  Person person){
        if (person == null) {
            for (int i = 0; i < ArrayList.size(); i++) {
                if (ArrayList.get(i) == null)
                    return i;
            }
        } else {
            for (int i = 0; i < ArrayList.size(); i++) {
                Person targetPhone = ArrayList.get(i);
                if (person.phone.equals(targetPhone.phone) && person.name.equals(targetPhone.name)){
                    return i;
                }
            }
        }
        return -1;
    }

//    public void DBadd(Person person){
////1
//    }

}