package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gymtrainer.trainerapp.Models.Trainer;
import com.gymtrainer.trainerapp.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    ArrayList<String> arrayListCategories,arrayListWorkingHrs;
    TextView textViewName,textViewEmail,textViewCellNumber,textViewAddress,textViewCity,textViewGender,textViewCategories,textViewWorkingHrs;
    CircleImageView trainerImgView;
    StorageReference firebaseStorageReference;
    DatabaseReference databaseReferenceTrainers;
    DatabaseReference databaseReferenceCategories,databaseReferenceWorkingHrs;
    Button logOutbutton;
    Trainer trainer;
    Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        setListeners();


    }

    private void setListeners()
    {
        trainerImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, 14);
            }
        });



        trainerImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, 14);
            }
        });


        databaseReferenceTrainers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    trainer = dataSnapshot.getValue(Trainer.class);
                    if(trainer!=null)
                    {
                        textViewName.setText(trainer.getName());
                        textViewEmail.setText(trainer.getEmail());
                        textViewCellNumber.setText(trainer.getPhonenumber());
                        textViewAddress.setText(trainer.getAddress());
                        textViewGender.setText(trainer.getGender());
                        textViewCity.setText(trainer.getCity());
                        Picasso.get().load(trainer.getImageUrl()).placeholder(R.drawable.ic_launcher_man).into(trainerImgView);

                        databaseReferenceCategories = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child("Trainers").child(trainer.getTrainerid()).child("Categories");

                        databaseReferenceWorkingHrs = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child("Trainers").child(trainer.getTrainerid()).child("WorkingHrs");

                        fetchCategories();
                        fetchWorkingHrs();


                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Could not fetch profile due to network error. Try again.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Could not fetch profile due to network error. Try again.",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbarProfile);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        textViewName = (TextView)findViewById(R.id.nameTrainerTxt);
        textViewEmail = (TextView)findViewById(R.id.emailTrainerTxt);
        logOutbutton = (Button)findViewById(R.id.buttonLogout);
        logOutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        textViewCellNumber = (TextView)findViewById(R.id.cellNumberTrainerTxt);
        textViewAddress = (TextView)findViewById(R.id.addressTrainerTxt);
        textViewCity = (TextView)findViewById(R.id.cityTrainerTxt);
        textViewGender = (TextView)findViewById(R.id.genderTrainerTxt);
        trainerImgView = (CircleImageView)findViewById(R.id.trainerImgView);
        textViewCategories = (TextView)findViewById(R.id.categoriesTrainerTxt);
        textViewWorkingHrs = (TextView)findViewById(R.id.workingHrsTrainerTxt);
        progressBar  = (ProgressBar)findViewById(R.id.progressBarProfile);
        arrayListCategories = new ArrayList<>();
        arrayListWorkingHrs = new ArrayList<>();

        firebaseStorageReference = FirebaseStorage.getInstance().getReference().child("TrainerProfileImages");
        databaseReferenceTrainers = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        else if(item.getItemId() == R.id.menuEdit)
        {
            Intent i = new Intent(ProfileActivity.this,EditProfileActivity.class);
            if(trainer!=null)
            {
                i.putExtra("trainerObj",trainer);
                i.putStringArrayListExtra("categoriesList",arrayListCategories);
                i.putStringArrayListExtra("workinghrs",arrayListWorkingHrs);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Could not fetch user profile.",Toast.LENGTH_LONG).show();
            }





        }
        return super.onOptionsItemSelected(item);
    }

    public void logout()
    {
        if(auth.getCurrentUser()!=null)
        {
            auth.signOut();
            Intent i = new Intent(ProfileActivity.this,SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 14 && resultCode==RESULT_OK && data!=null)
        {
            Uri uri = data.getData();

            CropImage.activity(uri).start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.d("result_code",String.valueOf(resultCode));


            if (resultCode ==RESULT_OK) {
                resultUri = result.getUri();
                trainerImgView.setImageURI(resultUri);

                // now upload the image to firebase storage
                progressBar.setVisibility(View.VISIBLE);
                savePicture();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void savePicture()
    {
        final StorageReference filePath = firebaseStorageReference.child(firebaseUser.getUid() + ".jpg");
        filePath.putFile(resultUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                databaseReferenceTrainers.child("imageUrl").setValue(uri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //     putAdminCircle();
                                                //    dialog.dismiss();
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(),"Profile image updated successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        });



                            }
                        });



                    }
                });
    }


    private void fetchCategories()
    {
        databaseReferenceCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListCategories.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot dss:dataSnapshot.getChildren())
                    {
                        arrayListCategories.add(dss.getValue(String.class));
                    }

                    StringBuilder builder = new StringBuilder();

                    for(int i=0;i<arrayListCategories.size();i++)
                    {
                        builder.append(arrayListCategories.get(i)+ ",");
                    }


                    if(builder.length()>0)
                    {
                        textViewCategories.setText(builder.toString());
                    }


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Could not fetch profile properly.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchWorkingHrs()
    {
        databaseReferenceWorkingHrs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListWorkingHrs.clear();
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot dss:dataSnapshot.getChildren())
                    {
                        arrayListWorkingHrs.add(dss.getValue(String.class));
                    }


                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0;i<arrayListWorkingHrs.size();i++)
                    {
                        stringBuilder.append(arrayListWorkingHrs.get(i)+ ",");
                    }


                    if(stringBuilder.length()>0)
                    {
                        textViewWorkingHrs.setText(stringBuilder.toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
