package com.jacd.smscommunity.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jacd.smscommunity.R;
import com.jacd.smscommunity.adapter.ListCAdapter;
import com.jacd.smscommunity.adapter.ListContactsAdapter;
import com.jacd.smscommunity.common.Items;
import com.jacd.smscommunity.common.SendUtils;
import com.jacd.smscommunity.common.Utils;
import com.jacd.smscommunity.data.SmsDB;
import com.jacd.smscommunity.model.ContactsModel;
import com.jacd.smscommunity.model.ListContactModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private final String TAG = "ListContactActivity";
    private final int mREQUEST_EXCEL = 1000;
    private final int mREQUEST_VCARD = 2000;
    private final int mREQUEST_CODE_GALERY2 = 30;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private Context mContext;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private Button btnAddContact;
    private RecyclerView rv;
    private ListContactsAdapter adapter;
    private SmsDB smsDB;
    private List<ListContactModel> listContact;
    private List<Items> listItems;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);

        initComponent();
    }

    private void initComponent() {
        mContext = ContactListActivity.this;
        settings = getSharedPreferences(getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();

        listContact = new ArrayList<>();
        listItems = new ArrayList<>();
        smsDB = new SmsDB(mContext);

        btnAddContact = findViewById(R.id.btn_add_excel);
        rv = findViewById(R.id.rv_contact);

        //validateRequestPermissions();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getList();
    }

    private void getList() {
        listItems.clear();
        listItems.addAll(smsDB.getListContact());
        print1(listItems);
    }

    private void print(List<ListContactModel> list){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new ListContactsAdapter(this, list);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private void print1(List<Items> list){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        ListCAdapter adapter = new ListCAdapter(this, list);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private void loadStorage(int reqCode){

        if (reqCode == 1){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
            } else {
                try {
                    goIntentStorageXLS();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error al abrir la galeria...", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (reqCode == 2){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 6);
            } else {
                try {
                    goIntentStorageVCF();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error al abrir la galeria...", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void goIntentStorageXLS() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/x-excel");
            startActivityForResult(intent, mREQUEST_EXCEL);
        }else{
            String types = "application/vnd.ms-excel";
            String[] mimetypes = {"application/x-excel", "application/vnd.ms-excel"};
            //Intent intent = new Intent();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //intent.setType("application/vnd.ms-excel");
            //intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            //intent.setType("*/.xls , */.xlsx");
            //intent.setType("application/vnd.ms-excel , application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //intentFile.setType("*/.xls, */.xlsx");
            //intent.setType("application/x-excel");
            startActivityForResult(intent, mREQUEST_EXCEL);
        }

    }

    private void goIntentStorageVCF() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/x-vcard");
            startActivityForResult(intent, mREQUEST_VCARD);
        }else{
            String[] mimetypes = {"text/x-vcard"};
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            startActivityForResult(intent, mREQUEST_VCARD);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    switch (requestCode) {
                        case 5:
                            goIntentStorageXLS();
                            break;
                        case 6:
                            goIntentStorageVCF();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error en el permiso de almacenamiento...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Permiso de lectura de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }


    }

    public void addDataExcel(View view) {
        loadStorage(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == mREQUEST_EXCEL){
            try {
                List<ContactsModel> listContact1 = new ArrayList<>();
                String rute="";
                File file;
                Uri uri = data.getData();
                String path = data.getData().getPath();
                String nameFile = ""+path.split("/")[path.split("/").length-1];
                //nameFile = nameFile.split(".")[0];
                //System.out.println(">> "+path);
                //System.out.println(">> "+nameFile);
                //File file = new File(android.os.Environment.MEDIA_MOUNTED + path);

                String path1 = path.contains(":")?path.split(":")[1]:path;
                rute = Environment.getExternalStorageDirectory() + "/" + path1;
                nameFile = nameFile.contains(":")?path1:nameFile;



                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                    file = new File(path);
                }else {
                    file = new File(rute);
                }

                listContact1 = SendUtils.FilesTask.getContactForExcel(mContext, nameFile, file, uri);

                List<Items> itemsList = new ArrayList<>();
                int id = smsDB.getListContact().size()+1;

                ListContactModel item = new ListContactModel(id, "" + nameFile, "" + path
                        ,""+ Utils.ramdomString(8), listContact1);

                //for (ContactsModel cm: listContact1)
                    //System.out.println(">> "+cm.getName() +" - "+cm.getNumber()+ " - "+cm.getTopic());

                itemsList.add(item);
                if (listContact1.size()!=0){
                    List<ListContactModel> list = new ArrayList<>();
                    list.add(item);
                    listContact.add(item);
                    print1(itemsList);
                    smsDB.setListContact(list);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == mREQUEST_VCARD){
            try {
                List<ContactsModel> listContact1 = new ArrayList<>();
                String rute="";
                File file;
                Uri uri = data.getData();
                String path = data.getData().getPath();
                String path1 = data.getData().getLastPathSegment();

                rute = Environment.getExternalStorageDirectory() + "/" + path1;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                    file = new File(path);
                }else {
                    String a = path1.contains(":")?path1.split(":")[1]:path1;
                    String r = Environment.getExternalStorageDirectory() + "/";
                    rute = r+a;
                    Log.w(TAG, rute);
                    file = new File(rute);
                }

                String nameFile = ""+path.split("/")[path.split("/").length-1];
                nameFile = nameFile.contains(":")?path1:nameFile;
                //Log.w(TAG, rute);

                listContact1 = SendUtils.FilesTask.getListVCF(file, nameFile);

                List<Items> itemsList = new ArrayList<>();
                int id = smsDB.getListContact().size()+1;

                ListContactModel item = new ListContactModel(id, "" + nameFile, "" + path
                        ,""+ Utils.ramdomString(8), listContact1);

                itemsList.add(item);
                if (listContact1.size()!=0){
                    List<ListContactModel> list = new ArrayList<>();
                    list.add(item);
                    listContact.add(item);
                    print1(itemsList);
                    smsDB.setListContact(list);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(resultCode == Activity.RESULT_OK && requestCode == 4000){
            dialog = Utils.customDialogWait(mContext);
            dialog.show();
            new Handler().postDelayed(()->{
                if (dialog!=null)
                    dialog.dismiss();
            },3000);
        }
    }

    public void addDataVCard(View view) {
        loadStorage(2);
    }
}