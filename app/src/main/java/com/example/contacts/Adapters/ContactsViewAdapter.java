package com.example.contacts.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.Models.ContactModel;
import com.example.contacts.R;

import java.util.ArrayList;

public class ContactsViewAdapter extends  RecyclerView.Adapter<ContactsViewAdapter.Viewholder> {

    ArrayList<ContactModel> list;
    Context context;

    public ContactsViewAdapter(ArrayList<ContactModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.contactdisplaysample, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final ContactModel model = list.get(position);
        holder.contactName.setText(model.getContactName());
        holder.contactPhone.setText(model.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class Viewholder extends RecyclerView.ViewHolder{

        TextView contactName, contactPhone;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
        }
    }
}
