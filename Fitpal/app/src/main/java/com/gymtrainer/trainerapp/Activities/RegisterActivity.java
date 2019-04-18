package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.Models.Category;
import com.gymtrainer.trainerapp.Models.Trainer;
import com.gymtrainer.trainerapp.Models.TrainerId;
import com.gymtrainer.trainerapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
ResultCallback{

    private final int REQUEST_CHECK_SETTINGS =100;
    String[] listCategoryItems,listHoursItems;
    boolean[] checkedCategoryItems,checkedHourItems;

    TextView textViewCategorySelect,textViewHourSelect;

    List<String> selectedHoursList,selectedCategoriesList;
    ArrayList<Integer> mCategoryItem = new ArrayList<>();
    ArrayList<Integer> mHourItem = new ArrayList<>();


    ArrayList<String> categoryLIST;
    List<Category> categoriesList;

    RadioGroup radioGroupGender;
    RadioButton radioGenderButton;
    Button registerButton;
    TextView textViewLogin,textViewFetchLocation;
    FirebaseAuth auth;
    ProgressBar progressBar;
    boolean isCheckedTrail;
    DatabaseReference databaseReferenceTrainer,databaseReferenceCategories;
    FirebaseUser firebaseUser;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLngCurrent;

    EditText ed_name,ed_email,ed_password,ed_confirmpassword,ed_phonenumber,ed_address,ed_city,ed_experience,ed_about,ed_hour_rate;
    String name,email,password,confirmpassword,phonenumber,address,city,experience,description,rate,trail;
    CheckBox checkBoxTrail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initControls();
        setListeners();
    }

    private void initControls()
    {
        categoryLIST = new ArrayList<>();
      //  listCategoryItems = new String[]{"ABC","DEF","GHI","JKL","MNO"};
        listHoursItems = new String[]{"8 AM","9 AM","10 AM","11 AM","12 PM","01 PM","02 PM","03 PM","04 PM","05 PM"};
        registerButton = (Button)findViewById(R.id.registerButton);
        textViewLogin = (TextView)findViewById(R.id.goToLogin);
        selectedHoursList = new ArrayList<>();
        selectedCategoriesList = new ArrayList<>();
        textViewFetchLocation = (TextView)findViewById(R.id.edLocation);
        progressBar = (ProgressBar)findViewById(R.id.progressBarRegister);
        checkBoxTrail = (CheckBox)findViewById(R.id.checkboxTrail);
        categoriesList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();

        databaseReferenceTrainer = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers");
        databaseReferenceCategories = FirebaseDatabase.getInstance().getReference().child("Categories");
        textViewCategorySelect = (TextView)findViewById(R.id.textViewCategorySelect);
        textViewHourSelect = (TextView)findViewById(R.id.edHours);
        radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);

        ed_name = (EditText)findViewById(R.id.edName);
        ed_email = (EditText)findViewById(R.id.edEmail);
        ed_password = (EditText)findViewById(R.id.edPassword);
        ed_confirmpassword = (EditText)findViewById(R.id.edConfirmPassword);
        ed_phonenumber = (EditText)findViewById(R.id.edPhoneNumber);
        ed_address = (EditText)findViewById(R.id.edAddress);
        ed_hour_rate = (EditText)findViewById(R.id.edRatePerHour);
        ed_city = (EditText)findViewById(R.id.edCity);
        ed_experience = (EditText)findViewById(R.id.edExperience);
        ed_about = (EditText)findViewById(R.id.edAbout);




        databaseReferenceCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     categoryLIST.clear();

                    for(DataSnapshot dss:dataSnapshot.getChildren())
                    {
                        Category category = dss.getValue(Category.class);
                        categoryLIST.add(category.getCategoryName());
                    }

               listCategoryItems = new String[categoryLIST.size()];
                for (int i = 0; i < categoryLIST.size(); i++) {
                    listCategoryItems[i] = categoryLIST.get(i);
                }

                checkedCategoryItems = new boolean[listCategoryItems.length];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        checkedHourItems = new boolean[listHoursItems.length];

    }

    private void setListeners()
    {

        if(textViewFetchLocation.getText().toString().equals("Fetch Location"))
        {

            textViewFetchLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    client = new GoogleApiClient.Builder(v.getContext())
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(RegisterActivity.this)
                            .addOnConnectionFailedListener(RegisterActivity.this)
                            .build();
                    client.connect();

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Location is already Fetched",Toast.LENGTH_LONG).show();
        }





        textViewHourSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose Working Hours");
                builder.setMultiChoiceItems(listHoursItems, checkedHourItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            if (!mHourItem.contains(which)) {
                                mHourItem.add(which);
                            }
                        }
                        else {
                            mHourItem.remove((Integer)which);
                        }

                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";

                        for(int i=0;i< mHourItem.size();i++)
                        {

                            item = item + listHoursItems[mHourItem.get(i)];

                            if(i!=mHourItem.size() - 1)
                            {

                                item = item + ", ";
                            }
                        }

                        if(item.equals(""))
                        {
                            textViewHourSelect.setText("Choose your available hours");
                        }
                        else
                        {
                            textViewHourSelect.setText(item);
                            selectedHoursList.clear();

                            for(int i=0;i<mHourItem.size();i++)
                            {
                                selectedHoursList.add(listHoursItems[mHourItem.get(i)]);
                            }


                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkedHourItems.length;i++)
                        {
                            checkedHourItems[i] = false;
                            mHourItem.clear();
                            textViewHourSelect.setText("Choose your available hours");
                        }
                    }
                });

                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });



        textViewCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose categories");
                builder.setMultiChoiceItems(listCategoryItems, checkedCategoryItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            if (!mCategoryItem.contains(which)) {
                                mCategoryItem.add(which);
                            }
                        }
                        else if(mCategoryItem.contains(which)){
                            mCategoryItem.remove((Integer)which);
                        }
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for(int i=0;i< mCategoryItem.size();i++)
                        {
                            item = item + listCategoryItems[mCategoryItem.get(i)];
                            if(i!=mCategoryItem.size() - 1)
                            {
                                item = item + ", ";
                            }
                        }

                        if(item.equals(""))
                        {
                            textViewCategorySelect.setText("Choose categories");
                        }
                        else
                        {
                            textViewCategorySelect.setText(item);

                            selectedCategoriesList.clear();

                            for(int i=0;i<mCategoryItem.size();i++)
                            {
                                selectedCategoriesList.add(listCategoryItems[mCategoryItem.get(i)]);
                            }



                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkedCategoryItems.length;i++)
                        {
                            checkedCategoryItems[i] = false;
                            mCategoryItem.clear();
                            textViewCategorySelect.setText("Choose categories");
                        }
                    }
                });

                AlertDialog mDialog = builder.create();
                mDialog.show();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTrainer();
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });
    }

    public void registerTrainer()
    {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        radioGenderButton = (RadioButton) findViewById(selectedId);
        name = ed_name.getText().toString();
        email = ed_email.getText().toString();
        password= ed_password.getText().toString();
        confirmpassword = ed_confirmpassword.getText().toString();
        phonenumber = ed_phonenumber.getText().toString();
        address = ed_address.getText().toString();
        city = ed_city.getText().toString();
        experience = ed_experience.getText().toString();
        description = ed_about.getText().toString();
        rate = ed_hour_rate.getText().toString();


        if (!password.equals(confirmpassword))
        {
            Toast.makeText(RegisterActivity.this, "Password must be equal to the confirm password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(RegisterActivity.this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValid(email))
        {
            Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals("") || confirmpassword.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(RegisterActivity.this, "Password must be 6 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phonenumber.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "Phone number cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(address.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "Address cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(city.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "City cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(experience.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "Experience cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(description.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "Description cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(textViewCategorySelect.getText().toString().equals("Choose categories"))
        {
            Toast.makeText(RegisterActivity.this, "You must choose atleast 1 category", Toast.LENGTH_SHORT).show();
            return;
        }

        if(textViewHourSelect.getText().toString().equals("Choose your available hours"))
        {
            Toast.makeText(RegisterActivity.this, "You must choose atleast 1 working hour", Toast.LENGTH_SHORT).show();
            return;
        }

        if(rate.equals(""))
        {
            Toast.makeText(RegisterActivity.this, "You must specify your working rate", Toast.LENGTH_SHORT).show();
            return;
        }

        else
        {

            registerFirebaseAuth();
        }


    }

    private void registerFirebaseAuth()
    {
          isCheckedTrail = checkBoxTrail.isChecked();

            progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    insertDatabase();


                                }
                        }
                    });
    }

    private void insertDatabase()
    {

        firebaseUser = auth.getCurrentUser();

        if(isCheckedTrail)
        {
            trail = "true";
        }
        else
        {
            trail = "false";
        }

        if(latLngCurrent!=null && !textViewFetchLocation.getText().toString().equals("Fetch Location"))
        {
            Trainer trainer = new Trainer(name,email,phonenumber,address,city,radioGenderButton.getText().toString(),trail,experience,description,rate,"https://firebasestorage.googleapis.com/v0/b/gym-trainer-app.appspot.com/o/ic_header_round.png?alt=media&token=e84a860f-706c-466c-8f2b-08db4202bf7e",firebaseUser.getUid(),String.valueOf(latLngCurrent.latitude),String.valueOf(latLngCurrent.longitude));


            databaseReferenceTrainer.child(firebaseUser.getUid()).setValue(trainer)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            setCategories();
                            setWorkingHrs();
                        }
                    });
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Could not fetch your current Location. Please fetch your location",Toast.LENGTH_LONG).show();
        }

    }

    private void setCategories() {
        databaseReferenceTrainer.child(firebaseUser.getUid())
                .child("Categories").setValue(selectedCategoriesList);

    }
    private void setWorkingHrs()
    {
        databaseReferenceTrainer.child(firebaseUser.getUid())
                .child("WorkingHrs").setValue(selectedHoursList);

        for(int i=0;i<selectedCategoriesList.size();i++)
        {
            Query query = databaseReferenceCategories.orderByChild("categoryName").equalTo(selectedCategoriesList.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                    //    Category category= dataSnapshot.getChildren().iterator().next().getValue(Category.class);
                        pushTrainer(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

        sendVerificationLink();

    }

    private void sendVerificationLink()
    {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"A verification email has been sent to your email address",Toast.LENGTH_LONG).show();
                                gotoHomeScreen();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Could not send email verification. Try again later",Toast.LENGTH_LONG).show();
                            }
                    }
                });
    }

    private void gotoHomeScreen()
    {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(RegisterActivity.this,SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void pushTrainer(DataSnapshot dataSnapShot)
    {
        String key= dataSnapShot.getChildren().iterator().next().getKey();
        TrainerId trainerId = new TrainerId(firebaseUser.getUid());
        databaseReferenceCategories.child(key).child("TrainerId").push().setValue(trainerId);
    }

    public void gotoLogin()
    {
        Intent i = new Intent(RegisterActivity.this,SignInActivity.class);
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        if(latLngCurrent!=null)
        {
            textViewFetchLocation.setText(getStringAddress(latLngCurrent.latitude,latLngCurrent.longitude));

        }
        progressBar.setVisibility(View.GONE);
        textViewFetchLocation.setClickable(false);
        Toast.makeText(getApplicationContext(),"Location fetched successfully",Toast.LENGTH_LONG).show();



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

         return;

        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        client,
                        builder.build()
                );
        result.setResultCallback(this);


        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String getStringAddress(Double lat,Double lng)
    {
        String address="";
        String city = "";
        Geocoder geocoder;
        List<Address> addresses;

        try
        {
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();


        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return address + " "+city;
    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(RegisterActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }
                break;



            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }



}
