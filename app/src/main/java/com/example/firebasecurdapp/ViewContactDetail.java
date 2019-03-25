package com.example.firebasecurdapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewContactDetail extends AppCompatActivity {

    TextView detailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_detail);

        detailTextView = findViewById(R.id.DtailtextView_id);
        String key = getIntent().getStringExtra(MyContactsAdapter.USER_KEY);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contacts").child(key);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contacts contacts = dataSnapshot.getValue(Contacts.class);
                contacts.setKey(dataSnapshot.getKey());
                detailTextView.setText(contacts.getName()+"\n");
                detailTextView.append(contacts.getNumber()+"\n");
                detailTextView.append(contacts.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
