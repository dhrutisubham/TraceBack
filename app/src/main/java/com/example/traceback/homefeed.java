package com.example.traceback;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.traceback.databinding.ActivityHomefeedBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class homefeed extends AppCompatActivity implements View.OnClickListener {

    private ActivityHomefeedBinding binding;
    Toolbar tb;
    FloatingActionButton addp;

//    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomefeedBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        tb=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        addp=(FloatingActionButton) findViewById(R.id.addPost);
        addp.bringToFront();

        binding.addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(homefeed.this, "Click", Toast.LENGTH_SHORT).show();
                showPictureOptionsPopup();
            }
        });

//        button=(Button)findViewById(R.id.button);
//        binding.button.setOnClickListener(v-> showPictureOptionsPopup());




        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFeed, R.id.placeholder, R.id.searchPost)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_homefeed);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



    private void showPictureOptionsPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addpost_popup);

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

    @Override
    public void onClick(View v) {

    }
}