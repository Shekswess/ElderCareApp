package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {


    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextType, editTextNumber;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextType = (EditText) findViewById(R.id.type);
        editTextNumber = (EditText) findViewById(R.id.number);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
            case R.id.back:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void registerUser() {

        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String number = editTextNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required !");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email Address is required !");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Valid Email Address is required !");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required !");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password is too short !");
            editTextPassword.requestFocus();
            return;
        }

        if (type.isEmpty()) {
            editTextType.setError("Type is required !");
            editTextType.requestFocus();
            return;
        }


       if(!type.equals("Volunteer")&&!type.equals("Elder")) {
         editTextType.setError("Valid Type is required !");
         editTextType.requestFocus();
         return;
        }

        if (number.isEmpty()) {
            editTextNumber.setError("Phone Number is required");
            editTextNumber.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(number).matches()) {
            editTextNumber.setError("Valid Phone Number is required");
            editTextNumber.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful())
                         {
                             User user = new User(fullName,email,type,number);
                             FirebaseDatabase.getInstance().getReference("Users")
                                     .child(FirebaseAuth.getInstance().getUid())
                                     .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                         progressBar.setVisibility(View.GONE);
                                         startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                     }
                                     else
                                     {
                                         Toast.makeText(RegisterUser.this, "User has not been registered successfully, try again !", Toast.LENGTH_LONG).show();
                                         progressBar.setVisibility(View.GONE);
                                     }
                                 }
                             });
                         }
                         else {
                             Toast.makeText(RegisterUser.this, "User has not been registered successfully, try again !", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);
                         }
                    }
                });

    }
}