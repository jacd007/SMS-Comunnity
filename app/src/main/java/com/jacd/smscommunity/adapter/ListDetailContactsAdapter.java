package com.jacd.smscommunity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jacd.smscommunity.R;
import com.jacd.smscommunity.model.ContactsModel;

import java.util.ArrayList;
import java.util.List;

class ListDetailViewHolder extends RecyclerView.ViewHolder {

    TextView mName;
    TextView mNumber;
    ImageView mImage;


    ListDetailViewHolder(View itemView, AppCompatActivity activity) {
        super(itemView);
        mName = itemView.findViewById(R.id.tv_item_name);
        mNumber = itemView.findViewById(R.id.tv_item_number);
        mImage = itemView.findViewById(R.id.ibtn_item_send);

    }
}

public class ListDetailContactsAdapter extends RecyclerView.Adapter<ListDetailViewHolder> {
    private static final String TAG = "ListDetailViewHolder";

    private SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    List<ContactsModel> mData = new ArrayList<>();
    List<ContactsModel> mUpdate = new ArrayList<>();
    private AppCompatActivity Activity;
    private Context context;


    public ListDetailContactsAdapter(Context context, List<ContactsModel> mData) {
        this.mData = mData;
        this.context = context;

        settings = context.getSharedPreferences(context.getResources().getString(R.string.shared_key), 0);
        editor = settings.edit();
    }

 @NonNull
    @Override
    public ListDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_detail_contact_list, parent, false);
        return new ListDetailViewHolder(itemView, Activity);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListDetailViewHolder holder, int position) {

        holder.mName.setText(mData.get(position).getName());
        holder.mNumber.setText(mData.get(position).getNumber());


    }


    public List<ContactsModel> getList() {
       return this.mData;
    }

    public void refresh(List<ContactsModel> list) {
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



















