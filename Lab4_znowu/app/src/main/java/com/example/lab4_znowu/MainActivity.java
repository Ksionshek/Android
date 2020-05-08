package com.example.lab4_znowu;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.lab4_znowu.tasks.TaskListContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements TaskFragment.OnListFragmentInteractionListener, DeleteDialog.OnDeleteDialogInteractionListener, CallDeleteDialog.OnCallDialogInteractionListener {
    public static final String taskExtra = "taskExtra";
    private int currentItemPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addingList.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onListFragmentClickInteraction(TaskListContent.Task task, int position) {
        Toast.makeText(this,getString(R.string.item_selected_msg),Toast.LENGTH_SHORT).show();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            displayTaskInFragment(task);
        }else{
            startSecondActivity(task,position);
        }

    }

    @Override
    public void onListFragmentLongClickInteraction(int position) {
        //Toast.makeText(this,getString(R.string.long_click_msg)+position, Toast.LENGTH_SHORT).show();
        showCallDialog();
        currentItemPosition = position;
    }

    @Override
    public void onListFragmentClickBinInteraction(int position) {
        //Toast.makeText(this,getString(R.string.long_click_msg)+position, Toast.LENGTH_SHORT).show();
        showDeleteDialog();
        currentItemPosition = position;
    }

    private void startSecondActivity(TaskListContent.Task task, int position){
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra(taskExtra,task);
        startActivity(intent);
    }
    private void displayTaskInFragment(TaskListContent.Task task){
        TaskInfoFragment taskInfoFragment = ((TaskInfoFragment)getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if(taskInfoFragment!=null){
            taskInfoFragment.displayTask(task);
        }
    }
    private void showDeleteDialog(){
        DeleteDialog.newInstance().show(getSupportFragmentManager(),getString(R.string.delete_dialog_tag));
    }
    private void showCallDialog(){
        CallDeleteDialog.newInstance().show(getSupportFragmentManager(),getString(R.string.delete_dialog_tag));
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

    @Override
    public void onPositiveCall(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Calling...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeCall(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Calling has been declined", Toast.LENGTH_SHORT).show();
    }
}
