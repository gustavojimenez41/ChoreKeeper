package com.example.gustavojimenez.chorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button registerButton;
    Button login;
    FirebaseAuth firebaseauth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebaseauth.getInstance();
        //causes a crash
        //working on it
        //user = firebaseauth.getInstance().getCurrentUser();

        //go straight to the home page if the user is already signed in
        if(user!=null)
        {

            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {



                String pass = ((EditText)findViewById(R.id.enterPassword)).getText().toString();
                String mail = ((EditText)findViewById(R.id.enterEmail)).getText().toString();
                signIn(mail, pass);
                user = firebaseauth.getCurrentUser();

                user = firebaseauth.getCurrentUser();

                //if the sign in works, go to home page
                if(user!=null)
                {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    private void signIn(String email, String password)
    {
        firebaseauth = FirebaseAuth.getInstance();

        firebaseauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = firebaseauth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Hello "+user.getDisplayName(),
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Sign In failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}

