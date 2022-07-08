package com.example.contacts.Adapters;

import android.content.Context;
import android.graphics.Color;
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

import com.example.contacts.DBHelper;
import com.example.contacts.Models.ContactModel;
import com.example.contacts.Models.ContactViewModel;
import com.example.contacts.R;

import java.util.ArrayList;

public class ContactsViewAdapter extends  RecyclerView.Adapter<ContactsViewAdapter.Viewholder> {

    ArrayList<ContactModel> list;
    Context context;
    boolean isEnable=false;
    boolean isSelectAll=false;

    ContactViewModel contactViewModel;

    ArrayList<ContactModel> selectList=new ArrayList<>();

    public ContactsViewAdapter(ArrayList<ContactModel> list, Context context) {
        this.list = list;
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
        holder.contactName.setText(model.getContactName());
        holder.contactPhone.setText(model.getPhoneNumber());

        final DBHelper helper = new DBHelper(context);

        holder.contactPhone.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(context,"You Clicked"+list.get(holder.getAdapterPosition()),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.contactName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(!isEnable){
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.contacts_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable = true;
                            contactViewModel.getText().observe((LifecycleOwner) context,new Observer<String>(){
                                @Override
                                public void onChanged(String s) {
                                    actionMode.setTitle("hello");
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
                                        // when array list is empty
                                        // visible text view
//                                        tvEmpty.setVisibility(View.VISIBLE);
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
                                    contactViewModel.setText(String .valueOf(selectList.size()));
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
                        }
                    };
                    view.startActionMode(callback);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
        TextView contactName, contactPhone;
        ImageView checkbox;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

    private void ClickItem(Viewholder holder){
        ContactModel s = list.get(holder.getAdapterPosition());

        if(holder.checkbox.getVisibility() == View.GONE){
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(s);
        }
        else
        {
            // when item selected
            // hide check box image
            holder.checkbox.setVisibility(View.GONE);
            // set background color
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            // remove value from select arrayList
            selectList.remove(s);
        }
        contactViewModel.setText(String.valueOf(selectList.size()));
    }
}