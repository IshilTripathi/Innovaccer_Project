package com.example.jobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostActivity extends AppCompatActivity {

    TextInputLayout emailHostLayout,nameHostLayout,phoneHostLayout,checkInHostLayout,checkOutHostLayout;
    TextInputEditText emailHostEditText,nameHostEditText,phoneHostEdiText,checkInHostEditText,checkOutHostEditText;
    MaterialButton submitHost;
    ConstraintLayout constraintLayoutHost;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBarHost;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        inititalize();
    }
    private void inititalize() {

        emailHostLayout = findViewById(R.id.emailHostLayout);
        phoneHostLayout = findViewById(R.id.phoneHostLayout);
        constraintLayoutHost = findViewById(R.id.ContraintLayoutHost);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBarHost = findViewById(R.id.progressBarHost);
        submitHost = findViewById(R.id.submitHost);
        nameHostLayout = findViewById(R.id.nameHostLayout);


        emailHostEditText = findViewById(R.id.emailHostEditText);
        phoneHostEdiText = findViewById(R.id.phoneHostEditText);

        nameHostEditText = findViewById(R.id.nameHostEditText);


        constraintLayoutHost.setOnClickListener(null);
        firebaseFirestore= FirebaseFirestore.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();
        inputCredentialsVerification();

    }

    @Override
    protected void onResume() {
        super.onResume();
        submitHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHost();
            }
        });
    }

    private void saveHost() {

        final String email = emailHostEditText.getText().toString().trim();
        String name = nameHostEditText.getText().toString().trim();
        String phone = phoneHostEdiText.getText().toString().trim();

        progressBarHost.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(email)){
            emailHostLayout.setErrorEnabled(true);
            emailHostLayout.setError("Please Enter the Email");
            progressBarHost.setVisibility(View.GONE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailHostLayout.setErrorEnabled(true);
            emailHostLayout.setError("Please Enter the Valid Email");
            progressBarHost.setVisibility(View.GONE);
            return;
        }
        if(TextUtils.isEmpty(name)){
            nameHostLayout.setErrorEnabled(true);
            nameHostLayout.setError("Please Enter Your Name");
            progressBarHost.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(phone)){
            phoneHostLayout.setErrorEnabled(true);
            phoneHostLayout.setError("Please Enter your Contact NO.");
            progressBarHost.setVisibility(View.GONE);
            return;
        }


        final Customer customer = new Customer(email,phone,name,"0","0");

        StringBuilder stringBuilder = new StringBuilder(email);
        String password = stringBuilder.reverse().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //  startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                progressBarHost.setVisibility(View.GONE);
                Toast.makeText(HostActivity.this,"YOU HAVE THE RESPONSIBILITY OF HOST NOW...", Toast.LENGTH_SHORT).show();

                storeHost(customer,firebaseAuth.getCurrentUser().getUid());
                //sendSMSMessageToHost(customer);
                //sendEmailToHost(customer);


                //Intent intent = new Intent(SignUpActivity.this,UploadProfile.class);
                //startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarHost.setVisibility(View.GONE);
            }
        });
    }
    void storeHost(Customer customer,String userId){


        firebaseFirestore.collection("Host").document("New_Host").set(customer);

    }


    private void inputCredentialsVerification() {


        emailHostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = emailHostEditText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    emailHostLayout.setErrorEnabled(true);
                    emailHostLayout.setError("Please enter the Email");
                }else{
                    emailHostLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        phoneHostEdiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = phoneHostEdiText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    phoneHostLayout.setErrorEnabled(true);
                    phoneHostLayout.setError("Please enter your Phone NO.");
                }else{
                    phoneHostLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameHostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = nameHostEditText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    nameHostLayout.setErrorEnabled(true);
                    nameHostLayout.setError("Please enter your Name.");
                }else{
                    nameHostLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        emailHostEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String email = emailHostEditText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    emailHostLayout.setErrorEnabled(true);
                    emailHostLayout.setError("Please enter the Email");
                }else{
                    emailHostLayout.setErrorEnabled(false);
                }
            }
        });

    }
}
