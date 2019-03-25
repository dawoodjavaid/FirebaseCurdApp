package com.example.firebasecurdapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    private EditText nameEd,NumberEd;
    private Button SaveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nameEd = findViewById(R.id.NameEd_id);
        NumberEd = findViewById(R.id.NumberEd_id);
        SaveBtn = findViewById(R.id.SaveBtn_id);

        String key = getIntent().getStringExtra(MyContactsAdapter.USER_KEY);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("contacts").child(key);
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEd.getText().toString();
                String Number = NumberEd.getText().toString();
                if (name.isEmpty() || Number.isEmpty() || (name.isEmpty() && Number.isEmpty())) {
                    Toast.makeText(EditActivity.this, "You must fill in all of the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", name);
                    data.put("number", Number);

                    databaseReference.updateChildren(data);

                    nameEd.setText("");
                    NumberEd.setText("");
                    finish();
                }
            }
        });
    }
}
