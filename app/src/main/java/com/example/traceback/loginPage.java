package com.example.traceback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity implements View.OnClickListener {


    final int NO_INTERNET=0;
    final int VERIFY_EMAIL=1;
    final int REGISTER_EMAIL=2;
    final int WRONG_PASS=3;


    TextView loginres, forgotPass;
    Button logButton;
    AlertDialog internetDialog;
    FirebaseAuth mAuth;
    EditText webmail, pw;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth=FirebaseAuth.getInstance();

        webmail=findViewById(R.id.loginEmail);
        pw=findViewById(R.id.loginpw);

        loginres = (TextView) findViewById(R.id.loginRegis);
        loginres.setPaintFlags(loginres.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginres.setOnClickListener(this);

        forgotPass = (TextView) findViewById(R.id.forgotPassword);
        forgotPass.setPaintFlags(forgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        forgotPass.setOnClickListener(this);
        
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
            if(validInput()){
                userLogin();
//                finish();
//                startActivity(new Intent(this, homefeed.class));

            }
        }
        else if(v.getId()==R.id.forgotPassword){
            forgotPasswordAction();
        }
    }

    private void forgotPasswordAction() {
    }

    private void userLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(logButton.getWindowToken(), 0);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (!isConnected) {
            showDialogPop(NO_INTERNET);
            return;
        }
//        loading.setVisibility(View.VISIBLE);

        String username=webmail.getText().toString().trim();
        String pass=pw.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    if(!user.isEmailVerified() || user==null){
                        showDialogPop(VERIFY_EMAIL);
                        mAuth.signOut();
                    }
                    else
                    startActivity(new Intent(loginPage.this, homefeed.class));
                }
                else{
                    Exception ex=task.getException();
                    if(ex instanceof FirebaseAuthInvalidUserException) {
                        webmail.requestFocus();

                        showDialogPop(REGISTER_EMAIL);
                    }

                    else if(ex instanceof FirebaseAuthInvalidCredentialsException) {
                        pw.requestFocus();
                        showDialogPop(WRONG_PASS);
                    }

                }
//                loading.setVisibility(View.GONE);
            }
        });

    }

    private boolean validInput() {
        String wbmail=webmail.getText().toString().trim();
        String pswrd=pw.getText().toString().trim();
        boolean flag=true;

        if (wbmail.isEmpty()) {
            webmail.setError("Please enter WebMail ID");
            webmail.requestFocus();
            flag=false;
        }
        if (pswrd.isEmpty()) {
            pw.setError("Choose a Password");
            pw.requestFocus();
            flag=false;
        }


        //checking webmail
        if (!wbmail.endsWith("@iitp.ac.in") || wbmail.startsWith("@iitp.ac.in")) {
            webmail.setError("Use Institute Webmail ID only");
            webmail.requestFocus();
            flag=false;

        }


        //checking pword


        return flag;
    }

    private void showDialogPop(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(code==NO_INTERNET) {

            builder.setTitle("No Internet Connection");
            builder.setMessage("Please check your internet connection and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            internetDialog = builder.create();
            internetDialog.show();
        }
        else if(code==VERIFY_EMAIL){
            builder.setTitle("Verify Webmail");
            builder.setMessage("Please verify your webmail using the link sent to your inbox and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            internetDialog = builder.create();
            internetDialog.show();

        }
        else if(code==REGISTER_EMAIL){
            builder.setTitle("Webmail not registered!");
            builder.setMessage("Please register your webmail and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(loginPage.this, registerPage.class));
                }
            });

            internetDialog = builder.create();
            internetDialog.show();
        }
        else if(code==WRONG_PASS){
            builder.setTitle("Incorrect Password");
            builder.setMessage("The password you have entered is incorrect!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Forgot Password", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    forgotPasswordAction();
                }
            });

            internetDialog = builder.create();
            internetDialog.show();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dismiss the dialog if it is showing
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
        }
    }
}