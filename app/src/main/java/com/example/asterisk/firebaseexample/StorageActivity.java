package com.example.asterisk.firebaseexample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {
    private static final int INT_CONST = 5;
    Button choose,upload;
    TextView tv;
    Uri dataUri;
    StorageReference sr;
    DatabaseReference dr;
    ProgressDialog progressDialog;
    int permissioncheck;
    ListView lv;
    List<String> image = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        choose = findViewById(R.id.choose);
        upload = findViewById(R.id.upload);
        tv = findViewById(R.id.filename);
        lv = findViewById(R.id.list);
        dr = FirebaseDatabase.getInstance().getReference().child("url");
        sr = FirebaseStorage.getInstance().getReference().child("file");
        permissioncheck = ContextCompat.checkSelfPermission(StorageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissioncheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},12);
        }
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");        // i.setType("*/*"); selects all type
                startActivityForResult(i,INT_CONST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataUri != null) {
                    progressDialog = new ProgressDialog(StorageActivity.this);
                    progressDialog.setMessage("Uploading ...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    sr.putFile(dataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            dr.push().setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(StorageActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StorageActivity.this, "Upload failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(StorageActivity.this, "File upload failure", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int percentage = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(percentage);
                        }
                    });
                }}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INT_CONST && resultCode == RESULT_OK) {
            dataUri = data.getData();
            tv.setText(dataUri.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},12);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                image.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String name = ds.getValue().toString();
                    image.add(name);
                }
                lv.setAdapter(new NewAdapter(StorageActivity.this,image));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
