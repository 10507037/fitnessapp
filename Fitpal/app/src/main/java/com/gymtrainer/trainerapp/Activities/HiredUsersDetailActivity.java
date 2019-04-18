package com.gymtrainer.trainerapp.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.Models.User;
import com.gymtrainer.trainerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HiredUsersDetailActivity extends AppCompatActivity {

    CircleImageView hiredUserImgView;
    TextView hiredUserName, hiredUserEmail, hiredUserGender, hiredUserPhoneNumber,
            hiredUserAddress, hiredUserHours, hiredUserDate, hiredUserCategory;
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
        setUserData();
    }


    private void init()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbarHiredUserDetail);
        hiredUserImgView = (CircleImageView)findViewById(R.id.hiredUserImg);
        hiredUserName = (TextView)findViewById(R.id.hiredUserName);
        hiredUserEmail = (TextView)findViewById(R.id.hiredUserEmail);
        hiredUserGender = (TextView)findViewById(R.id.hiredUserGender);
        hiredUserPhoneNumber = (TextView)findViewById(R.id.hiredUserPhoneNumber);
        hiredUserAddress = (TextView)findViewById(R.id.hiredUserAddress);
        hiredUserHours = (TextView)findViewById(R.id.hiredUserHours);
        hiredUserDate = (TextView)findViewById(R.id.hiredUserDate);
        hiredUserCategory = (TextView)findViewById(R.id.hiredUserCategory);
        hoursList = new ArrayList<>();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child("Users");
        databaseReferenceHire = FirebaseDatabase.getInstance().getReference().child("Hire");

    }




    private void setUserData()
    {
        databaseReferenceUser.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null)
                    {
                        hiredUserName.setText(user.getName());
                        hiredUserEmail.setText(user.getEmail());
                        hiredUserGender.setText(user.getGender());
                        hiredUserPhoneNumber.setText(user.getPhonenumber());
                        hiredUserAddress.setText(user.getAddress());
                        hiredUserDate.setText(date);
                        hiredUserCategory.setText(categoryName);
                        toolbar.setTitle(user.getName());
                        setSupportActionBar(toolbar);
                        if (getSupportActionBar() != null){

                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setDisplayShowHomeEnabled(true);
                        }
                        Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.ic_launcher_man).into(hiredUserImgView);
                        StringBuilder stringBuilder = new StringBuilder();


                        for(int i=0;i<hoursList.size();i++)
                        {
                            stringBuilder.append(hoursList.get(i) + ",");
                        }

                        if(stringBuilder.length()>0)
                        {
                            hiredUserHours.setText(stringBuilder.toString());
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
            // getting data from previous activity

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
