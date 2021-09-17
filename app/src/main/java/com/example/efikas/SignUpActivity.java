package com.example.efikas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText regmailText,regpassText;
    private Button registerButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Efikas- Sign Up!");
        mAuth = FirebaseAuth.getInstance();
        regmailText=findViewById(R.id.regemailText);
        regpassText=findViewById(R.id.regpassText);
        registerButton=findViewById(R.id.registerButton);
        loginTextView= findViewById(R.id.loginTextButton);
        progressBar=findViewById(R.id.progressbarID2);


        loginTextView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.registerButton:
                userRegister();
                break;

            case R.id.loginTextButton:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegister() {
        String email=regmailText.getText().toString().trim();
        String password=regpassText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        //checking the validity of the email
        if(email.isEmpty())
        {
            regmailText.setError("Enter an email address");
            regmailText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            regmailText.setError("Enter a valid email address");
            regmailText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            regpassText.setError("Enter a valid numeric pin of minimum 4 character length!");
            regpassText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            regpassText.setError("Enter a valid numeric pin of minimum 6 character length!");
            regpassText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Registration Complete! Sign in to Continue.",Toast.LENGTH_SHORT).show();
                    regmailText.setText("");
                    regpassText.setText("");
                } else {
                   if(task.getException() instanceof FirebaseAuthUserCollisionException){
                       Toast.makeText(getApplicationContext(),"User/E-mail Already exists in the directory!",Toast.LENGTH_SHORT).show();
                   }
                   else{
                       Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });
    }
}