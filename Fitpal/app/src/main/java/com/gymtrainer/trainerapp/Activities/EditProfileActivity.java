package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.Models.Category;
import com.gymtrainer.trainerapp.Models.Trainer;
import com.gymtrainer.trainerapp.Models.WorkingHrs;
import com.gymtrainer.trainerapp.R;
import com.gymtrainer.trainerapp.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText email_ed,name_ed,cellnumber_ed,address_ed,city_ed,about_ed,experience_ed;

    ArrayList<String> trainerCategoryListDb;
    ProgressBar progressBarEditProfile;
    Button buttonUpdate;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    Trainer trainer;
    TextView textViewCategories,textViewWorkingHrs;

    DatabaseReference databaseReferenceCategories,databaseReferenceTrainers,databaseReferenceWorkingHrs;

    ArrayList<String> workingHrsListTrainer;

    RadioGroup radioGroupGender;
    RadioButton radioGenderButton;
    ArrayList<Category> categoriesList = new ArrayList<>();
    ArrayList<WorkingHrs> workingHrsList = new ArrayList<>();

    String[] hourItemNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        setListeners();
        getIntentData();
        getAllCategories();
        setData();

    }

    private void getAllCategories() {
        databaseReferenceCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dss:dataSnapshot.getChildren())
                {
                    Category category = dss.getValue(Category.class);
                    if(trainerCategoryListDb.contains(category.getCategoryName()))
                        category.setSelected(true);
                        categoriesList.add(category);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init()
    {
        workingHrsList = Constants.getHoursList();
        radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        toolbar = (Toolbar)findViewById(R.id.toolbarEditProfile);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        trainerCategoryListDb = new ArrayList<>();
        workingHrsListTrainer = new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
        hourItemNew = Constants.hourItems;






        databaseReferenceCategories = FirebaseDatabase.getInstance().getReference().child("Categories");
        databaseReferenceTrainers = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid());
        databaseReferenceWorkingHrs = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid()).child("WorkingHrs");


        email_ed = findViewById(R.id.edEmail);
        name_ed = findViewById(R.id.edName);
        cellnumber_ed = findViewById(R.id.edPhoneNumber);
        address_ed = findViewById(R.id.edAddress);
        textViewWorkingHrs = (TextView)findViewById(R.id.workingHrsTxtView);

        city_ed = findViewById(R.id.edCity);
        about_ed = findViewById(R.id.edAbout);
        experience_ed = findViewById(R.id.edExperience);
        progressBarEditProfile = (ProgressBar)findViewById(R.id.progressBarEditProfile);
        textViewCategories = (TextView)findViewById(R.id.categoriesTxtView);

    }

    private void setListeners()
    {
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        textViewWorkingHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Toast.makeText(getApplicationContext(),"You cannot change your working hours right now. Work is going on.",Toast.LENGTH_LONG).show();
                displayDialogWorkingHrs();
            }
        });

        textViewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogCategories();

            }
        });
    }



    private void getIntentData()
    {
        if(getIntent()!=null)
        {
            trainer = (Trainer)getIntent().getSerializableExtra("trainerObj");
            trainerCategoryListDb = getIntent().getStringArrayListExtra("categoriesList");
            workingHrsListTrainer = getIntent().getStringArrayListExtra("workinghrs");
        }
    }
    private void setData()
    {


        email_ed.setText(trainer.getEmail());
        email_ed.setEnabled(false);
        name_ed.setText(trainer.getName());
        cellnumber_ed.setText(trainer.getPhonenumber());
        address_ed.setText(trainer.getAddress());
        city_ed.setText(trainer.getCity());
        about_ed.setText(trainer.getAbout());
        experience_ed.setText(trainer.getExperience());

        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<trainerCategoryListDb.size();i++)
        {
            stringBuilder.append(trainerCategoryListDb.get(i)+ ",");
        }


        if(stringBuilder.length()>0)
        {
            textViewCategories.setText(stringBuilder.toString());
        }


        StringBuilder workingHrsBuilder = new StringBuilder();

        for(int i=0;i<workingHrsListTrainer.size();i++)
        {
            workingHrsBuilder.append(workingHrsListTrainer.get(i)+ ",");
        }


        if(workingHrsBuilder.length()>0)
        {
            textViewWorkingHrs.setText(workingHrsBuilder.toString());
        }


    }




    private void updateProfile()
    {
        if(name_ed.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Name cannot be blank",Toast.LENGTH_LONG).show();
        }

        if(cellnumber_ed.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this, "Phone number cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(address_ed.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this, "Address cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if(city_ed.getText().toString().equals(""))
        {
            Toast.makeText(EditProfileActivity.this, "City cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(categoriesList.size()==0)
        {
            Toast.makeText(EditProfileActivity.this, "You must select atleast one category", Toast.LENGTH_SHORT).show();
            return;
        }

//        if(selectedHoursList.size() == 0)
//        {
//            Toast.makeText(EditProfileActivity.this, "You must select atleast one hour", Toast.LENGTH_SHORT).show();
//            return;
//        }
        else
        {


            updateTrainer();

        }
    }
    

    private void displayDialogWorkingHrs()
    {

        final boolean[] itemHourSelected = new boolean[workingHrsList.size()];

        for(int i=0;i<workingHrsList.size();i++)
        {
            if(workingHrsListTrainer.contains(workingHrsList.get(i).getHourName()))
            {
//                WorkingHrs workingHrs = new WorkingHrs(Constants.getHoursList().get(i).getHourName(),Constants.getHoursList().get(i).isSelected());
//                workingHrs.setSelected(true);
//                workingHrsList.add(new WorkingHrs( Constants.getHoursList().get(i).getHourName(), Constants.getHoursList().get(i).isSelected()));
                workingHrsList.get(i).setSelected(true);
            }
        }


        for(int i=0;i<workingHrsList.size();i++)
        {
            itemHourSelected[i] = workingHrsList.get(i).isSelected();
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Choose Working Hours");
        builder.setMultiChoiceItems(Constants.hourItems, itemHourSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    itemHourSelected[which] = isChecked;
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for(int i=0; i<itemHourSelected.length;i++){
                    workingHrsList.get(i).setSelected(itemHourSelected[i]);
                }

                if(itemHourSelected.length==0)
                {
                    textViewWorkingHrs.setText("Choose Working Hours");
                }

                String workText = "";
                workingHrsListTrainer.clear();

                for (WorkingHrs workingHrs:workingHrsList)
                {
                    if(workingHrs.isSelected()){
//                        textViewCategories.setText(cat.getCategoryName());
                        if(workText.equals("")) {
                            workText = workingHrs.getHourName();
                        }else {
                            workText = workText + "," + workingHrs.getHourName();
                        }
                        workingHrsListTrainer.add(workingHrs.getHourName());
                    }
                }

                textViewWorkingHrs.setText(workText);

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
                workingHrsList.clear();
                    textViewWorkingHrs.setText("Choose your available hours");

            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void displayDialogCategories()
    {

        String[] itemsName = new String[categoriesList.size()];
        final boolean[] itemsSelected = new boolean[categoriesList.size()];

        for(int i=0;i<categoriesList.size();i++)
        {
            itemsName[i] = categoriesList.get(i).getCategoryName();
            itemsSelected[i]= categoriesList.get(i).isSelected();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Choose categories");
        builder.setMultiChoiceItems(itemsName, itemsSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                itemsSelected[which] = isChecked;
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                for(int i=0; i<itemsSelected.length;i++){
                    categoriesList.get(i).setSelected(itemsSelected[i]);
                }

                if(categoriesList.size()==0)
                {
                    textViewCategories.setText("Choose categories");
                }


                String catText = "";
                for (Category cat:categoriesList)
                {
                    if(cat.isSelected()){
//                        textViewCategories.setText(cat.getCategoryName());
                        if(catText.equals("")) {
                            catText = cat.getCategoryName();
                        }else {
                            catText = catText + "," + cat.getCategoryName();
                        }
                    }
                }

                textViewCategories.setText(catText);

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
                categoriesList.clear();
                    textViewCategories.setText("Choose categories");

            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void updateTrainer()
    {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        radioGenderButton = (RadioButton) findViewById(selectedId);

        databaseReferenceTrainers.child("name").setValue(name_ed.getText().toString());
        databaseReferenceTrainers.child("phonenumber").setValue(cellnumber_ed.getText().toString());
        databaseReferenceTrainers.child("address").setValue(address_ed.getText().toString());
        databaseReferenceTrainers.child("city").setValue(city_ed.getText().toString());
        databaseReferenceTrainers.child("gender").setValue(radioGenderButton.getText().toString());
        databaseReferenceTrainers.child("about").setValue(about_ed.getText().toString());
        databaseReferenceTrainers.child("experience").setValue(experience_ed.getText().toString());

      //  databaseReferenceTrainers.child("WorkingHrs").setValue(selectedHoursList);


        ArrayList<String> categoryNames = new ArrayList<>();
        ArrayList<String> hourNamesList = new ArrayList<>();
        for(int i=0;i<categoriesList.size();i++)
        {
            if(categoriesList.get(i).isSelected())
            {
                categoryNames.add(categoriesList.get(i).getCategoryName());
            }
        }

        for(WorkingHrs workingHrs:workingHrsList)
        {
            if(workingHrs.isSelected())
            {
                hourNamesList.add(workingHrs.getHourName());
            }
        }

        databaseReferenceTrainers.child("Categories").setValue(categoryNames);
        databaseReferenceTrainers.child("WorkingHrs").setValue(hourNamesList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"User profile updated successfully",Toast.LENGTH_LONG).show();
                                finish();
                                Intent i = new Intent(EditProfileActivity.this,ProfileActivity.class);
                                startActivity(i);
                            }
                    }
                });







    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
            Intent i = new Intent(EditProfileActivity.this,ProfileActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
