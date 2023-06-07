package com.example.traceback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class register2 extends AppCompatActivity {

    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        ImageView photo=(ImageView) findViewById(R.id.regis2pfp);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureOptionsPopup();
            }
        });
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
                dialog.dismiss();
            }
        });

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle upload picture option
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}