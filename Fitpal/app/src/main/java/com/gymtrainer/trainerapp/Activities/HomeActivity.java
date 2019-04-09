package com.gymtrainer.trainerapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.gymtrainer.trainerapp.Adapters.HireAdapter;
import com.gymtrainer.trainerapp.Adapters.HomeAdapter;
import com.gymtrainer.trainerapp.Models.Category;
import com.gymtrainer.trainerapp.Models.Hire;
import com.gymtrainer.trainerapp.Models.Trainer;
import com.gymtrainer.trainerapp.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    HireAdapter hireAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView notfoundTxt;
    Toolbar toolbar;

    ArrayList<Hire> hireList;
    DatabaseReference databaseReferenceTrainer,databaseReferenceHire;
    Trainer trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       init();

    }

    private void init()
    {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        notfoundTxt = (TextView)findViewById(R.id.notfoundTxt);
        progressBar = (ProgressBar)findViewById(R.id.progressBarHiredUsers);
        toolbar = (Toolbar)findViewById(R.id.toolbarHome);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hireList = new ArrayList<>();
        hireAdapter = new HireAdapter(HomeActivity.this,hireList);
        recyclerView.setAdapter(hireAdapter);
        databaseReferenceTrainer = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(firebaseUser.getUid());
        databaseReferenceHire = FirebaseDatabase.getInstance().getReference().child("Hire");
        databaseReferenceTrainer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    trainer = dataSnapshot.getValue(Trainer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReferenceHire.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            hireList.clear();
                            for(DataSnapshot dss:dataSnapshot.getChildren())
                            {
                                String key = dss.getKey();
                                if(trainer!=null)
                                {
                                    if(key.contains(trainer.getTrainerid()))
                                    {
                                        Hire hire = dss.getValue(Hire.class);
                                        hireList.add(hire);
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Trainer is null",Toast.LENGTH_LONG).show();
                                    notfoundTxt.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }


                            // notify data set changed
                            hireAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            notfoundTxt.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                        notfoundTxt.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        },3000);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.menuProfile)
        {
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void openNewActivity(String trainerId, ArrayList<String> hourList, String categoryName, String userId, String date)
    {
        Intent i = new Intent(HomeActivity.this,HiredUsersDetailActivity.class);
        i.putExtra("categoryName",categoryName);
        i.putExtra("userId",userId);
        i.putExtra("date",date);
        i.putStringArrayListExtra("hoursList",hourList);
        startActivity(i);
    }
}
