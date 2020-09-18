package com.example.hw3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw3.tasks.TaskListContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class addingList extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_list);
    }
    // Access a Cloud Firestore instance from your Activity

    public void addClick(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText addMarka = findViewById(R.id.marka);
        EditText addSilnik = findViewById(R.id.silnik);
        EditText addModel = findViewById(R.id.model);
        EditText addCena = findViewById(R.id.cena);

        String stringMarka = addMarka.getText().toString();
        String stringSilnik = addSilnik.getText().toString();
        String stringModel = addModel.getText().toString();
        String stringCena = addCena.getText().toString();

        if(stringMarka.isEmpty() && stringSilnik.isEmpty() && stringModel.isEmpty() && stringCena.isEmpty()){
            Toast.makeText(getApplicationContext(), "wypelnij wszystkie pola", Toast.LENGTH_SHORT).show();
        }else{
            if(stringCena.length()<150) {

                TaskListContent.Task data =  new TaskListContent.Task("",
                        stringMarka,
                        stringModel,
                        stringSilnik,
                        stringCena);

                db.collection("bdAuta")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error adding document", e);
                            }
                        });
            }
            else {
                Toast.makeText(getApplicationContext(), "nie ma auta o takiej cenie", Toast.LENGTH_SHORT).show();
                addCena.setText("");
            }
        }
        //((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Intent intent = new Intent(addingList.this, MainActivity.class);
        startActivity(intent);

        addMarka.setText("");
        addModel.setText("");
        addSilnik.setText("");
        addCena.setText("");





    }
}
