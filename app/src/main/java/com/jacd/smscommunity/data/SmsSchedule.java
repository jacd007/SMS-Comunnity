package com.jacd.smscommunity.data;

import android.provider.BaseColumns;

/**
 * Created by zippyttech on 21/08/17.
 */

public class SmsSchedule {


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    /**
     * Create tables of data base
     */
    public static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " +  Contacts.TABLE_NAME + " (" +
                    Contacts._ID + " INTEGER PRIMARY KEY," +
                    Contacts.COLUMN_NAME_ID + INTEGER_TYPE  +  COMMA_SEP +
                    Contacts.COLUMN_NAME_NAME + TEXT_TYPE  +  COMMA_SEP +
                    Contacts.COLUMN_NAME_NUMBER + TEXT_TYPE  +  COMMA_SEP +
                    Contacts.COLUMN_NAME_MESSAGE + TEXT_TYPE  +  COMMA_SEP +
                    Contacts.COLUMN_NAME_TOPIC + TEXT_TYPE  +  COMMA_SEP +
                    Contacts.COLUMN_NAME_DATE + TEXT_TYPE  +
                    ")";

    public static final String SQL_CREATE_LIST_CONTACTS =
            "CREATE TABLE " +  ListContact.TABLE_NAME + " (" +
                    ListContact._ID + " INTEGER PRIMARY KEY," +
                    ListContact.COLUMN_NAME_ID + INTEGER_TYPE  +  COMMA_SEP +
                    ListContact.COLUMN_NAME_NAME + TEXT_TYPE  +  COMMA_SEP +
                    ListContact.COLUMN_NAME_LIST + TEXT_TYPE  +  COMMA_SEP +
                    ListContact.COLUMN_NAME_PATH + TEXT_TYPE  + COMMA_SEP +
                    ListContact.COLUMN_NAME_TAG + TEXT_TYPE  +
                    ")";

    public static abstract class Contacts implements BaseColumns {

        public static final String TABLE_NAME = "Contacts";

        public static final String COLUMN_NAME_ID= "_idContact";
        public static final String COLUMN_NAME_NAME = "_name";
        public static final String COLUMN_NAME_NUMBER= "_number";
        public static final String COLUMN_NAME_MESSAGE= "_message";
        public static final String COLUMN_NAME_TOPIC= "_topic";
        public static final String COLUMN_NAME_DATE= "_date";


    }

    public static abstract class ListContact implements BaseColumns {

        public static final String TABLE_NAME = "ListContact";

        public static final String COLUMN_NAME_ID= "_idListContact";
        public static final String COLUMN_NAME_LIST = "_list";
        public static final String COLUMN_NAME_NAME= "_nameFile";
        public static final String COLUMN_NAME_TAG= "_tagFile";
        public static final String COLUMN_NAME_PATH= "_path";


    }

}
