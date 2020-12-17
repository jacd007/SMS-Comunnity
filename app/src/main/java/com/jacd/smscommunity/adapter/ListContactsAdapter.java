package com.jacd.smscommunity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.jacd.smscommunity.R;
import com.jacd.smscommunity.model.ListContactModel;

import java.util.ArrayList;
import java.util.List;

class ListViewHolder extends RecyclerView.ViewHolder {

    TextView mName;
    ImageView mImage;


    ListViewHolder(View itemView, AppCompatActivity activity) {
        super(itemView);
        mName = itemView.findViewById(R.id.tv_item_name);
        mImage = itemView.findViewById(R.id.ibtn_item_send);

    }
}

public class ListContactsAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private static final String TAG = "ListAdapter";

    private SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    List<ListContactModel> mData = new ArrayList<>();
    List<ListContactModel> mUpdate = new ArrayList<>();
    private AppCompatActivity Activity;
    private Context context;


    public ListContactsAdapter(Context context, List<ListContactModel> mData) {
        this.mData = mData;
        this.context = context;

        settings = context.getSharedPreferences(context.getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();
    }

 @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_contact_list, parent, false);
        return new ListViewHolder(itemView, Activity);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        holder.mName.setText(mData.get(position).getName());


    }


    public List<ListContactModel> getList() {
       return this.mData;
    }

    public void refresh(List<ListContactModel> list) {
        this.mData = list;
        this.notifyDataSetChanged();
    }

    public void refresh() {
        this.notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }

}



















