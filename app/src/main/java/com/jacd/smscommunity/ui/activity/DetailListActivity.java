package com.jacd.smscommunity.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacd.smscommunity.R;
import com.jacd.smscommunity.adapter.ListCAdapter;
import com.jacd.smscommunity.adapter.ListContactsAdapter;
import com.jacd.smscommunity.common.Items;
import com.jacd.smscommunity.model.ContactsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailListActivity extends AppCompatActivity {

    private final String TAG = "DetailListActivity";
    private final int REQUEST_CONTENT = 100;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private Context mContext;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private FloatingActionButton btnFab;
    private RecyclerView rv;
    private ListContactsAdapter adapter;
    private List<Items> listContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_list);
        getBundle();
        initComponent();
    }

    private void getBundle() {
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        System.out.println("-------------");
        try {
            if (extras != null) {
                listContact = new ArrayList<>();
                String jsonArray = extras.getString("list");
                Type listType = new TypeToken<ArrayList<ContactsModel>>(){}.getType();
                List<ContactsModel> yourClassList = new Gson().fromJson(jsonArray, listType);

                for (ContactsModel cm: yourClassList){
                    listContact.add(cm);
                    System.out.println(">> "+cm.getName() +" - "+cm.getNumber()+ " - "+cm.getTopic());
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }

    private void initComponent() {
        mContext = DetailListActivity.this;
        settings = getSharedPreferences(getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();

        rv = findViewById(R.id.rv_detail_contact);
        btnFab = findViewById(R.id.fab);

    }

    @Override
    protected void onStart() {
        super.onStart();
        print(listContact);

        btnFab.setOnClickListener(view -> {
            Gson gson = new Gson();
            String data = gson.toJson(listContact);

            Intent i = new Intent(mContext, WriteSMSActivity.class);
            i.putExtra("list", data);
            startActivityForResult(i, 4000);
        });
    }


    private void print(List<Items> list){
        LinearLayoutManager llm = new LinearLayoutManager(this);
        ListCAdapter adapter = new ListCAdapter(this, list);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == 4000){
                finish();
        }
        if(resultCode == Activity.RESULT_CANCELED && requestCode == 4000){
            finish();
        }
    }
}