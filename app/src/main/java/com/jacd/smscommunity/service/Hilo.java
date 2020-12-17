package com.jacd.smscommunity.service;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jacd.smscommunity.R;
import com.jacd.smscommunity.common.SendUtils;
import com.jacd.smscommunity.common.Utils;
import com.jacd.smscommunity.model.ContactsModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Hilo extends AsyncTask<String,String,String> {
    private static final String TAG ="Hilo Service";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Dialog dialog;
    private List<ContactsModel> list;
    private String message;
    private int count;
    private boolean val;

    private int TIME_SLEEP_SMS_WAIT = 1000 * 60 * 30;
    private int TIME_SLEEP_SMS = 1000 * 5;


    Hilo(Context context){
        this.context = context;

        getList();
    }

    private void getList() {
        list = new ArrayList<>();
        message = "";
        settings = context.getSharedPreferences(context.getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();

        String concacts = settings.getString("run_sms_send_contacts",""+new Gson().toJson(list));
        try {
            list = new ArrayList<>();
            Type listType = new TypeToken<ArrayList<ContactsModel>>(){}.getType();
            List<ContactsModel> yourClassList = new Gson().fromJson(concacts, listType);

            for (ContactsModel cm: yourClassList){
                list.add(cm);
                //System.out.println(">>"+cm.getName());
            }
        }catch (Exception e){e.printStackTrace();}

        message = settings.getString("run_sms_send_message","");
    }

    /*
    @Override
    protected String doInBackground(String... strings) {
        count = 0;
        try{
            do{
                if (count<30){
                    System.out.println("Hola mundo...");
                    sleep(2000);
                    count++;
                }else {
                    count=0;
                    notifications("Espere 10 segundos",
                            " limite excedido de envio de sms",
                            Utils.getToday("MMMM dd, hh:mm a"));
                    sleep(10000);
                }
            }while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    */

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (context != null){
            dialog = Utils.customDialogWait(context);
            val=true;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        count = 0;
        int c=0;
        try {
            if (list.size()>0){
                notifications("Enviando SMS",
                        "Tiempo estimado: "+getTimeWait(list.size())+"...\nEspere mientras se envian los mensajes...",
                        //"Tiempo estimado: "+getTimeWait(list.size())+"... \nPara enviar los "+list.size()+" mensajes, \nse enviarán 10 SMS cada 20 segundos...",
                        Utils.getToday("hh:mm:ss a"), false);
                for (ContactsModel cm: list){
                    //if (settings.getBoolean("block",false)) break;
                    c++;
                    //if (count<30){
                    if (count<10){
                        System.out.println(">> "+c+" -> "+cm.getName() +" - "+cm.getNumber()+ " - "+cm.getTopic()+"-> "+message);
                        SendUtils.sendMessageSMS(context, cm.getNumber(), message, false);
                        //sleep(5000);
                        //sleep(TIME_SLEEP_SMS);
                        count++;
                    }else {
                        count=0;
                        Log.i(TAG,"pausado 20 segundos....");
                        /*
                        notifications("Limite Excedido",
                                "Ha alcanzado el limite de envio de sms. \nSe continuará el envio dentro de 30 minutos. \n"
                                        +Utils.getToday("MMMM dd, hh:mm a"),
                                "#"+c+" de "+list.size());
                        */
                        sleep(20000);
                        //sleep(TIME_SLEEP_SMS_WAIT);
                    }
                }
            }else{
                Log.e(TAG,"Lista esta vacia");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeWait(int size) {
        int input = size * 2;
        int numberOfDays;
        int numberOfHours;
        int numberOfMinutes;
        int numberOfSeconds;

        numberOfDays = input / 86400;
        numberOfHours = (input % 86400 ) / 3600 ;
        numberOfMinutes = ((input % 86400 ) % 3600 ) / 60;
        numberOfSeconds = ((input % 86400 ) % 3600 ) % 60  ;
        String res = "";
        if (numberOfDays!=0)res = ""+numberOfDays+"día"+(numberOfDays>1?"s":"");
        if (numberOfHours!=0)res = res+" "+numberOfHours+"hora"+(numberOfHours>1?"s":"");
        if (numberOfMinutes!=0)res = res+" "+numberOfMinutes+"minuto"+(numberOfMinutes>1?"s":"");
        if (numberOfSeconds!=0)res = res+" "+numberOfSeconds+"segundos";
        return res;
    }


    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (context!=null){
            if (dialog!=null)
                dialog.dismiss();
            editor.clear();
            editor.putBoolean("block",true);
            editor.commit();
            Log.i(TAG, "finalizado....");
            if (!settings.getBoolean("block",false)){

            }
            notifications("Envios de SMS",
                    "Envios finalizados...",
                    Utils.getToday("MMMM dd, hh:mm a"), true);
            Toast.makeText(context, "Envios finalizados...", Toast.LENGTH_SHORT).show();
        }

    }

    private void notifications(String title, String content, String info, boolean isFinish) {
        String date = Utils.getToday("yyyy-MM-dd HH:mm:ss");
        int icon = isFinish ? R.drawable.ic_finish :R.drawable.ic_email;
        if (context != null){
            NotificationsTask.createNotificacionsSilent(context, icon, ""+title,
                    ""+content, ""+info, false, 1);
        }

    }

}
