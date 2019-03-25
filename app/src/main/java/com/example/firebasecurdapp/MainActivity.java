package com.example.firebasecurdapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText nameEd,NumberEd;
    private Button AddBtn;
    private RecyclerView ContactrecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private List<Contacts> contactsList;
    private MyContactsAdapter myContactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        nameEd = findViewById(R.id.NameEd_id);
        NumberEd = findViewById(R.id.NumberEd_id);
        AddBtn = findViewById(R.id.AddBtn_id);
        ContactrecyclerView = findViewById(R.id.ContactRecyclerView_id);
        contactsList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("contacts");

        myContactsAdapter = new MyContactsAdapter(contactsList,MainActivity.this);
        ContactrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ContactrecyclerView.setAdapter(myContactsAdapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Contacts contacts = dataSnapshot.getValue(Contacts.class);
                contacts.setKey(dataSnapshot.getKey());
                contactsList.add(contacts);
                myContactsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                databaseReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Contacts contacts = dataSnapshot.getValue(Contacts.class);
                        contacts.setKey(dataSnapshot.getKey());
                        contactsList.set(myContactsAdapter.ReturnPosition(),contacts);
                        myContactsAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Your contact edit Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i=0;i<contactsList.size();i++){
                    if (contactsList.get(i).getKey()==dataSnapshot.getKey()){
                        contactsList.remove(i);
                        myContactsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addChildEventListener(childEventListener);


        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEd.getText().toString();
                String number = NumberEd.getText().toString();
                if (name.isEmpty() || number.isEmpty() || (name.isEmpty() && number.isEmpty())) {
                    Toast.makeText(MainActivity.this, "You must fill in all of the fields", Toast.LENGTH_SHORT).show();
                } else {
                    String key = databaseReference.push().getKey();
                    Contacts contacts = new Contacts(name, number);
                    databaseReference.child(key).setValue(contacts);
                    nameEd.setText("");
                    NumberEd.setText("");
                }
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 121:{
                myContactsAdapter.removeUser();
                return true;
            }

            case 122:{
                myContactsAdapter.EditUser();
                return true;
            }

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener);
    }
}
