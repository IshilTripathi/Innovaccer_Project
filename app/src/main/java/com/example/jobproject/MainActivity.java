package com.example.jobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import static android.provider.Telephony.Carriers.PASSWORD;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 101;

    TextInputLayout emailCustomerLayout,nameCustomerLayout,phoneCustomerLayout,checkInCustomerLayout,checkOutCustomerLayout;
    TextInputEditText emailCustomerEditText,nameCustomerEditText,phoneCustomerEdiText,checkInCustomerEditText,checkOutCustomerEditText;
    MaterialButton submitCustomer;
    ConstraintLayout constraintLayoutCustomer;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBarCustomer;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inititalize();
        /*final Button send = (Button) this.findViewById(R.id.button1);

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                sendSMSMessage();
                //sendSMSMessage();
                /*Log.i("SendMailActivity", "Send Button Clicked.");

                String fromEmail = ((TextView) findViewById(R.id.editText1))
                        .getText().toString();
                String fromPassword = ((TextView) findViewById(R.id.editText2))
                        .getText().toString();
                String toEmails = ((TextView) findViewById(R.id.editText3))
                        .getText().toString();
                List<String> toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = ((TextView) findViewById(R.id.editText4))
                        .getText().toString();
                String emailBody = ((TextView) findViewById(R.id.editText5))
                        .getText().toString();
                new SendMailTask(MainActivity.this).execute("ProfessionalIshil1@gmail.com",
                        "ProfessionalIshil1@123", toEmailList, "Email subject", "Email body");
            }
        });
*/




    }

    private void inititalize() {

        emailCustomerLayout = findViewById(R.id.emailCustomerLayout);
        phoneCustomerLayout = findViewById(R.id.phoneCustomerLayout);
        constraintLayoutCustomer = findViewById(R.id.ContraintLayoutCustomer);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBarCustomer = findViewById(R.id.progressBarCustomer);
        submitCustomer = findViewById(R.id.submitCustomer);
        nameCustomerLayout = findViewById(R.id.nameCustomerLayout);
        checkInCustomerLayout = findViewById(R.id.checkInCustomerLayout);
        checkOutCustomerLayout = findViewById(R.id.checkOutCustomerLayout);

        emailCustomerEditText = findViewById(R.id.emailCustomerEditText);
        phoneCustomerEdiText = findViewById(R.id.phoneCustomerEditText);

        nameCustomerEditText = findViewById(R.id.nameCustomerEditText);
        checkInCustomerEditText = findViewById(R.id.checkInCustomerEditText);
        checkOutCustomerEditText = findViewById(R.id.checkOutCustomerEditText);

        constraintLayoutCustomer.setOnClickListener(null);
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

        submitCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterCustomer();
            }
        });
    }

    private void enterCustomer() {

        final String email = emailCustomerEditText.getText().toString().trim();
        String name = nameCustomerEditText.getText().toString().trim();
        String checkIn = checkInCustomerEditText.getText().toString().trim();
        String checkOut = checkOutCustomerEditText.getText().toString().trim();

        String phone = phoneCustomerEdiText.getText().toString().trim();

        progressBarCustomer.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(email)){
            emailCustomerLayout.setErrorEnabled(true);
            emailCustomerLayout.setError("Please Enter the Email");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailCustomerLayout.setErrorEnabled(true);
            emailCustomerLayout.setError("Please Enter the Valid Email");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }
        if(TextUtils.isEmpty(name)){
            nameCustomerLayout.setErrorEnabled(true);
            nameCustomerLayout.setError("Please Enter Your Name");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }

        if(TextUtils.isEmpty(phone)){
            phoneCustomerLayout.setErrorEnabled(true);
            phoneCustomerLayout.setError("Please Enter your Contact NO.");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }
        if(TextUtils.isEmpty(checkIn)){
            checkInCustomerLayout.setErrorEnabled(true);
            checkInCustomerLayout.setError("Please Enter your Check In Time");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }
        if(TextUtils.isEmpty(checkOut)){
            checkOutCustomerLayout.setErrorEnabled(true);
            checkOutCustomerLayout.setError("Please Enter your Check Out Time");
            progressBarCustomer.setVisibility(View.GONE);
            return;
        }

        final Customer customer = new Customer(email,phone,name,checkIn,checkOut);

        StringBuilder stringBuilder = new StringBuilder(email);
        String password = stringBuilder.reverse().toString();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //  startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                progressBarCustomer.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"CHECK IN SUCCESSFULL", Toast.LENGTH_SHORT).show();

                storeCustomer(customer,firebaseAuth.getCurrentUser().getUid());
                sendEmailToHost(customer);




                Intent intent = new Intent(MainActivity.this,Home.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarCustomer.setVisibility(View.GONE);
            }
        });
    }
    void storeCustomer(Customer customer,String userId){


        firebaseFirestore.collection("New_Customer").document(userId).set(customer);

    }
//////////////////////////////////////////////////////////////////////////
    void sendEmailToHost(final Customer customer){
        /*if(!available()){
            Toast.makeText(this, "Email Host Issue", Toast.LENGTH_SHORT).show();
            return;
        }*/
        CollectionReference docRef = firebaseFirestore.collection("Host");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();

                    if(list.size()>0){

                    }else{
                        Toast.makeText(MainActivity.this, "Host Not PRESENT", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }
        });


        final Customer[] host = {null};


        DocumentReference documentRef = firebaseFirestore.collection("Host").document("New_Host");

        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnap = task.getResult();
                    Customer cs = docSnap.toObject(Customer.class);
                    host[0] = cs;
                    Toast.makeText(MainActivity.this,host[0].toString(), Toast.LENGTH_SHORT).show();

                    String hostEmailId = host[0].getEmail();
                    List<String> toEmailList = Arrays.asList(hostEmailId
                            .split("\\s*,\\s*"));
                    Toast.makeText(MainActivity.this,hostEmailId, Toast.LENGTH_SHORT).show();
                    String Message = "Customer name: "+customer.getName()+" Customer Contact No: "+
                            customer.getPhone()+"Customer Email: "+customer.getEmail()+
                            "Customer Check in Time: "+customer.getCheckIn() +
                            "Customer Check out Time: "+customer.getCheckOut();

                    //String message2 = "Ishil message";

                    new SendMailTask(MainActivity.this).execute("ProfessionalIshil1@gmail.com",
                            "ProfessionalIshil1@123",toEmailList, "New Customer Checked in", Message);

                    sendSMSMessageToHost(customer);


                }else{
                    Toast.makeText(MainActivity.this, "Object not taken", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    protected void sendSMSMessageToHost(final Customer customer) {

///////////////////////////////////////////////////
        final Customer[] host = {null};


        DocumentReference documentRef = firebaseFirestore.collection("Host").document("New_Host");

        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnap = task.getResult();
                    Customer cs = docSnap.toObject(Customer.class);
                    host[0] = cs;
                    Toast.makeText(MainActivity.this,host[0].toString(), Toast.LENGTH_SHORT).show();

                    String hostPhoneNumber = host[0].getPhone();

                    String Message = "Customer name: "+customer.getName()+" Customer Contact No: "+
                            customer.getPhone()+"Customer Email: "+customer.getEmail()+
                            "Customer Check in Time: "+customer.getCheckIn() +
                            "Customer Check out Time: "+customer.getCheckOut();

                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                Manifest.permission.SEND_SMS)) {
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    MY_PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    }else{
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(hostPhoneNumber, null,Message, null, null);
                        Toast.makeText(getApplicationContext(), "SMS sent.",
                                Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Object not taken", Toast.LENGTH_SHORT).show();
                }
            }
        });




        ///////////////////////////////////





    }

    Customer getTheHostData(){
        final Customer[] customer = {null};
        DocumentReference docRef = firebaseFirestore.collection("Host").document("New_Host");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnap = task.getResult();
                    Customer cs = docSnap.toObject(Customer.class);
                    customer[0] = cs;
                    Toast.makeText(MainActivity.this,customer[0].toString(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Object not taken", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return customer[0];
    }
    boolean available(){



        CollectionReference docRef = firebaseFirestore.collection("Host");
        final boolean[] decision = {false};
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();

                    if(list.size()>0){
                        decision[0] = true;
                    }
                }


            }
        });

        return decision[0];

    }
/////////////////////////////////////////////////////////////////////////////
    private void inputCredentialsVerification() {


        emailCustomerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = emailCustomerEditText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    emailCustomerLayout.setErrorEnabled(true);
                    emailCustomerLayout.setError("Please enter the Email");
                }else{
                    emailCustomerLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        phoneCustomerEdiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = phoneCustomerEdiText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    phoneCustomerLayout.setErrorEnabled(true);
                    phoneCustomerLayout.setError("Please enter your Phone NO.");
                }else{
                    phoneCustomerLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameCustomerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = nameCustomerEditText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    nameCustomerLayout.setErrorEnabled(true);
                    nameCustomerLayout.setError("Please enter your Name.");
                }else{
                    nameCustomerLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkInCustomerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = checkInCustomerEditText.getText().toString().trim();

                if(TextUtils.isEmpty(phone)){
                    checkInCustomerLayout.setErrorEnabled(true);
                    checkInCustomerLayout.setError("Please enter your Check In Time");
                }else{
                    checkInCustomerLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        checkOutCustomerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = checkOutCustomerEditText.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    checkOutCustomerLayout.setErrorEnabled(true);
                    checkOutCustomerLayout.setError("Please enter your Check Out Time");
                }else{
                    checkOutCustomerLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        emailCustomerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String email = emailCustomerEditText.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    emailCustomerLayout.setErrorEnabled(true);
                    emailCustomerLayout.setError("Please enter the Email");
                }else{
                    emailCustomerLayout.setErrorEnabled(false);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("9935422240", null,"This is a sms message from Ishil", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}
