package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.Models.Trainer;
import com.gymtrainer.trainerapp.Models.User;
import com.gymtrainer.trainerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HiredUsersDetailActivity extends AppCompatActivity {

    CircleImageView hiredTrainerImgView;
    TextView hiredTrainerName,hiredTrainerEmail,hiredTrainerGender,hiredTrainerPhoneNumber,
            hiredTrainerAddress,hiredTrainerHours,hiredTrainerDate,hiredTrainerRate,hiredTrainerCategory;
    ArrayList<String> hoursList;
    DatabaseReference databaseReferenceUser,databaseReferenceHire;
    String categoryName,userId,date;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hired_users_detail);
        init();
        getData();
        setTrainerData();
    }


    private void init()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbarHiredTrainerDetail);
        hiredTrainerImgView = (CircleImageView)findViewById(R.id.hiredTrainerImg);
        hiredTrainerName = (TextView)findViewById(R.id.hiredTrainerName);
        hiredTrainerEmail = (TextView)findViewById(R.id.hiredTrainerEmail);
        hiredTrainerGender = (TextView)findViewById(R.id.hiredTrainerGender);
        hiredTrainerPhoneNumber = (TextView)findViewById(R.id.hiredTrainerPhoneNumber);
        hiredTrainerAddress = (TextView)findViewById(R.id.hiredTrainerAddress);
        hiredTrainerHours = (TextView)findViewById(R.id.hiredTrainerHours);
        hiredTrainerDate = (TextView)findViewById(R.id.hiredTrainerDate);
        hiredTrainerRate = (TextView)findViewById(R.id.hiredTrainerRate);
        hiredTrainerCategory = (TextView)findViewById(R.id.hiredTrainerCategory);
        hoursList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Users");
        databaseReferenceHire = FirebaseDatabase.getInstance().getReference().child("Hire");

    }
    private void setTrainerData()
    {
        databaseReferenceUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null)
                    {
                        hiredTrainerName.setText(user.getName());
                        hiredTrainerEmail.setText(user.getEmail());
                        hiredTrainerGender.setText(user.getGender());
                        hiredTrainerPhoneNumber.setText(user.getPhonenumber());
                        hiredTrainerAddress.setText(user.getAddress());
                        hiredTrainerDate.setText(date);
                        hiredTrainerCategory.setText(categoryName);
                  //      hiredTrainerRate.setText(user.getRate()+ "$");
                        toolbar.setTitle(user.getName());
                        setSupportActionBar(toolbar);
                        if (getSupportActionBar() != null){

                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setDisplayShowHomeEnabled(true);
                        }
                        Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_man).into(hiredTrainerImgView);
                        StringBuilder stringBuilder = new StringBuilder();


                        for(int i=0;i<hoursList.size();i++)
                        {
                            stringBuilder.append(hoursList.get(i) + ",");
                        }

                        if(stringBuilder.length()>0)
                        {
                            hiredTrainerHours.setText(stringBuilder.toString());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    private void getData()
    {

        userId = getIntent().getStringExtra("userId");
        date = getIntent().getStringExtra("date");
        categoryName = getIntent().getStringExtra("categoryName");
        hoursList = getIntent().getStringArrayListExtra("hoursList");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




}
