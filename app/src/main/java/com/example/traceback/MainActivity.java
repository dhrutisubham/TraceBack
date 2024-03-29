package com.example.traceback;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login, register;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login= (Button) findViewById((R.id.landingLogin));
        login.setOnClickListener(this);

        register=(Button)findViewById(R.id.landingRegister);
        register.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

            if(v.getId()==R.id.landingLogin)
                startActivity(new Intent(this, loginPage.class));
            if(v.getId()== R.id.landingRegister)
                startActivity(new Intent(this, registerPage.class));

        }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform any cleanup or additional tasks before exiting the app
                finish(); // Close the activity and exit the app
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if(user!= null){
            startActivity(new Intent(this, homefeed.class));
        }

    }

}