package com.aniketproject.medsist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aniketproject.medsist.patient.PatLogIN;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class DocSignIn extends AppCompatActivity {

    private TextView txt_signBtn;
    private TextView txt_signUp;
    private CircleImageView Profile_image;
    private EditText reg_Name,reg_Email,reg_pass,reg_CPass;
    private FirebaseAuth auth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Uri imageUri;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private String imageURI;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_sign_in);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        txt_signBtn = findViewById(R.id.doc_idTxtSignIn);
        Profile_image = findViewById(R.id.doc_profile_image);
        reg_Name = findViewById(R.id.doc_idRegName);
        reg_Email = findViewById(R.id.doc_idRegEmail);
        reg_pass = findViewById(R.id.doc_idRegPass);
        reg_CPass = findViewById(R.id.doc_idRegCPass);
        txt_signUp  = findViewById(R.id.doc_IdBtnSignUp);

        Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = reg_Name.getText().toString();
                String email = reg_Email.getText().toString();
                String password = reg_pass.getText().toString();
                String CPassword= reg_CPass.getText().toString();
                String status = "Hello There";

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)||
                        TextUtils.isEmpty(password)|| TextUtils.isEmpty(CPassword)){
                    Toast.makeText(DocSignIn.this, "Field Should not be Empty", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!email.matches(emailPattern)){
                    reg_Email.setError("Enter Valid Email");
                    Toast.makeText(DocSignIn.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (!password.equals(CPassword)){
                    Toast.makeText(DocSignIn.this, "Password and CPassword should be same", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else if (password.length()<6){
                    Toast.makeText(DocSignIn.this, "Password Must Be at least 6 char", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DocSignIn.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                DatabaseReference databaseReference = database.getReference().child("doctor").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("profile_img").child(auth.getUid());

                                if (imageUri!= null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){

                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(),name,email,imageURI,"Hello There");
                                                        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(DocSignIn.this, DocHomeActivity.class));
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(DocSignIn.this, "Error In Creating a new User", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {


                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/medsist-527a1.appspot.com/o/profile_img%2FPowerPeople_Liam003.png?alt=media&token=c5aac856-c5a8-4b8a-990c-dc71c105f3ca";
                                    Users patient = new Users(auth.getUid(),name,email,imageURI,"Hello There");
                                    databaseReference.setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                startActivity(new Intent(DocSignIn.this,DocHomeActivity.class));
                                                finish();
                                            }else {
                                                Toast.makeText(DocSignIn.this, "Error In Creating a new User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(DocSignIn.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        txt_signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocSignIn.this, DocLogIN.class));
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111){
            if (data != null){
                imageUri = data.getData();
                Profile_image.setImageURI(imageUri);
            }
        }
    }
}