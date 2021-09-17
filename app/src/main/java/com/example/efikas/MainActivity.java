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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText logEmailEditText,logPassEditText;
    private Button loginButton;
    private TextView signupTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Efikas- Sign in!");

        mAuth=FirebaseAuth.getInstance();

        logEmailEditText=findViewById(R.id.logemailText);
        logPassEditText=findViewById(R.id.logpassText);
        loginButton=findViewById(R.id.loginButton);
        signupTextView= findViewById(R.id.signupButton);
        progressBar=findViewById(R.id.progressbarID);

        signupTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.loginButton:
                userLogin();
                break;

            case R.id.signupButton:
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userLogin() {

        String email=logEmailEditText.getText().toString().trim();
        String password=logPassEditText.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        //checking the validity of the email
        if(email.isEmpty())
        {
            logEmailEditText.setError("Enter an email address");
            logEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            logEmailEditText.setError("Enter a valid email address");
            logEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            logPassEditText.setError("Enter a valid numeric pin of minimum 4 character length!");
            logPassEditText.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            logPassEditText.setError("Enter a valid numeric pin of minimum 6 character length!");
            logPassEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(getApplicationContext(),MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}