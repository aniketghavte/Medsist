package com.aniketproject.medsist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aniketproject.medsist.doctor.DocLogIN;
import com.aniketproject.medsist.patient.PatLogIN;

public class Choose_Screen extends AppCompatActivity {

    TextView patient , doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_screen);

        patient = findViewById(R.id.patient);
        doctor = findViewById(R.id.doctor);


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose_Screen.this, PatLogIN.class));
                finish();
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Choose_Screen.this, DocLogIN.class));
                Intent i = new Intent(Choose_Screen.this , DocLogIN.class);
                startActivity(i);
                finish();
            }
        });

    }
}