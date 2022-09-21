package com.aniketproject.medsist.doctor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aniketproject.medsist.Choose_Screen;
import com.aniketproject.medsist.Pat_Profile;
import com.aniketproject.medsist.R;
import com.aniketproject.medsist.databinding.ActivityPatHomeBinding;
import com.aniketproject.medsist.patient.PtDocFragment;
import com.aniketproject.medsist.patient.PtExcerFragment;
import com.aniketproject.medsist.patient.PtHomeFragment;
import com.aniketproject.medsist.patient.PtMediFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PatHomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ImageView ptset;
    ImageView ptprof;


    ActivityPatHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPatHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new PtHomeFragment());

        ptset = findViewById(R.id.ptset);
        ptprof = findViewById(R.id.ptprof);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FragmentActivity fragmentActivity = new FragmentActivity();

        if (auth.getCurrentUser() == null){
            startActivity(new Intent(PatHomeActivity.this, Choose_Screen.class));
        }
        ptprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatHomeActivity.this, Pat_Profile.class);
                startActivity(i);
            }
        });
        ptset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(PatHomeActivity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                auth.signOut();
                                startActivity(new Intent(PatHomeActivity.this,Choose_Screen.class));
                                finish();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        binding.pbottomnav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pthome1:
                    replaceFragment(new PtHomeFragment());
                    break;
                case R.id.ptmedicine1:
                    replaceFragment(new PtMediFragment());
                    break;
                case R.id.ptdoctor1:
                    replaceFragment(new PtDocFragment());
                    break;
                case R.id.ptexercise1:
                    replaceFragment(new PtExcerFragment());
                    break;
            }
            return true;
        });







    }
    public void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ptfragment, fragment);
        fragmentTransaction.commit();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}