package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.Model.PropertyDetails;
import com.example.realestate.Prevalent.Prevalent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddProperty extends AppCompatActivity implements View.OnClickListener{
    Spinner apartment_spinner, bhk_spinner, facing_spinner, city_spinner, propertyAge_spinner, water_spinner, furnish_spinner, sanction_spinner;
    EditText Property_Size, Bathroom, Balcony, Property_description,property_price;
    String propertySize, apartmentType, bhkType,propertyAge,waterSupply,sanction,furnishing, bathroom, balcony,security, facing, city,price,phone, property_description, address, parking ,productRandomKey, saveCurrentDate, saveCurrentTime, downloadImageUrl;
    RadioButton radio_yes, radio_no, Bike, Car, Both;
    RadioGroup Security;
    Button UploadFromGallery;
    RelativeLayout relativeLayout, relativeLayout2;
    LinearLayout linearLayout, linearLayout3, linearLayout4, linearLayout5;
    private static final int GalleryPic = 1;
    private Uri imageUri;
    TextView next_button, nextPage3, GetAddress, nextPage1, nextPage2;
    ArrayList<Uri> FileList = new ArrayList<Uri>();
    ArrayList<String> SavedImageUrl = new ArrayList<String>();
    TextView imagesUploaded;

    int count;

    private StorageReference ProductImageRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference PropertyRef,UserPropertyRef, ImageRef;
    private ProgressDialog loadingBar;
    private ProgressBar navigationProgress;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Property Details");
        setSupportActionBar(toolbar);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("PropertyImages");

        Property_Size = (EditText) findViewById(R.id.Property_Size_EditText);
        Bathroom = (EditText) findViewById(R.id.Bathroom_EditText);
        Balcony = (EditText) findViewById(R.id.Balcony_EditText);
        Property_description = (EditText) findViewById(R.id.property_description_EditText);
        property_price = (EditText) findViewById(R.id.property_price_edittext);
        navigationProgress = (ProgressBar) findViewById(R.id.progressBar);
        UploadFromGallery = (Button) findViewById(R.id.uploadFromGallery);
        UploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        imagesUploaded = (TextView) findViewById(R.id.imagesUploaded);

        nextPage3 = (TextView) findViewById(R.id.Nextpage3);
        nextPage1 = (TextView) findViewById(R.id.Nextpage);
        nextPage2 = (TextView) findViewById(R.id.Nextpage2);
        next_button = (TextView) findViewById(R.id.Next);
//        nextPage2 = (TextView) findViewById(R.id.Next);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
        linearLayout5  = (LinearLayout) findViewById(R.id.linearLayout5);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);

        nextPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout3.setVisibility(View.VISIBLE);

                navigationProgress.setProgress(33);
                nextPage1.setVisibility(View.INVISIBLE);
                nextPage2.setVisibility(View.VISIBLE);

            }
        });

        nextPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout3.setVisibility(View.INVISIBLE);
                linearLayout5.setVisibility(View.VISIBLE);

                navigationProgress.setProgress(66);
                nextPage1.setVisibility(View.INVISIBLE);
                nextPage2.setVisibility(View.INVISIBLE);
                nextPage3.setVisibility(View.VISIBLE);
            }
        });

        nextPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout5.setVisibility(View.INVISIBLE);
                linearLayout3.setVisibility(View.INVISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);

                navigationProgress.setProgress(100);
                nextPage1.setVisibility(View.INVISIBLE);
                nextPage2.setVisibility(View.INVISIBLE);
                nextPage3.setVisibility(View.INVISIBLE);
                next_button.setVisibility(View.VISIBLE);
            }
        });

        GetAddress =   (TextView) findViewById(R.id.Address);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        GetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddProperty.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(AddProperty.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


        Security = (RadioGroup) findViewById(R.id.RadioGroup);


        apartment_spinner = (Spinner) findViewById(R.id.Apartment_spinner);
        bhk_spinner = (Spinner) findViewById(R.id.BHK_spinner);
        city_spinner = (Spinner) findViewById(R.id.City_spinner);
        facing_spinner = (Spinner) findViewById(R.id.Facing_spinner);
        propertyAge_spinner = (Spinner) findViewById(R.id.Property_Age_Spinner);
        water_spinner = (Spinner) findViewById(R.id.Water_Supply_Spinner);
        furnish_spinner = (Spinner) findViewById(R.id.Furnishing_Spinner);
        sanction_spinner = (Spinner) findViewById(R.id.Sanction_Spinner);


        next_button.setOnClickListener(this);

        radio_no = (RadioButton) findViewById(R.id.radio_ninjas);
        radio_yes = (RadioButton) findViewById(R.id.radio_pirates);
        Bike = (RadioButton) findViewById(R.id.Bike);
        Car = (RadioButton) findViewById(R.id.Car);
        Both = (RadioButton) findViewById(R.id.Both);

        PropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
        UserPropertyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUser.getPhone()).child("PropertyDetails");
        loadingBar = new ProgressDialog(this);
    }

    private void OpenGallery(){
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, GalleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getClipData() != null){

//            imageUri = data.getData();
            count = data.getClipData().getItemCount();
            int i = 0;
            while(i < count){
                Uri File = data.getClipData().getItemAt(i).getUri();
                FileList.add(File);
                i++;
            }
            Toast.makeText(this, "You Have Selected"+ FileList.size(), Toast.LENGTH_SHORT).show();

            imagesUploaded.setText("Images Uploaded("+ FileList.size()+")");
//            final StorageRefrence filepath = ProductImageRef.child(productRandomKey + ".jpg");
//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(AddProperty.this, "Image Successfull", Toast.LENGTH_SHORT).show();
//                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                               String  downloadImage = uri.toString();
//                               downloadImageUrl = downloadImage;
//                               PropertyRef.child("profileimage").setValue(downloadImage);
//
//                            }
//                        });
//
//                    }
//                    else
//                    {
//
//                        Toast.makeText(AddProperty.this, "Image not succesfull", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Next:
                next_button();
                break;


        }

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(AddProperty.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1

                        );
                        address = addresses.get(0).getAddressLine(0);
                        GetAddress.setText(address);
                    } catch (IOException e) {
                        Toast.makeText(AddProperty.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void next_button(){

        propertySize = Property_Size.getText().toString();
        apartmentType = apartment_spinner.getSelectedItem().toString();
        bhkType = bhk_spinner.getSelectedItem().toString();
        bathroom = Bathroom.getText().toString();
        balcony = Balcony.getText().toString();
        property_description = Property_description.getText().toString();
        facing = facing_spinner.getSelectedItem().toString();
        city = city_spinner.getSelectedItem().toString();
        price = property_price.getText().toString();
        phone = Prevalent.currentonlineUser.getPhone();
        propertyAge = propertyAge_spinner.getSelectedItem().toString();
        waterSupply = water_spinner.getSelectedItem().toString();
        sanction = sanction_spinner.getSelectedItem().toString();
        furnishing = furnish_spinner.getSelectedItem().toString();



        //Radio Group...
        if (radio_yes.isChecked()){
            security = "yes";
        }
        else if (radio_no.isChecked()){
            security = "no";
        }
        else {
            Toast.makeText(this, "Please Select Gated Security", Toast.LENGTH_SHORT).show();
        }

        if (Bike.isChecked()){
            parking = "Bike";
        }
        else if (Car.isChecked()){
            parking = "Car";
        }
        else if (Both.isChecked()){
            parking = "Both";
        }
        else {
            Toast.makeText(this, "Please Select Type of Parking you have", Toast.LENGTH_SHORT).show();
        }


        if (TextUtils.isEmpty(propertySize)){
            Property_Size.setError("This field can't be empty");
            Property_Size.requestFocus();
        }
        else if (apartmentType.equals("Select Apartment Type")){
            Toast.makeText(this, "Do not select default Apartment Type", Toast.LENGTH_SHORT).show();
        }
        else if (propertyAge.equals("Select Property Age")){
            Toast.makeText(this, "Please Select Valid Property Age", Toast.LENGTH_SHORT).show();
        }
        else if (waterSupply.equals("Select")){
            Toast.makeText(this, "Please Select Valid Water Supply", Toast.LENGTH_SHORT).show();
        }
        else if (sanction.equals("Sanction_Spinner_items")){
            Toast.makeText(this, "Please Select Valid Sanction Reason", Toast.LENGTH_SHORT).show();
        }
        else if (furnishing.equals("Furnishing_Spinner_items")){
            Toast.makeText(this, "Please Select Valid Furnishing", Toast.LENGTH_SHORT).show();
        }
        else if (bhkType.equals("Select BHK Type")){
            Toast.makeText(this, "Do not select default BHK Type", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(apartmentType)){
            Toast.makeText(this, "Write BHK Type", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(bathroom)){
            Bathroom.setError("This field can't be empty");
            Bathroom.requestFocus();
        }
        else if (TextUtils.isEmpty(balcony)){
            Balcony.setError("This field can't be empty");
            Balcony.requestFocus();
        }
        else if (TextUtils.isEmpty(property_description)){
            Property_description.setError("This field can't be empty");
            Property_description.requestFocus();
        }
        else if (facing.equals("Select Direction")){
            Toast.makeText(this, "Do not select default Direction", Toast.LENGTH_SHORT).show();
        }
        else if (city.equals("Select City")){
            Toast.makeText(this, "Do not select default City", Toast.LENGTH_SHORT).show();
        }
//        else if (imageUri == null){
//            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(price)){
            property_price.setError("This field can't be empty");
            property_price.requestFocus();
        }

        else {

            loadingBar.setTitle("property details");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());

            productRandomKey = saveCurrentDate + saveCurrentTime;

//            PropertyRef = FirebaseDatabase.getInstance().getReference().child("PropertyDetails");
//            PropertyDetails details = new PropertyDetails(propertySize, apartmentType, bhkType, bathroom, balcony,security, facing, city,price, property_description, address, parking ,productRandomKey, saveCurrentDate, saveCurrentTime, downloadImageUrl, phone);
//            UserPropertyRef.child(productRandomKey).setValue(details);
//            PropertyRef.child(productRandomKey).setValue(details);

            for (int j=0; j < FileList.size();j++) {
                Uri PerFile = FileList.get(j);
                final StorageReference filename = ProductImageRef.child(productRandomKey + j + ".jpg");
                filename.putFile(PerFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                SavedImageUrl.add(url);
                                StoreLink(url);
                            }
                        });
                    }
                });
            }


//            final StorageReference filepath = ProductImageRef.child(productRandomKey + ".jpg");
//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(AddProperty.this, "Image Successfull", Toast.LENGTH_SHORT).show();
//                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                downloadImageUrl = uri.toString();
//
//                                PropertyDetails details = new PropertyDetails(propertySize, apartmentType, bhkType, bathroom, balcony,security, facing, city,price, property_description, address, parking ,productRandomKey, saveCurrentDate, saveCurrentTime, downloadImageUrl,phone);
//                                UserPropertyRef.child(productRandomKey).setValue(details);
//                                PropertyRef.child(productRandomKey).setValue(details)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Toast.makeText(AddProperty.this, "Property Add Succesfully", Toast.LENGTH_SHORT).show();
//                                                    loadingBar.dismiss();
//                                                }
//                                                else {
//                                                    String exception = task.getException().toString();
//                                                    Toast.makeText(AddProperty.this, "ERROR:"+ exception, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//
//                            }
//                        });
//
//                    }
//                    else
//                    {
//
//                        Toast.makeText(AddProperty.this, "Image not succesfull", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });







        }


    }

    private void StoreLink(String url) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("PropertyDetails").child(productRandomKey);
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0 ; i< SavedImageUrl.size();i++){
            hashMap.put("downloadImageUrl" + i, SavedImageUrl.get(i));
        }
            hashMap.put("propertySize", propertySize);
        hashMap.put("apartmentType", apartmentType);
        hashMap.put("phone", phone);
        hashMap.put("bhkType", bhkType);
        hashMap.put("propertyAge", propertyAge);
        hashMap.put("waterSupply", waterSupply);
        hashMap.put("sanction", sanction);
        hashMap.put("furnishing", furnishing);
        hashMap.put("bathroom", bathroom);
        hashMap.put("balcony", balcony);
        hashMap.put("security", security);
        hashMap.put("facing", facing);
        hashMap.put("city", city);
        hashMap.put("price", price);
        hashMap.put("property_description", property_description);
        hashMap.put("address", address);
        hashMap.put("parking", parking);
        hashMap.put("productRandomKey", productRandomKey);
        hashMap.put("saveCurrentDate", saveCurrentDate);
        hashMap.put("saveCurrentTime", saveCurrentTime);
        databaseReference.setValue(hashMap);
        UserPropertyRef.child(productRandomKey).setValue(hashMap);
        loadingBar.dismiss();
        FileList.clear();

//        StoreInformation();

    }

    private void StoreInformation() {
//        PropertyDetails details = new PropertyDetails(propertySize, apartmentType, phone, bhkType, bathroom, balcony,security, facing, city,price, property_description, address, parking ,productRandomKey, saveCurrentDate, saveCurrentTime, downloadImageUrl);
//
//        PropertyRef.child(productRandomKey).setValue(details)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(AddProperty.this, "Property Add Succesfully", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//                        }
//                        else {
//                            String exception = task.getException().toString();
//                            Toast.makeText(AddProperty.this, "ERROR:"+ exception, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private void StorePropertyInformation() {
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYYY");
//        saveCurrentDate = currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentTime.format(calendar.getTime());
//
//        productRandomKey = saveCurrentDate + saveCurrentTime;
//
//        PropertyDetails details = new PropertyDetails(propertySize, ApartmentType, BHKType, bathroom, balcony,security,productRandomKey, saveCurrentDate, saveCurrentTime);
//
//        PropertyRef.child(productRandomKey).setValue(details);
//        SaveProductInfoToDatabase();
    }

//    private void SaveProductInfoToDatabase() {
//
////        HashMap<String, Object> productMap = new HashMap<>();
////        productMap.put("pid", productRandomKey);
////        productMap.put("date", saveCurrentDate);
////        productMap.put("time", saveCurrentTime);
////        productMap.put("PropertyType", ApartmentType);
////        productMap.put("BHK Type", BHKType);
////        productMap.put("Property Size", propertySize);
////        productMap.put("No of Bathroom", bathroom);
////        productMap.put("Balcony", balcony);
////        productMap.put("Gated Security", security);
//
//        PropertyDetails details = new PropertyDetails(propertySize, ApartmentType, BHKType, bathroom, balcony,security,productRandomKey, saveCurrentDate, saveCurrentTime);
//
//        PropertyRef.child(productRandomKey).setValue(details);
//
//    }
}