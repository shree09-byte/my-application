package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddImage extends AppCompatActivity {
    String currentUserId;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference UserRef;
    private ProgressDialog loadingBar;
    private static final int PICK_FILE = 1;
    ArrayList<Uri> FileList = new ArrayList<Uri>();

    Button chooseButton, UploadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        mAuth = FirebaseAuth.getInstance();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("PropertyImages");

        chooseButton = findViewById(R.id.button);
        UploadButton = findViewById(R.id.upload);
        loadingBar = new ProgressDialog(this);


    }


    public void ChooseFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE){
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    int i = 0;
                    while (i < count) {

                        Uri File = data.getClipData().getItemAt(i).getUri();
                        FileList.add(File);
                        i++;
                    }

                    Toast.makeText(this, "you have selected"+ FileList.size()+"Files", Toast.LENGTH_SHORT).show();

                }
            }
        }



    }

    public void UploadFiles(View view){


        for (int j=0;j<FileList.size();j++){

            Uri PerFile = FileList.get(j);
            final StorageReference filename = storageReference.child("file"+ j);
            filename.putFile(PerFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    loadingBar.setTitle("property details");
                    loadingBar.setMessage("Please wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    if(task.isSuccessful()){
                        Toast.makeText(AddImage.this, "Succesfull", Toast.LENGTH_SHORT).show();
                        filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloaduri = uri.toString();
                                Toast.makeText(AddImage.this, "To string done", Toast.LENGTH_SHORT).show();
                                UserRef.child("profileimage").setValue(downloaduri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(AddImage.this, "Image uri stored", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                });
                            }
                        });

                    }
                }
            });
        }
    }
}