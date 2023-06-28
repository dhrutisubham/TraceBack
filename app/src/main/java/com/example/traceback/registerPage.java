package com.example.traceback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class registerPage extends AppCompatActivity implements View.OnClickListener {

    EditText webmail, pw, cnfpw;
    FirebaseAuth auth;
    TextView regPagelogin;
    Button regButton;

    AlertDialog internetDialog;

    FirebaseUser user;

    Uri filepath;
    ProgressBar pb;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = findViewById(R.id.regispage);
        final View yourView = findViewById(R.id.imageView);
        setContentView(R.layout.activity_register_page);


        auth=FirebaseAuth.getInstance();

        pb=(ProgressBar)findViewById(R.id.regPB);
        webmail=(EditText)findViewById(R.id.registerEmail);
        pw=(EditText)findViewById(R.id.registerpw);
        cnfpw=(EditText)findViewById(R.id.registerConfirmpw);

        regPagelogin=(TextView) findViewById(R.id.regisLogin);
        regPagelogin.setPaintFlags(regPagelogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        regPagelogin.setOnClickListener(this);

        regButton=(Button) findViewById(R.id.regisButton);
        regButton.setOnClickListener(this);


//        webmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // Remove the hint when the view has focus
//                    webmail.setHint("");
//                } else {
//                    // Restore the hint when the view loses focus
//                    webmail.setHint("Enter Webmail");
//                }
//            }
//        });
//
//        pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // Remove the hint when the view has focus
//                    pw.setHint("");
//                } else {
//                    // Restore the hint when the view loses focus
//                    pw.setHint("Enter New Password");
//                }
//            }
//        });
//
//        cnfpw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // Remove the hint when the view has focus
//                    cnfpw.setHint("");
//                } else {
//                    // Restore the hint when the view loses focus
//                    cnfpw.setHint("Confirm New Password");
//                }
//            }
//        });
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


        if (v.getId() == R.id.regisLogin) {
            finish();
            startActivity(new Intent(this, loginPage.class));
        } else if (v.getId() == R.id.regisButton) {
//            pb.setVisibility(View.VISIBLE);
            if (validInputs()) {
                userRegister();

//                startActivity(new Intent(this, register2.class));
            } else pb.setVisibility(View.GONE);

        }
    }

    private boolean validInputs(){
        String wbmail=webmail.getText().toString().trim();
        String pswrd=pw.getText().toString().trim();
        String cnfpswrd=cnfpw.getText().toString().trim();
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
        if (cnfpswrd.isEmpty() && !pswrd.isEmpty()) {
            cnfpw.setError("Confirm your Password");
            cnfpw.requestFocus();
            flag=false;
        }

        //checking webmail
        if (!wbmail.endsWith("@iitp.ac.in") || wbmail.startsWith("@iitp.ac.in")) {
            webmail.setError("Use Institute Webmail ID only");
            webmail.requestFocus();
            flag=false;

        }
        

        //checking pword
        if (pswrd.length() < 8) {
            pw.setError("Password should have a minimum of eight characters!");
            pw.requestFocus();
            flag=false;

        }
        if (!pswrd.equals(cnfpswrd)) {
            cnfpw.setError("Passwords do not match");
            pw.requestFocus();
            cnfpw.requestFocus();
            flag=false;

        }
        return flag;

    }
    private void userRegister() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(regButton.getWindowToken(), 0);
        String wbmail=webmail.getText().toString().trim();
        String pswrd=pw.getText().toString().trim();
        auth.createUserWithEmailAndPassword(wbmail, pswrd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pb.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            user=FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(registerPage.this, "Verification Link sent to WebMail", Toast.LENGTH_SHORT).show();
                            finish();
//                            return true;
                            startActivity(new Intent(registerPage.this, register2.class));
                        }
                        else {

                            Exception exception=task.getException();
                            if(exception instanceof FirebaseAuthUserCollisionException){
                                webmail.setError("Account with this webmail already exists!");
                                webmail.requestFocus();
                            }
                            else
                            showInternetDialog();
//                            return false;
                        }

                    }
                });
    }

//    private void upload() {
//
//        webmail=(EditText) findViewById(R.id.registerEmail);
//        pw=(EditText) findViewById(R.id.registerpw);
//        cnfpw=(EditText) findViewById(R.id.registerConfirmpw);
//        pb= (ProgressBar) findViewById(R.id.regPB);
//        user= FirebaseAuth.getInstance().getCurrentUser();
//
//
//
//        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
//        StorageReference uploader= firebaseStorage.getReference("ProfilePicture/Image1"+new Random().nextInt(50));
//        uploader.putFile(filepath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                FirebaseDatabase db= FirebaseDatabase.getInstance();
//                                DatabaseReference root= db.getReference("Users");
//                                dataholder obj= new dataholder( email.getText().toString(), name.getText().toString(), passW.getText().toString(), uri.toString(), phone.getText().toString(), roll.getText().toString(), wp.getText().toString());
//                                root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
//                                name.setText("");
//                                email.setText("");
//                                roll.setText("");
//                                passW.setText("");
//                                phone.setText("");
//                                cpassW.setText("");
//                                wp.setText("");
//                                img.setImageResource(R.drawable.pngfind_com_upload_icon_png_661092);
//                                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
//                                pb.setVisibility(View.GONE);
//
//
//                                user.sendEmailVerification();
//                                Toast.makeText(RegisterUser.this, "Verification Link Sent to Email", Toast.LENGTH_SHORT).show();
//                                mAuth.signOut();
//
//
//                                startActivity(new Intent(RegisterUser.this, MainActivity.class));
//                            }
//                        });
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        pb.setVisibility(View.VISIBLE);
//                    }
//                });
//    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dismiss the dialog if it is showing
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
        }
    }
}