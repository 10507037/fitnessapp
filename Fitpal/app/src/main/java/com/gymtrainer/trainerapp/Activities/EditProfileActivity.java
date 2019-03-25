package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.Models.Category;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    String[] listCategoryItems,listHoursItems;
    boolean[] checkedCategoryItems,checkedHourItems;
    Toolbar toolbar;
    EditText email_ed,name_ed,cellnumber_ed,address_ed,city_ed;
    String email,name,cellnumber,address,city;
    ArrayList<String> categoriesList;
    ProgressBar progressBarEditProfile;
    Button buttonUpdate;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    TextView textViewCategories,textViewWorkingHrs;
    ArrayList<Integer> mCategoryItem = new ArrayList<>();
    List<String> selectedCategoriesList,selectedHoursList;
    DatabaseReference databaseReferenceCategories,databaseReferenceTrainers,databaseReferenceWorkingHrs;

    ArrayList<String> allcategoryLIST,workingHrsListTrainer;
    ArrayList<Integer> mHourItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gymtrainer.trainerapp.R.layout.activity_edit_profile);
        init();

        setListeners();


        getIntentData();

        setData();

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
                checkedHourItems = new boolean[listHoursItems.length];
                displayDialogWorkingHrs();
            }
        });

        textViewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferenceCategories.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allcategoryLIST.clear();

                        for(DataSnapshot dss:dataSnapshot.getChildren())
                        {
                            Category category = dss.getValue(Category.class);
                            allcategoryLIST.add(category.getCategoryName());
                        }

                        listCategoryItems = new String[allcategoryLIST.size()];
                        for (int i = 0; i < allcategoryLIST.size(); i++)
                        {      // convert array list to string array
                            listCategoryItems[i] = allcategoryLIST.get(i);
                        }

                        checkedCategoryItems = new boolean[listCategoryItems.length];

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                displayDialogCategories();



            }
        });
    }

    private void init()
    {
        toolbar = (Toolbar)findViewById(com.gymtrainer.trainerapp.R.id.toolbarEditProfile);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        categoriesList = new ArrayList<>();
        workingHrsListTrainer = new ArrayList<>();
        listHoursItems = new String[]{"8 AM","9 AM","10 AM","11 AM","12 PM","01 PM","02 PM","03 PM","04 PM","05 PM"};
        allcategoryLIST = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        buttonUpdate = (Button)findViewById(com.gymtrainer.trainerapp.R.id.buttonUpdate);

        databaseReferenceCategories = FirebaseDatabase.getInstance().getReference().child("Categories");
        databaseReferenceTrainers = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid());
        databaseReferenceWorkingHrs = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid()).child("WorkingHrs");


        email_ed = (EditText)findViewById(com.gymtrainer.trainerapp.R.id.emailTrainer);
        name_ed = (EditText)findViewById(com.gymtrainer.trainerapp.R.id.nameTrainer);
        cellnumber_ed = (EditText)findViewById(com.gymtrainer.trainerapp.R.id.cellnumberTrainer);
        address_ed = (EditText)findViewById(com.gymtrainer.trainerapp.R.id.addressTrainer);
        textViewWorkingHrs = (TextView)findViewById(com.gymtrainer.trainerapp.R.id.workingHrsTxtView);
        selectedCategoriesList = new ArrayList<>();
        selectedHoursList = new ArrayList<>();
        city_ed = (EditText)findViewById(com.gymtrainer.trainerapp.R.id.cityTrainer);
        progressBarEditProfile = (ProgressBar)findViewById(com.gymtrainer.trainerapp.R.id.progressBarEditProfile);
        textViewCategories = (TextView)findViewById(com.gymtrainer.trainerapp.R.id.categoriesTxtView);
    }

    private void displayDialogWorkingHrs()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                    textViewWorkingHrs.setText("Choose your available hours");
                }
                else
                {
                    selectedHoursList.clear();
                    textViewWorkingHrs.setText(item);

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
                    selectedHoursList.clear();
                    textViewWorkingHrs.setText("Choose your available hours");
                }
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
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

        if(selectedCategoriesList.size()==0)
        {
            Toast.makeText(EditProfileActivity.this, "You must select atleast one category", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedHoursList.size() == 0)
        {
            Toast.makeText(EditProfileActivity.this, "You must select atleast one hour", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {

            updateTrainer();

        }
    }

    private void updateTrainer()
    {
        databaseReferenceTrainers.child("name").setValue(name_ed.getText().toString());
        databaseReferenceTrainers.child("phonenumber").setValue(cellnumber_ed.getText().toString());
        databaseReferenceTrainers.child("address").setValue(address_ed.getText().toString());
        databaseReferenceTrainers.child("city").setValue(city_ed.getText().toString());

        if(selectedCategoriesList.size()>0 && selectedHoursList.size()>0)
        {
            databaseReferenceTrainers.child("Categories").setValue(selectedCategoriesList);
            databaseReferenceTrainers.child("WorkingHrs").setValue(selectedHoursList);
        }

        else
        {
            databaseReferenceTrainers.child("Categories").setValue(categoriesList);
            databaseReferenceTrainers.child("WorkingHrs").setValue(workingHrsListTrainer);
        }

        Toast.makeText(getApplicationContext(),"User profile updated successfully",Toast.LENGTH_LONG).show();
        finish();
        Intent i = new Intent(EditProfileActivity.this,ProfileActivity.class);
        startActivity(i);


    }

    private void displayDialogCategories()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                    textViewCategories.setText("Choose categories");
                }
                else
                {
                    textViewCategories.setText(item);

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
                    selectedCategoriesList.clear();
                    textViewCategories.setText("Choose categories");
                }
            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void setData()
    {
        email_ed.setText(email);
        email_ed.setEnabled(false);
        name_ed.setText(name);
        cellnumber_ed.setText(cellnumber);
        address_ed.setText(address);
        city_ed.setText(city);

        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<categoriesList.size();i++)
        {
            stringBuilder.append(categoriesList.get(i)+ ",");
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

    private void getIntentData()
    {
            if(getIntent()!=null)
            {
                email = getIntent().getStringExtra("email");
                name = getIntent().getStringExtra("name");
                cellnumber = getIntent().getStringExtra("cellnumber");
                address = getIntent().getStringExtra("address");
                city = getIntent().getStringExtra("city");
                categoriesList = getIntent().getStringArrayListExtra("categoriesList");
                workingHrsListTrainer = getIntent().getStringArrayListExtra("workinghrs");
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
