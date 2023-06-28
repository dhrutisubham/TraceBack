package com.example.traceback;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class register2 extends AppCompatActivity {

    ProgressBar pb;

    AlertDialog internetDialog;

    final int CAMERA_REQ_CODE = 100, CAM_PERM = 101;
    ImageView photo;
    boolean dpflag;
    Uri filepath, imageUri;
    Bitmap bitmap;
    ActivityResultLauncher<String> imagePickerLauncher;
    ActivityResultLauncher<Uri> imageCaptureLauncher;

    EditText nameField, phoneField;

    Button updateBtn;

    FirebaseAuth auth;
    FirebaseUser user;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setContentView(R.layout.activity_register2);
        pb = (ProgressBar) findViewById(R.id.regis2pb);

        ImageView photo = (ImageView) findViewById(R.id.regis2pfp);

        updateBtn = (Button) findViewById(R.id.regisupdate);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=validDetails();
                if(flag) {
                     {
                        updateDetails();
                    }
                    {
//                        Toast.makeText(register2.this, "failed", Toast.LENGTH_SHORT).show();
//                        user=auth.getCurrentUser();
//                        user.delete();
//                        finish();
                    }
                }
            }
        });

        nameField = findViewById(R.id.regis2name);
        phoneField = findViewById(R.id.regis2phone);
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            // Set the image on your view
//                        ImageView imageView = findViewById(R.id.imageView);
                            photo.setImageURI(result);
                        }
                    }
                });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureOptionsPopup();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        user= auth.getCurrentUser();
        user.delete();
    }

    private void updateDetails() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(updateBtn.getWindowToken(), 0);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (!isConnected) {
            showInternetDialog();
            return;
        }
//        pb.setVisibility(View.VISIBLE);
        photo = findViewById(R.id.regis2pfp);

        String name = nameField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String email = user.getEmail().toString().trim();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference uploader = firebaseStorage.getReference().child("ProfilePicture");

        //processing pfp
        String filename = phone + ".jpg";
        Drawable drawable = photo.getDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawable instanceof VectorDrawable) {
                // Convert VectorDrawable to Bitmap
                VectorDrawable vectorDrawable = (VectorDrawable) drawable;
                bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                        vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                vectorDrawable.draw(canvas);
            }else{
            BitmapDrawable bitmapDrawable = (BitmapDrawable) photo.getDrawable();
            bitmap = bitmapDrawable.getBitmap();}
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        //uploading
        StorageReference imageRef = uploader.child(filename);
        UploadTask uploadTask = imageRef.putBytes(imageData);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String un="";
                        for(int i=0; i<email.length(); i++){
                            if(email.charAt(i)=='@') break;
                            un+=email.charAt(i);
                        }
                        filepath = uri;
                        FirebaseDatabase db = FirebaseDatabase.getInstance("https://traceback-126c6-default-rtdb.asia-southeast1.firebasedatabase.app");
                        DatabaseReference dr = db.getReference("Users");
                        User obj = new User(email, name, phone, filepath.toString().trim());
                        dr.child(un).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        nameField.setText("");
                                        phoneField.setText("");
//                        photo.setImageResource(R.drawable.pfp);

                                        pb.setVisibility(View.GONE);
                                        Toast.makeText(register2.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        auth.signOut();
                                        startActivity(new Intent(register2.this, loginPage.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        user.delete();

                                        Toast.makeText(register2.this, "Failed", Toast.LENGTH_SHORT).show();
                                        pb.setVisibility(View.GONE);

                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register2.this, "Image Uploading Failed", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        pb.setVisibility(View.VISIBLE);
                    }
                });





    }
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

    private void showPictureOptionsPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.regis_popup);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setGravity(Gravity.BOTTOM);
            window.setDimAmount(0.75f); // Adjust the dim amount as desired
        }

        Button btnTakePicture = dialog.findViewById(R.id.popTake);
        Button btnUploadPicture = dialog.findViewById(R.id.popUpload);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle take picture option
                captureImage();
                dialog.dismiss();
            }
        });

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void choosePic() {
        Dexter.withContext(register2.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        imagePickerLauncher.launch("image/*");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getApplicationContext(), "Please Provide Permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void captureImage() {


        if (ContextCompat.checkSelfPermission(register2.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAM_PERM);
//            captureImage();
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_PERM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                captureImage();
            } else {

                Toast.makeText(this, "Please grant camera permissions to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            dpflag=true;
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                photo.setImageBitmap(bitmap);
            } catch (Exception ex) {

            }
        }
        if (requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK) {
            try {
                photo = (ImageView) findViewById(R.id.regis2pfp);
                bitmap = (Bitmap) (data.getExtras().get("data"));
//                if(photo!=null)
                photo.setImageBitmap(bitmap);
//                else
//                    Toast.makeText(this, "Failed1", Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

            }
        }


    }

    boolean validDetails(){
        boolean flag=true;
        String phone=phoneField.getText().toString().trim();
        String name=nameField.getText().toString().trim();
        if(name.isEmpty()) {
            nameField.setError("Please enter your Name");
            nameField.requestFocus();
            return false;
//            flag=true;
        }
        if(phone.length()!=10) flag=false;
        if(flag) {
            for (int i = 0; i < phone.length(); i++) {
                if (!Character.isDigit(phone.charAt(i))) {
                    flag = false;

                    break;
                }
            }
        }
        if(!flag){
            phoneField.setError("Please enter a Valid Phone Number");
            phoneField.requestFocus();
        }
        return flag;
    }
}