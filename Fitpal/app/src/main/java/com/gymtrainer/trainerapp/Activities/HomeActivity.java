package com.gymtrainer.trainerapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.gymtrainer.trainerapp.R;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();

    }

    public void logout(View v)
    {
        if(auth.getCurrentUser()!=null)
        {
            auth.signOut();
            Intent i = new Intent(HomeActivity.this,SignInActivity.class);
            startActivity(i);
            finish();
        }
    }

}
