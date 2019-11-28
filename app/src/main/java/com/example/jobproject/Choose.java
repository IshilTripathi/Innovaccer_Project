package com.example.jobproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class Choose extends AppCompatActivity {

    Button Customer,Host;
    FirebaseFirestore firebaseFirestore;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        FirebaseApp.initializeApp(this);
        Customer = findViewById(R.id.Customer);
        Host = findViewById(R.id.Host);
        firebaseFirestore = FirebaseFirestore.getInstance();

        view = findViewById(R.id.ContraintLayoutChoose);

        check();


        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose.this,MainActivity.class));
            }
        });


        Host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose.this,HostActivity.class));
            }
        });
    }

    void check(){
        CollectionReference docRef = firebaseFirestore.collection("Host");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();

                    if(list.size()>0){
                        Host.setEnabled(false);
                        Snackbar snackbar = Snackbar.make(view,"Atmost 1 Host can be there.",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }


            }
        });

    }
}
