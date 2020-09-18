package com.example.hw3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.hw3.tasks.TaskListContent;

public class MainActivity extends AppCompatActivity
        implements
        TaskFragment.OnListFragmentInteractionListener,
        DeleteDialog.OnDeleteDialogInteractionListener
         {

    public static final String taskExtra = "taskExtra";
    private static final String TAG = "MainActivity";
    private FirebaseFirestore mFirestore;
    private int currentItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startAddCarActivity();
            }
        });
        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirestoreContent();
    }

    private void getFirestoreContent() {
        TaskListContent.ITEM_MAP.clear();
        TaskListContent.ITEMS.clear();
        mFirestore.collection("bdAuta")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String stringID = document.getId();
                                String stringMarka = (String) document.getData().get("marka");
                                String stringModel = (String) document.getData().get("model");
                                String stringCena = (String) document.getData().get("cena");
                                String stringSilnik = (String) document.getData().get("silnik");

                                TaskListContent.addItem(new TaskListContent.Task(stringID,
                                        stringMarka,
                                        stringModel,
                                        stringSilnik,
                                        stringCena

                                ));
                            }
                            ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.list)).notifyDataChange();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void startCarDetailsActivity(TaskListContent.Task car) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra(taskExtra, car);
        startActivity(intent);
    }

    private void startCarUpdateActivity(TaskListContent.Task car) {
        Intent intent = new Intent(this, TaskUpdateActivity.class);
        intent.putExtra(taskExtra, car);
        startActivity(intent);
    }

    private void startAddCarActivity() {
        Intent intent = new Intent(this, addingList.class);
        startActivity(intent);
    }

    private void displayTaskInFragment(TaskListContent.Task car) {
        TaskInfoFragment taskInfoFragment = ((TaskInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if (taskInfoFragment != null) {
            taskInfoFragment.displayTask(car);
        }
    }
    @Override
    public void onListFragmentClickInteraction(TaskListContent.Task task) {
        Toast.makeText(this,getString(R.string.item_selected_msg),Toast.LENGTH_SHORT).show();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            displayTaskInFragment(task);
        }else{
            startCarDetailsActivity(task);
        }

    }

    @Override
    public void onListFragmentLongClickInteraction(TaskListContent.Task car,int position) {
        //Toast.makeText(this,getString(R.string.long_click_msg)+position, Toast.LENGTH_SHORT).show();

        currentItemPosition = position;
        startCarUpdateActivity(car);
    }

    @Override
    public void onListFragmentClickBinInteraction(int position) {
        //Toast.makeText(this,getString(R.string.long_click_msg)+position, Toast.LENGTH_SHORT).show();
        showDeleteDialog();
        currentItemPosition = position;
    }


    private void showDeleteDialog(){
        DeleteDialog.newInstance().show(getSupportFragmentManager(),getString(R.string.delete_dialog_tag));
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        if(currentItemPosition != -1 && currentItemPosition < TaskListContent.ITEMS.size()){
            TaskListContent.removeItem(currentItemPosition);
            ((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();

        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    View v = findViewById(R.id.addButton);
    if(v != null){
        Snackbar.make(v,getString(R.string.delete_cancel_msg),Snackbar.LENGTH_LONG).setAction(getString(R.string.retry_msg), new View.OnClickListener(){
                    @Override
            public void onClick(View v){
                        showDeleteDialog();
                    }
                }
                ).show();
    }
    }


}
