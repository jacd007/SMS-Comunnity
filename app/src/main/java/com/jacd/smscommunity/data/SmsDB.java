package com.jacd.smscommunity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacd.smscommunity.common.Utils;
import com.jacd.smscommunity.model.ContactsModel;
import com.jacd.smscommunity.model.ListContactModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 21/08/17.
 */

public class SmsDB {

    private static SQLiteDatabase db;
    private static SmsDBHelper mDbHelper;
    private Context context;
    private static final String TAG = "SmsDB";

    public SmsDB(Context context) {

        mDbHelper = new SmsDBHelper(context);
        this.context = context;
    }

    /**
     * @param tableName  name of table of dataBase to select registers
     * @param projection that specifies which columns from the table of database
     * @param where      Filter results WHERE "qr" = 'My qr'
     * @param args       arguments to where = {"My Caegory"}
     * @return
     */

    public Cursor selectRows(String tableName, String[] projection, String where, String[] args) {

        db = mDbHelper.getReadableDatabase();
/*
        Cursor c = db.query(
                tableName,                                  // The table to query
                projection,                                 // The columns to return
                where,                                      // The columns for the WHERE clause
                args,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );
*/
        String proj = Utils.StringJoiner(", ", projection);
        String argWhere = "";

        if (args.length > 1)
            argWhere = Utils.StringJoiner(", " + args);
        else if (args.length == 1)
            argWhere = args[0];

        String query = "SELECT " + proj + " FROM " + tableName + " " + where + " " + argWhere;

        Cursor c = db.rawQuery(query, null);

        return c;

    }

    /**
     * where and args separated by comma
     *
     * @param tableName
     * @param projection
     * @param where
     * @param args
     * @return
     */

    public Cursor selectRows(String tableName, String projection, String where, String args) {

        db = mDbHelper.getReadableDatabase();
        String query = "SELECT " + projection + " FROM " + tableName + " " + where + " " + args + ";";
        Cursor c = db.rawQuery(query, null);
        return c;

    }

    /**
     * @param tableName name of table of dataBase
     * @param values    values to new register
     */
    public long insertRow(String tableName, ContentValues values) {

        db = mDbHelper.getWritableDatabase();
        long insert = db.insert(tableName, null, values);
        db.close();
        Log.i("DB insert", tableName + ": " + String.valueOf(insert));
        return insert;

    }

    /**
     * Update Tables: where format => id= "01"
     * @param tableName
     * @param value
     * @param where
     * @return
     */

    public long updateRow(String tableName, ContentValues value, String where){

        db = mDbHelper.getWritableDatabase();
        long update= db.update(tableName, value, where, null);
        db.close();
        Log.i("DB update", tableName + ": " + String.valueOf(update));
        return update;
    }

    
    public void deleteAll(){
        db = mDbHelper.getWritableDatabase();
        String deleteQuery="DELETE FROM "+ SmsSchedule.Contacts.TABLE_NAME;
        db.execSQL(deleteQuery);
        deleteQuery="DELETE FROM "+ SmsSchedule.ListContact.TABLE_NAME;
        db.execSQL(deleteQuery);
    }

    public void deleteByName(String TABLE_NAME){
        db = mDbHelper.getWritableDatabase();
        String deleteQuery="DELETE FROM "+ TABLE_NAME;
        db.execSQL(deleteQuery);
    }


    public ContactsModel getitemById(String id){
        db = mDbHelper.getWritableDatabase();
        ContactsModel item=null;
        String table_item= SmsSchedule.Contacts.TABLE_NAME;
        String column_id= SmsSchedule.Contacts.COLUMN_NAME_ID;


        Cursor c = db.rawQuery("SELECT * FROM " +table_item +" WHERE "+column_id+"="+id, null);
        c.moveToFirst();

        if (c.moveToFirst()) {

            do {
                item = new ContactsModel();

                item.setId(c.getInt(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_ID)));
                item.setName(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_NAME)));
                item.setNumber(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_NUMBER)));
                item.setMessage(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_MESSAGE)));
                item.setTopic(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_TOPIC)));
                item.setDate(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_DATE)));

                Log.i(TAG, "getDB " + item.getName());

            } while (c.moveToNext());
        }

        return item;
    }

    public boolean isExistitem(String id){
        db = mDbHelper.getWritableDatabase();
        ContactsModel item=null;
        String table_item= SmsSchedule.Contacts.TABLE_NAME;
        String column_id= SmsSchedule.Contacts.COLUMN_NAME_ID;


        Cursor c = db.rawQuery("SELECT * FROM " +table_item +" WHERE "+column_id+"=\""+id+"\";", null);
        c.moveToFirst();
        boolean exist=false;

        if (c.moveToFirst()) {
            exist=true;
            do {
               exist=true;

            } while (c.moveToNext());
        }

       return exist;
    }

    public List<ContactsModel> getContact() {

        String dato = "";
        try {
            db = mDbHelper.getWritableDatabase();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        List<ContactsModel> Product = new ArrayList<>();

       if(SmsDBHelper.DATABASE_VERSION<1) {
           String[] projection = {
           SmsSchedule.Contacts.COLUMN_NAME_ID,
           SmsSchedule.Contacts.COLUMN_NAME_NAME,
           SmsSchedule.Contacts.COLUMN_NAME_NUMBER,
           SmsSchedule.Contacts.COLUMN_NAME_MESSAGE,
           SmsSchedule.Contacts.COLUMN_NAME_TOPIC,
           SmsSchedule.Contacts.COLUMN_NAME_DATE
           };
       }

        //TODO: HACER UN "WHERE" PARA ENABLED

        String sortOrder = "";
        String arg[] = {"false"};
        Cursor c = db.rawQuery(
                "SELECT * FROM "
                        + SmsSchedule.Contacts.TABLE_NAME
                        +" ORDER BY  LOWER("+ SmsSchedule.Contacts.COLUMN_NAME_NAME +")", null);
        c.moveToFirst();
        int count = c.getCount();

        if (c.moveToFirst()) {
            ContactsModel item;
            do {
                item = new ContactsModel();

                item.setId(c.getInt(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_ID)));
                item.setName(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_NAME)));
                item.setNumber(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_NUMBER)));
                item.setMessage(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_MESSAGE)));
                item.setTopic(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_TOPIC)));
                item.setDate(c.getString(c.getColumnIndex(SmsSchedule.Contacts.COLUMN_NAME_DATE)));

                Log.i(TAG, "getDB " + item.getName());

                Product.add(item);

            } while (c.moveToNext());
        }

        return Product;

    }

    public void setContact(List<ContactsModel> list) {
        try {
            String table_name = SmsSchedule.Contacts.TABLE_NAME;

            String column_id = SmsSchedule.Contacts.COLUMN_NAME_ID;
            String column_name = SmsSchedule.Contacts.COLUMN_NAME_NAME;
            String column_number = SmsSchedule.Contacts.COLUMN_NAME_NUMBER;
            String column_message = SmsSchedule.Contacts.COLUMN_NAME_MESSAGE;
            String column_topic = SmsSchedule.Contacts.COLUMN_NAME_TOPIC;
            String column_date = SmsSchedule.Contacts.COLUMN_NAME_DATE;

            for (ContactsModel vm
                    :
                    list) {
                ContentValues register = new ContentValues();
                register.put(column_id, vm.getId());
                register.put(column_name, vm.getName());
                register.put(column_number, vm.getNumber());
                register.put(column_message, vm.getMessage());
                register.put(column_topic, vm.getTopic());
                register.put(column_date, vm.getDate());

                insertRow(table_name, register);
            }
        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros en db");
            e.printStackTrace();
        }
    }

    public void insertProduct(ContactsModel vm) {
        try {
            String table_name = SmsSchedule.Contacts.TABLE_NAME;

            String column_id = SmsSchedule.Contacts.COLUMN_NAME_ID;
            String column_name = SmsSchedule.Contacts.COLUMN_NAME_NAME;
            String column_number = SmsSchedule.Contacts.COLUMN_NAME_NUMBER;
            String column_message = SmsSchedule.Contacts.COLUMN_NAME_MESSAGE;
            String column_topic = SmsSchedule.Contacts.COLUMN_NAME_TOPIC;
            String column_date = SmsSchedule.Contacts.COLUMN_NAME_DATE;

            ContentValues register = new ContentValues();
            register.put(column_id, vm.getId());
            register.put(column_name, vm.getName());
            register.put(column_number, vm.getNumber());
            register.put(column_message, vm.getMessage());
            register.put(column_topic, vm.getTopic());
            register.put(column_date, vm.getDate());

                insertRow(table_name, register);

        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros en db");
            e.printStackTrace();
        }
    }

    //--------
    public List<ListContactModel> getListContact() {

        String dato = "";
        try {
            db = mDbHelper.getWritableDatabase();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        List<ListContactModel> list = new ArrayList<>();

        String sortOrder = "";
        String arg[] = {"false"};
        Cursor c = db.rawQuery(
                "SELECT * FROM "
                        + SmsSchedule.ListContact.TABLE_NAME
                        +" ORDER BY  LOWER("+ SmsSchedule.ListContact.COLUMN_NAME_NAME +")", null);
        c.moveToFirst();
        int count = c.getCount();

        if (c.moveToFirst()) {
            ListContactModel item;
            do {
                item = new ListContactModel();

                item.setId(c.getInt(c.getColumnIndex(SmsSchedule.ListContact.COLUMN_NAME_ID)));
                item.setName(c.getString(c.getColumnIndex(SmsSchedule.ListContact.COLUMN_NAME_NAME)));
                item.setContactsModelList(getListContact(c.getString(c.getColumnIndex(SmsSchedule.ListContact.COLUMN_NAME_LIST))));
                item.setPath(c.getString(c.getColumnIndex(SmsSchedule.ListContact.COLUMN_NAME_PATH)));
                item.setTag(c.getString(c.getColumnIndex(SmsSchedule.ListContact.COLUMN_NAME_TAG)));

                Log.i(TAG, "getDB " + item.getName());

                list.add(item);

            } while (c.moveToNext());
        }

        return list;

    }

    public void deleteListContactByName(ListContactModel model){
        List<ListContactModel> list = getListContact();
        List<ListContactModel> aux = new ArrayList<>();

        for (ListContactModel lc: list){
            if( !lc.getTag().equals( model.getTag() ) ){
                aux.add(lc);
            }
        }

        deleteAll();
        setListContact(aux);

    }

    public void setListContact(List<ListContactModel> list) {
        try {
            String table_name = SmsSchedule.ListContact.TABLE_NAME;

            String column_id = SmsSchedule.ListContact.COLUMN_NAME_ID;
            String column_name = SmsSchedule.ListContact.COLUMN_NAME_NAME;
            String column_list = SmsSchedule.ListContact.COLUMN_NAME_LIST;
            String column_path = SmsSchedule.ListContact.COLUMN_NAME_PATH;
            String column_tag = SmsSchedule.ListContact.COLUMN_NAME_TAG;

            for (ListContactModel vm
                    :
                    list) {
                ContentValues register = new ContentValues();
                register.put(column_id, vm.getId());
                register.put(column_name, vm.getName());
                register.put(column_list, new Gson().toJson( vm.getContactsModelList() ) );
                register.put(column_path, vm.getPath());
                register.put(column_tag, vm.getTag());

                insertRow(table_name, register);
            }
        } catch (Exception e) {
            Log.e(TAG, "ocurrio un error insertando registros en db");
            e.printStackTrace();
        }
    }

    //------

    private List<ContactsModel> getListContact(String jsonArray){
        List<ContactsModel> yourClassList = new ArrayList<>();

        try {
            Type listType = new TypeToken<ArrayList<ContactsModel>>(){}.getType();
            yourClassList = new Gson().fromJson(jsonArray, listType);
        }catch (Exception e){  e.printStackTrace();  }

        return  yourClassList;
    }

    public int getSizeDB(){
        return getContact().size();
    }

}
