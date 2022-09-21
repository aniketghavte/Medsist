package com.aniketproject.medsist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aniketproject.medsist.R;
import com.aniketproject.medsist.databinding.ActivityDocHomeBinding;
import com.aniketproject.medsist.databinding.ActivityPatHomeBinding;
import com.aniketproject.medsist.patient.PatHomeActivity;
import com.aniketproject.medsist.patient.PtHomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DocHomeActivity extends AppCompatActivity {

    ActivityDocHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ImageView doc_dtset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDocHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        doc_dtset = findViewById(R.id.dtset);
        replaceFragment(new Doc_Home_Fragment());

        if (auth.getCurrentUser() == null){
            startActivity(new Intent(DocHomeActivity.this, Choose_Screen.class));
        }
        doc_dtset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(DocHomeActivity.this);
                builder.setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                auth.signOut();
                                startActivity(new Intent(DocHomeActivity.this,Choose_Screen.class));
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

        binding.dbottomnav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dthome1:
                    replaceFragment(new Doc_Home_Fragment());
                    break;
                case R.id.dtmedicine1:
                    replaceFragment(new Doc_medi_Fragment());
                    break;
            }
            return true;
        });
    }

    public void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dtfragment, fragment);
        fragmentTransaction.commit();
    }

}