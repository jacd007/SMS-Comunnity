package com.jacd.smscommunity.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.jacd.smscommunity.data.SmsDB;
import com.jacd.smscommunity.model.ContactModel;
import com.jacd.smscommunity.ui.activity.DetailListActivity;
import com.jacd.smscommunity.ui.activity.ContactListActivity;
import com.jacd.smscommunity.R;
import com.jacd.smscommunity.common.Items;
import com.jacd.smscommunity.model.ContactsModel;
import com.jacd.smscommunity.model.ListContactModel;
import com.jacd.smscommunity.ui.activity.WriteSMSActivity;


import java.util.List;

public class ListCAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ImageView ivSelect;
    private ImageView imageSelect;
    private TextView tvSelect;
    private String method;
    private final int ITEM1 = 1;
    private final int ITEM2 = 2;
    private final int ITEM3 = 3;

    private SmsDB smsDB;
    private List<Items> mData;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public ListCAdapter(Context context, List<Items> mData ) {
        this.mData = mData;
        this.context = context;
        SmsDB smsDB = new SmsDB(context);
    }

    public ListCAdapter(List<Items> mData) {
        this.mData = mData;
        SmsDB smsDB = new SmsDB(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        switch (viewType){
            case ITEM1:
                viewHolder = new Item1Holder(inflater.inflate(R.layout.item_detail_contact_list,parent,false));
                break;
            case ITEM2: viewHolder = new Item2Holder(inflater.inflate(R.layout.item_contact_list,parent,false));
                break;
            case ITEM3: viewHolder = new Item3Holder(inflater.inflate(R.layout.item_view_contact,parent,false));
                break;
            default: viewHolder = new Item1Holder(inflater.inflate(R.layout.item_detail_contact_list,parent));
        }
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM1: //detalle de la lista de contactos
                ContactsModel item1 = (ContactsModel) mData.get(position);

                Item1Holder item1Holder = (Item1Holder)holder;
                item1Holder.mName.setText(""+item1.getName());
                if (item1.getName().length()>=2){
                    //item1Holder.mABC.setVisibility(View.VISIBLE);
                    //item1Holder.mIcon.setVisibility(View.GONE);
                    item1Holder.mABC.setText(""+item1.getName().substring(0, 2));
                }else{
                    //item1Holder.mABC.setVisibility(View.GONE);
                    //item1Holder.mIcon.setVisibility(View.VISIBLE);
                }
                item1Holder.mNumber.setText(""+item1.getNumber());

                break;
            case ITEM2: // lista de contactos de la hoja de excell
                ListContactModel item2 = (ListContactModel) mData.get(position);
                Item2Holder item2Holder = (Item2Holder)holder;

                item2Holder.mName.setText(item2.getName());

                item2Holder.itemView.setOnClickListener(view -> {
                    int contact = ((ListContactModel) mData.get(position)).getContactsModelList().size();

                    List<ContactsModel> list = ((ListContactModel) mData.get(position)).getContactsModelList();

                    Gson gson = new Gson();
                    String data = gson.toJson(list);

                    Intent i = new Intent(context, WriteSMSActivity.class);
                    i.putExtra("list", data);
                    ((ContactListActivity) context).startActivityForResult(i, 4000);
                });

                item2Holder.itemView.setOnLongClickListener(view -> {
                    smsDB = new SmsDB(context);
                    ListContactModel model = ((ListContactModel) mData.get(position));
                    confirmDelete(model);

                    return false;
                });

                item2Holder.mImage.setOnClickListener(view -> {
                    System.out.println("position: "+position);
                    List<ContactsModel> list = ((ListContactModel) mData.get(position)).getContactsModelList();

                    Gson gson = new Gson();
                    String data = gson.toJson(list);

                    Intent i = new Intent(context, DetailListActivity.class);
                    i.putExtra("list", data);
                    ((ContactListActivity) context).startActivityForResult(i, 3000);

                });

                break;
            case ITEM3: //vista de la lista de contactos en envio de sms
                ContactModel item3 = (ContactModel) mData.get(position);

                Item3Holder item3Holder = (Item3Holder)holder;
                item3Holder.mName.setText(""+item3.getName()+"");

                break;
        }

    }

    private void confirmDelete(ListContactModel model){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Eliminar "+model.getName());
        alertDialog.setCancelable(false);

        alertDialog.setMessage("¿Seguro que desea borrar esta lista?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Eliminar Lista", (dialog, which)->{

            Toast.makeText(context, "Eliminando "+model.getName(), Toast.LENGTH_SHORT).show();
            smsDB.deleteListContactByName(model);
            mData.remove(model);

            notifyDataSetChanged();

            dialog.dismiss();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", (dialog, which)->{
            dialog.dismiss();
        });

        alertDialog.show();
    }


    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Item1Holder extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mABC;
        TextView mNumber;
        ImageView mIcon;

        public Item1Holder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_item_name);
            mABC = itemView.findViewById(R.id.tv_item_abc);
            mNumber = itemView.findViewById(R.id.tv_item_number);
            mIcon = itemView.findViewById(R.id.iv_item_icon);
        }
    }

    class Item2Holder extends RecyclerView.ViewHolder{
        TextView mName;
        ImageView mImage;

        public Item2Holder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_item_name);
            mImage = itemView.findViewById(R.id.ibtn_item_send);
        }
    }

    class Item3Holder extends RecyclerView.ViewHolder{
        TextView mName;

        public Item3Holder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_item_name);
        }
    }


}