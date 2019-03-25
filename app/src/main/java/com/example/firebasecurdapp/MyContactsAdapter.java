package com.example.firebasecurdapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactsAdapter.MyViewHolder> {
    public static final String USER_KEY = "user_key";
    private List<Contacts> contactsList ;
    private Context mcontext;
    private int adapterPosition;

    public MyContactsAdapter(List<Contacts> contactsList, Context mcontext) {
        this.contactsList = contactsList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contatcs_row,viewGroup,false);
        return new MyViewHolder(Rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Contacts contacts = contactsList.get(i);
        myViewHolder.RowContactNameTv.setText(contacts.getName());
        myViewHolder.RowContactNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,ViewContactDetail.class);
                intent.putExtra(USER_KEY,contacts.getKey());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView RowContactNameTv;
       public MyViewHolder(@NonNull final View itemView) {
           super(itemView);
           RowContactNameTv =  itemView.findViewById(R.id.RowNameTextView_id);
           RowContactNameTv.setOnCreateContextMenuListener(this);
       }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            adapterPosition = this.getAdapterPosition();
            menu.add(this.getAdapterPosition(),121,0,"Delete");
            menu.add(this.getAdapterPosition(),122,1,"Edit");
        }
    }

    public void removeUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contacts").child(contactsList.get(adapterPosition).getKey());
        databaseReference.removeValue();
    }

    public void EditUser(){

        Intent intent = new Intent(mcontext,EditActivity.class);
        intent.putExtra(USER_KEY,contactsList.get(adapterPosition).getKey());
        mcontext.startActivity(intent);

    }

    public int ReturnPosition(){
        return adapterPosition;
    }

}
