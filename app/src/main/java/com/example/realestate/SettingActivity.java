package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.CellSignalStrength;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Model.Users;
import com.example.realestate.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullnameEdittext, phoneEdittext, emailEdittext, addressEdittext;
    private TextView profileChangeTextbtn, closeText, saveTextbtn;

    private Uri imageUri;
    private String Url = "", fullname, phone, adderss, email, downloadImageUrl, password;
    private StorageReference storageProfilePicref;
    private String checker = "";

    private static final int GalleryPic = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        profileImageView = (CircleImageView) findViewById(R.id.profileimage_settings);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        fullnameEdittext = (EditText) findViewById(R.id.FullName_edittext);
        phoneEdittext = (EditText) findViewById(R.id.PhoneNumber_edittext);
        emailEdittext = (EditText) findViewById(R.id.Email_edittext);
        addressEdittext = (EditText) findViewById(R.id.Address_edittext);

        password = Prevalent.currentonlineUser.getPassword();

        profileChangeTextbtn = (TextView) findViewById(R.id.profileimage_change_btn);
        closeText = (TextView) findViewById(R.id.close_settings);
        saveTextbtn = (TextView) findViewById(R.id.update_settings);

        storageProfilePicref = FirebaseStorage.getInstance().getReference().child("ProfilePic");


//        Picasso.get().load(Prevalent.currentonlineUser.getImage()).into(profileImageView);
//        fullnameEdittext.setText(Prevalent.currentonlineUser.getFullname());
//        phoneEdittext.setText(Prevalent.currentonlineUser.getPhone());
//        emailEdittext.setText(Prevalent.currentonlineUser.getEmail());
//        addressEdittext.setText(Prevalent.currentonlineUser.getAddress());

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfoSaved();
            }
        });
        profileChangeTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                Toast.makeText(SettingActivity.this, "checker clicked", Toast.LENGTH_SHORT).show();

            }
        }); 


        Picasso.get().load(Prevalent.currentonlineUser.getImage()).into(profileImageView);
        fullnameEdittext.setText(Prevalent.currentonlineUser.getFullname());
        phoneEdittext.setText(Prevalent.currentonlineUser.getPhone());
        emailEdittext.setText(Prevalent.currentonlineUser.getEmail());
        addressEdittext.setText(Prevalent.currentonlineUser.getAddress());
    }




//    private void updateOnlyUserInfo() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone());
//
//        HashMap<String, Object> userdataMap = new HashMap<>();
//        userdataMap.put("fullname", fullnameEdittext.getText().toString());
//        userdataMap.put("phone", phoneEdittext.getText().toString());
//        userdataMap.put("email", emailEdittext.getText().toString());
//        userdataMap.put("address", addressEdittext.getText().toString());
//        ref.updateChildren(userdataMap);
//
//        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
//        finish();
//    }

    private void OpenGallery(){
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GalleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!= null) {

            imageUri = data.getData();

            profileImageView.setImageURI(imageUri);
        }
    }

    private void userInfoSaved() {

        if (TextUtils.isEmpty(fullnameEdittext.getText().toString()))
        {
            fullnameEdittext.setError("This field cant be empty");
        }
        if (TextUtils.isEmpty(phoneEdittext.getText().toString()))
        {
            phoneEdittext.setError("This field cant be empty");
        }
        if (TextUtils.isEmpty(emailEdittext.getText().toString()))
        {
            emailEdittext.setError("This field cant be empty");
        }
        if (TextUtils.isEmpty(addressEdittext.getText().toString()))
        {
            addressEdittext.setError("This field cant be empty");
        }
        else if (imageUri == null){
            downloadImageUrl = Prevalent.currentonlineUser.getImage();
            uploadImage();
        }

        else{
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri !=null){
            final StorageReference filepath = storageProfilePicref
                    .child(Prevalent.currentonlineUser.getPhone() + ".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadImageUrl = uri.toString();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone());

                            final HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("fullname", fullnameEdittext.getText().toString());
                            userdataMap.put("phone", phoneEdittext.getText().toString());
                            userdataMap.put("email", emailEdittext.getText().toString());
                            userdataMap.put("address", addressEdittext.getText().toString());
                            userdataMap.put("password", password);
                            userdataMap.put("image", downloadImageUrl);
                            ref.setValue(userdataMap);
                            progressDialog.dismiss();
                            startActivity(new Intent(SettingActivity.this, MainActivity.class));

                        }


                    });
                }
            });


        }
        else
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("fullname", fullnameEdittext.getText().toString());
            userdataMap.put("phone", phoneEdittext.getText().toString());
            userdataMap.put("email", emailEdittext.getText().toString());
            userdataMap.put("address", addressEdittext.getText().toString());
            userdataMap.put("password", password);
            userdataMap.put("image", downloadImageUrl);
            ref.setValue(userdataMap);

            progressDialog.dismiss();
            startActivity(new Intent(SettingActivity.this, MainActivity.class));
            finish();
        }
    }







//        UserRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                         String image = snapshot.child("image").getValue().toString();
//                         String fullname = snapshot.child("fullname").getValue().toString();
//                         String phone = snapshot.child("password").getValue().toString();
//                         String email = snapshot.child("email").getValue().toString();
//                         String address = snapshot.child("address").getValue().toString();
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

