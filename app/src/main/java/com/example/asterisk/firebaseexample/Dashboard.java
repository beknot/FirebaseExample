package com.example.asterisk.firebaseexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    EditText roll,name;
    Button save,storage;
    ListView listview;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    List<DataModule> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        roll = findViewById(R.id.roll);
        name = findViewById(R.id.name);
        save = findViewById(R.id.save);
        storage = findViewById(R.id.storage);
        listview = findViewById(R.id.listview);
        progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setMessage("Saving data ...");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");

        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,StorageActivity.class);
                startActivity(i);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                DataModule dm = new DataModule();
                dm.setRoll(Integer.parseInt(roll.getText().toString()));
                dm.setName(name.getText().toString());
                databaseReference.push().setValue(dm).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(Dashboard.this,"Success",Toast.LENGTH_LONG).show();
                        onResume();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    Toast.makeText(Dashboard.this,"Failure",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        list.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    DataModule m =new DataModule();
                    m.setRoll(Integer.parseInt(ds.child("roll").getValue().toString()));
                    m.setName(ds.child("name").getValue().toString());
                    list.add(m);
                }
                listview.setAdapter(new MyAdapter(Dashboard.this,list));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onResume();
    }
}
