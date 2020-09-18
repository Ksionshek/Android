package com.example.hw3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.hw3.tasks.TaskListContent;

import java.util.Objects;


public class TaskUpdateFragment extends Fragment {
    private static final String TAG = "TaskUpdateFragment";

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    EditText addMarka;
    EditText addModel;
    EditText addSilnik;
    EditText addCena;

    String stringID;
    String stringMarka;
    String stringModel;
    String stringSilnik;
    String stringCena;

    Button updateButton;

    public static TaskUpdateFragment newInstance(){return new TaskUpdateFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_update, container, false);
    }
    public void setCarParameters(TaskListContent.Task car) {

        //display info
        stringID = car.id;
        addMarka.setText(String.format("%s", car.marka));
        addModel.setText(String.format("%s", car.model));
        addSilnik.setText(String.format("%s", car.silnik));
        addCena.setText(String.format("%s", car.cena));

    }

    public void updateCarParameters() {

        stringMarka = addMarka.getText().toString();
        stringModel = addModel.getText().toString();
        stringSilnik = addSilnik.getText().toString();
        stringCena = addCena.getText().toString();

        if (!stringMarka.isEmpty() && !stringModel.isEmpty() && !stringSilnik.isEmpty() && !stringCena.isEmpty()) {

            Toast toastAddedContact = Toast.makeText(getActivity(), "All fields fill successfully!", Toast.LENGTH_SHORT);
            toastAddedContact.setGravity(Gravity.CENTER_VERTICAL, 0, +200);
            toastAddedContact.show();

            DocumentReference documentReference = mFirestore.collection("bdAuta").document(stringID);

            documentReference
                    .update(
                            "marka", stringMarka,
                            "model", stringModel,
                            "silnik", stringSilnik,
                            "cena", stringCena

                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

            //Automatically hide keyboard after button press
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (view == null) {
                view = new View(getActivity());
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //Move to activity_main/MainActivity and refresh contact list
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            addMarka.setText("");
            addModel.setText("");
            addSilnik.setText("");
            addCena.setText("");

        } else {
            Toast toastFillAllFields = Toast.makeText(getActivity(), "Fill all fields correctly to add contact!", Toast.LENGTH_LONG);
            toastFillAllFields.setGravity(Gravity.CENTER_VERTICAL, 0, +200);
            toastFillAllFields.show();
        }

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        addMarka = activity.findViewById(R.id.marka);
        addModel = activity.findViewById(R.id.model);
        addSilnik = activity.findViewById(R.id.silnik);
        addCena = activity.findViewById(R.id.cena);


        updateButton = activity.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCarParameters();
            }
        });

        Intent intent = requireActivity().getIntent();
        if (intent != null) {
            TaskListContent.Task receivedTask = intent.getParcelableExtra(MainActivity.taskExtra);
            if (receivedTask != null) {
                setCarParameters(receivedTask);
            }
        }
    }



}
