package com.example.jobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    Button button;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button = findViewById(R.id.CheckOutButton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailNew();
            }
        });
    }

    void sendEmailNew(){
        CollectionReference docRef = firebaseFirestore.collection("Host");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();

                    if(list.size()>0){

                    }else{
                        Toast.makeText(Home.this, "Host Not PRESENT", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }
        });





        DocumentReference documentRef = firebaseFirestore.collection("Host").document("New_Host");

        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnap = task.getResult();
                    Customer HostObject = docSnap.toObject(Customer.class);

//

                    getCustomerAndSend(HostObject);




                }else{
                    Toast.makeText(Home.this, "Object not taken", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    void getCustomerAndSend(final Customer hostObject){

        DocumentReference documentRef = firebaseFirestore.collection("New_Customer").document(firebaseAuth.getCurrentUser().getUid());

        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   Customer customer = task.getResult().toObject(Customer.class);
                   String Message = "Host Name: "+hostObject.getName()+"Host Email id: "+hostObject.getEmail()+"Customer name: "+customer.getName()+" Customer Contact No: "+
                           customer.getPhone()+"Customer Email: "+customer.getEmail()+
                           "Customer Check in Time: "+customer.getCheckIn() +
                           "Customer Check out Time: "+customer.getCheckOut();

                   //String message2 = "Ishil message";

                   String hostEmailId = customer.getEmail();
                   List<String> toEmailList = Arrays.asList(hostEmailId
                           .split("\\s*,\\s*"));
                   new SendMailTask(Home.this).execute("ProfessionalIshil1@gmail.com",
                           "ProfessionalIshil1@123",toEmailList, "New Customer Checked in", Message);





               }
            }
        });

    }




}
