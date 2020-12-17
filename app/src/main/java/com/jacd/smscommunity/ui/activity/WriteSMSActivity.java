package com.jacd.smscommunity.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacd.smscommunity.R;
import com.jacd.smscommunity.adapter.ListCAdapter;
import com.jacd.smscommunity.common.Items;
import com.jacd.smscommunity.common.Utils;
import com.jacd.smscommunity.model.ContactModel;
import com.jacd.smscommunity.model.ContactsModel;
import com.jacd.smscommunity.model.ListContactModel;
import com.jacd.smscommunity.service.MyService;

import org.apache.xmlbeans.XmlSimpleList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WriteSMSActivity extends AppCompatActivity {

    private final String TAG = "WriteSMSActivity";
    private Context mContext;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private TextView tvContacts;
    private EditText etMessage;
    private Button btnSend;

    private List<ContactsModel> listContact;
    private List<ContactModel> listViewContact;
    private String strContacts;

    private RecyclerView rv;
    private Intent services;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_s_m_s);
        getBundle();
    }

    private void getBundle() {
        count =0;
        services = new Intent(this, MyService.class);
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        strContacts="<Not Contacts>";
        System.out.println("-------------");
        try {
            if (extras != null) {
                listContact = new ArrayList<>();
                listViewContact = new ArrayList<>();
                String jsonArray = extras.getString("list");
                Type listType = new TypeToken<ArrayList<ContactsModel>>(){}.getType();
                List<ContactsModel> yourClassList = new Gson().fromJson(jsonArray, listType);

                strContacts = "";
                for (ContactsModel cm: yourClassList){
                    listContact.add(cm);
                    listViewContact.add(new ContactModel(cm.getId(), cm.getName()));
                    strContacts += "<"+cm.getName()+">";
                    if (yourClassList.iterator().hasNext()){
                        strContacts +=" , ";
                    }
                }
                //Toast.makeText(this, "Lista con "+listContact.size()+" contactos", Toast.LENGTH_LONG).show();
            }


        }catch (Exception e){e.printStackTrace();}
        initComponent();
    }

    private void initComponent() {
        mContext = WriteSMSActivity.this;
        settings = getSharedPreferences(getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();

        tvContacts = findViewById(R.id.tv_w_contacts);
        etMessage = findViewById(R.id.et_w_message);
        btnSend = findViewById(R.id.btn_send);

        tvContacts.setText(strContacts);
        rv = findViewById(R.id.rv_view_contact);
    }

    @Override
    protected void onStart() {
        super.onStart();

        print(listViewContact);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                (findViewById(R.id.cardview_btn)).setVisibility((text.length()>0) ? View.VISIBLE : View.INVISIBLE);
                if (text.length()<=0){
                    Toast.makeText(mContext, "Mensaje demasiado corto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        btnSend.setOnClickListener(view -> {
           /*
            try {
                for (ContactsModel cm: listContact){
                    new Handler().postDelayed(()->{
                        //SendUtils.sendMessageSMS(context, cm.getNumber(), message, false);
                        System.out.println("enviar mensaje a: \""+cm.getName()+" - "+cm.getNumber()+"\"");
                    },8000);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            */
            loadSMS(1);
        });
    }

    private void print(List<ContactModel> list1) {
        List<Items> list = new ArrayList<>(list1);
        GridLayoutManager llm = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        ListCAdapter adapter = new ListCAdapter(this, list);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    private void starServiceNoti() {
        Intent i = new Intent();
        services = new Intent(this, MyService.class);
        Gson gson = new Gson();
        String data = gson.toJson(listContact);
        String message = ""+etMessage.getText().toString();

        editor.putString("run_sms_send_contacts",data);
        editor.putString("run_sms_send_message",message);
        editor.putBoolean("block",false);
        editor.putBoolean("send",true);
        editor.commit();

        //etMessage.setText("  ");
        if (!isMyServiceRunning(this, MyService.class)){ //método que determina si el servicio ya está corriendo o no
            startService(services); //ctx de tipo Context
            Log.d("App", "Service started");
            i.putExtra("show",true);
        } else {
            i.putExtra("show",false);
            Log.d("App", "Service already running");
            Toast.makeText(mContext, "Envio de mensajes en progreso...", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK, i);
        finish();
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void loadSMS(int reqCode){

        if (reqCode == 1){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 5);
            } else {
                try {
                    sentSMS();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error al abrir la galeria...", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    sentSMS();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error en el permiso de envio de SMS...", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Permiso de envio de SMS denegado", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sentSMS() {
        if(settings.getBoolean("block",true)){
            starServiceNoti();
        }else{
            count++;
            boolean val = !isMyServiceRunning(this, MyService.class);
            editor.putBoolean("block",val);
            editor.commit();
            if (!val) Toast.makeText(mContext, "Faltan enviar unos SMS...", Toast.LENGTH_SHORT).show();
            if(count>=4){
                confirmRestart();
            }
        }
    }

    private void confirmRestart(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reiniciar envios de SMS.");
        alertDialog.setCancelable(false);

        alertDialog.setMessage("¿Seguro que desea reiniciar los envios de SMS?\n\nEsto puede no enviar algunos mensajes...");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sí, reiniciar.", (dialog, which)->{
            editor.putBoolean("block",true);
            editor.putBoolean("send",false);
            editor.commit();
            stopService(services);
            count =0;
            Toast.makeText(this, "Reiniciado...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", (dialog, which)->{
            Toast.makeText(mContext, "Envio de Mensajes en progreso...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
    }
}