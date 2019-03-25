package com.gymtrainer.trainerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


public class SignInActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.gymtrainer.trainerapp.R.layout.activity_sign_in);
        editTextEmail = (TextInputEditText) findViewById(com.gymtrainer.trainerapp.R.id.email_ed_login);
        editTextPassword = (TextInputEditText) findViewById(com.gymtrainer.trainerapp.R.id.password_ed_login);
        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar)findViewById(com.gymtrainer.trainerapp.R.id.progressBarLogin);

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
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Logged in...",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(SignInActivity.this,HomeActivity.class);
                                startActivity(intent);
                                finish();
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


}
