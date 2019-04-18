package com.gymtrainer.trainerapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gymtrainer.trainerapp.R;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class SignInActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();


    }

    private void init()
    {
        editTextEmail = (TextInputEditText) findViewById(R.id.email_ed_login);
        editTextPassword = (TextInputEditText) findViewById(R.id.password_ed_login);
        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(R.id.progressBarLogin);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers");

        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        if(!EasyPermissions.hasPermissions(this,perms))
        {
            EasyPermissions.requestPermissions(this,"We need Location permission.",123,perms);

        }
    }

    public void forgotPasswordClick(View v) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(SignInActivity.this);

        LinearLayout container = new LinearLayout(SignInActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 100);
        final EditText input = new EditText(SignInActivity.this);
        input.setLayoutParams(lp);
        input.setGravity(android.view.Gravity.TOP|android.view.Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setLines(1);
        input.setMaxLines(1);
        //   input.setText(lastDateValue);
        container.addView(input, lp);

        alert.setMessage("Enter your registered email address.");
        alert.setTitle("Forgot Password");
        alert.setView(container);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

                String entered_email = input.getText().toString();
                if(isValidEmail(entered_email))
                {
                    //    requestForgotPassword(entered_email);
                    auth.sendPasswordResetEmail(entered_email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Email sent. Check your email to reset your password",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignInActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
                }

            }


        });

        alert.show();
    }

    public void loginTrainer(View v)
    {
        progressBar.setVisibility(View.VISIBLE);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();




        if(!email.equals("")&& !password.equals(""))
        {
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //   FirebaseUser user = auth.getCurrentUser();

                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if(firebaseUser.isEmailVerified())
                                {
                                    progressBar.setVisibility(View.GONE);

                                    // CHECK IF trainer data exists or not....
                                    // if trainer data exists then login otherwise print a message that the trainer does not exist.

                                    databaseReference.child(firebaseUser.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Logged in...",Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(SignInActivity.this,HomeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(getApplicationContext(),"A user is already registered with this email address.",Toast.LENGTH_LONG).show();
                                                        auth.signOut();

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(),""+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });



                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Your email is not verified yet. Please verify it first",Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                    progressBar.setVisibility(View.GONE);
                                    recreate();

                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Wrong email/password combination.Try again",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Email or password cannot be empty",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void gotoSignUp(View v)
    {
                Intent i = new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(i);
    }

    public static boolean isValidEmail(CharSequence target){
        if (TextUtils.isEmpty(target)){
            return false;
        }else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

                new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
