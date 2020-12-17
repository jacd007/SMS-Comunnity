package com.jacd.smscommunity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zippyttech on 21/08/17.
 */

public class SmsDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Sms.db";
    public Context context;


    public SmsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SmsSchedule.SQL_CREATE_CONTACTS);
        sqLiteDatabase.execSQL(SmsSchedule.SQL_CREATE_LIST_CONTACTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int i1) {

    }
}
