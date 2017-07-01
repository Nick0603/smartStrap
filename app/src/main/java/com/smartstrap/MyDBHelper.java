package com.smartstrap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nick on 2017/6/30.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE  TABLE contact " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "name ARCHAR, " +
                "phone VARCHAR, " +
                "level INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
