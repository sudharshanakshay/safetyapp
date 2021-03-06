package com.example.safetyapp.Adapters;

import android.content.Context;
//import androidx.appcompat.view.ActionMode;
import android.content.Intent;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safetyapp.R;
import com.example.safetyapp.DBHelper;
import com.example.safetyapp.Models.ContactModel;
import com.example.safetyapp.Models.ContactViewModel;
import com.example.safetyapp.SaveContactActivity;

import java.util.ArrayList;

public class ContactsViewAdapter extends  RecyclerView.Adapter<ContactsViewAdapter.Viewholder> {

    ArrayList<ContactModel> list;
    Context context;
    boolean isEnable=false;
    boolean isSelectAll=false;

    ContactViewModel contactViewModel;

    ArrayList<ContactModel> selectList = new ArrayList<>();

    public ContactsViewAdapter(Context context) {
        ArrayList<ContactModel> list;
//        this.list = list;
        final DBHelper helper = new DBHelper(context);
        this.list = helper.getContactsList();
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.sample_contact, viewGroup, false);

        contactViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ContactViewModel.class);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        final ContactModel model = list.get(position);

        char firstLetter =  model.getContactName().toUpperCase().charAt(0);

        holder.avatarFirstChar.setText(String.valueOf(firstLetter));
        holder.contactName.setText(model.getContactName());
        holder.contactPhone.setText(model.getPhoneNumber());

        if(list.size()==0){
            holder.displayWhenEmpty.setVisibility(View.VISIBLE);
        }

        final DBHelper helper = new DBHelper(context);

        holder.contactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEnable)
                {
                    // when action mode is enable
                    // call method
                    ClickItem(holder);
                }
                else
                {
                    // when action mode is not enable
                    // display toast

                    Intent intent = new Intent(context, SaveContactActivity.class);
                    intent.putExtra("contactID", String.valueOf(model.getContactId()));
                    context.startActivity(intent);

//                    Toast.makeText(context,"You Clicked"+list.get(holder.getAdapterPosition()),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.contactName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!isEnable){
                    ClickItem(holder);
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.contacts_onselect_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable = true;
                            contactViewModel.getText().observe((LifecycleOwner) context,new Observer<String>(){
                                @Override
                                public void onChanged(String s) {

                                    actionMode.setTitle("1");
                                }
                            });
                            return true;
                        }


                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case  R.id.action_delete_forever:
                                    for(ContactModel s:selectList)
                                    {
                                        // remove selected item list
                                        list.remove(s);
                                        helper.deleteContact(s);

                                    }
                                    // check condition
                                    if(list.size()==0)
                                    {
//                                        holder.displayWhenEmpty.setVisibility(View.VISIBLE);
                                    }
                                    // finish action mode
                                    actionMode.finish();
                                    break;
                                case R.id.action_select_all:
                                    if(selectList.size()==list.size())
                                    {
                                        // when all item selected
                                        // set isselectall false
                                        isSelectAll=false;
                                        // create select array list
                                        selectList.clear();
                                    }
                                    else
                                    {
                                        // when  all item unselected
                                        // set isSelectALL true
                                        isSelectAll=true;
                                        // clear select array list
                                        selectList.clear();
                                        // add value in select array list
                                        selectList.addAll(list);
                                    }
                                    // set text on view model
                                    contactViewModel.setText(String.valueOf(selectList.size()));
                                    // notify adapter
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            // when action mode is destroy
                            // set isEnable false
                            isEnable=false;
                            // set isSelectAll false
                            isSelectAll=false;
                            // clear select array list
                            selectList.clear();
                            // notify adapter
                            notifyDataSetChanged();
                            holder.checkbox.setVisibility(View.GONE);
//                            holder.checkbox_outline.setVisibility(View.GONE);
                            // set background color
//                            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

                        }
                    };
                    view.startActionMode(callback);
                }
                else {
                    ClickItem(holder);
                }
                return true;
            }
        });

        if(isSelectAll)
        {
            // when value selected
            // visible all check boc image
//            holder.checkbox_outline.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
            //set background color
//            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {
            // when all value unselected
            // hide all check box image
            holder.checkbox.setVisibility(View.GONE);
//            holder.checkbox_outline.setVisibility(View.VISIBLE);
            // set background color
//            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
        TextView contactName, contactPhone, avatarFirstChar, displayWhenEmpty;
        ImageView checkbox, checkbox_outline;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            checkbox = itemView.findViewById(R.id.checkbox);
            avatarFirstChar = itemView.findViewById(R.id.avatarFirstChar);
            displayWhenEmpty = itemView.findViewById(R.id.displayWhenEmpty);
            checkbox_outline = itemView.findViewById(R.id.checkbox_outline);
        }
    }

    private void ClickItem(Viewholder holder){
        ContactModel s = list.get(holder.getAdapterPosition());

        if(holder.checkbox.getVisibility() == View.GONE){
//            holder.checkbox_outline.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
            selectList.add(s);
        }
        else
        {
            holder.checkbox.setVisibility(View.GONE);
            selectList.remove(s);
        }
        contactViewModel.setText(String.valueOf(selectList.size()));
    }
}