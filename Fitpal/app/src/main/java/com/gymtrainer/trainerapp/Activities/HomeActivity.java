package com.gymtrainer.trainerapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.gymtrainer.trainerapp.Adapters.HomeAdapter;
import com.gymtrainer.trainerapp.Models.Category;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    Toolbar toolbar;

    DatabaseReference databaseReferenceCategories;
    ArrayList<Category> arrayListCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gymtrainer.trainerapp.R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        toolbar = (Toolbar)findViewById(com.gymtrainer.trainerapp.R.id.toolbarHome);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView)findViewById(com.gymtrainer.trainerapp.R.id.recyclerViewHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    private void setRecyclerView()
    {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.gymtrainer.trainerapp.R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == com.gymtrainer.trainerapp.R.id.menuProfile)
        {
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}
