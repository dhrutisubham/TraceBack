package com.example.traceback;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class loginPage extends AppCompatActivity implements View.OnClickListener {

    TextView loginres;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginres= (TextView) findViewById(R.id.loginRegis);
        loginres.setPaintFlags(loginres.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginres.setOnClickListener(this);
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
    }
}