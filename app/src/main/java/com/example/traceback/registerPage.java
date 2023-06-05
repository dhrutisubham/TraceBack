package com.example.traceback;

import static com.example.traceback.R.id.regisLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class registerPage extends AppCompatActivity implements View.OnClickListener {

    TextView regPagelogin;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.regispage);
        final View yourView = findViewById(R.id.imageView);
        setContentView(R.layout.activity_register_page);

        regPagelogin=(TextView) findViewById(R.id.regisLogin);
        regPagelogin.setPaintFlags(regPagelogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        regPagelogin.setOnClickListener(this);
//        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                layout.getWindowVisibleDisplayFrame(r);
//                int screenHeight = layout.getRootView().getHeight();
//
//                int keypadHeight = screenHeight - r.bottom;
//
//                if (keypadHeight > screenHeight * 0.15) { // adjust this threshold as needed
//                    yourView.setVisibility(View.GONE);
//                } else {
//                    yourView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.regisLogin){
            finish();
            startActivity(new Intent(this, loginPage.class));
        }
    }
}