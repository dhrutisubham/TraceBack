package com.example.traceback;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class loginPage extends AppCompatActivity implements View.OnClickListener {

    TextView loginres;
    Button logButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginres = (TextView) findViewById(R.id.loginRegis);
        loginres.setPaintFlags(loginres.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginres.setOnClickListener(this);

        logButton = (Button) findViewById(R.id.loginButton);
        logButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.loginRegis){
            finish();
            startActivity(new Intent(this, registerPage.class));
        }
        else if(v.getId()==R.id.loginButton){
            startActivity(new Intent(this, homefeed.class));
        }
    }
}