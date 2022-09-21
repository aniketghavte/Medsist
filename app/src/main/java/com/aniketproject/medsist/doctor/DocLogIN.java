package com.aniketproject.medsist.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.aniketproject.medsist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DocLogIN extends AppCompatActivity {

    private TextView idTxtSignUp;
    private EditText login_Email, login_password;
    private TextView SignBtn;
    private FirebaseAuth auth;
    private  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_log_in);

        idTxtSignUp = findViewById(R.id.doc_idTxtSignUp);
        auth = FirebaseAuth.getInstance();
        login_Email = findViewById(R.id.doc_idLoginEmail);
        login_password = findViewById(R.id.doc_idLoginPass);
        SignBtn = findViewById(R.id.doc_IdBtnLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        SignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = login_Email.getText().toString();
                String password = login_password.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(DocLogIN.this, "Please provide Valid Email or Password", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    login_Email.setError("Invalid Email");
                    progressDialog.dismiss();
                    Toast.makeText(DocLogIN.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    login_password.setError("Invalid Password");
                    progressDialog.dismiss();
                    Toast.makeText(DocLogIN.this, "Please Enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(DocLogIN.this, DocHomeActivity.class));
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DocLogIN.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        idTxtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocLogIN.this, DocSignIn.class));
            }
        });

    }
}