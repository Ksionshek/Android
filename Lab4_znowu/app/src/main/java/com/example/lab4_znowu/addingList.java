package com.example.lab4_znowu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4_znowu.tasks.TaskListContent;

public class addingList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_list);
    }

    public void addClick(View view) {
        EditText taskTitleEditTxt = findViewById(R.id.taskTitle);
        EditText taskDescriptionEditTxt = findViewById(R.id.taskDescription);
        EditText taskBirEditTxt = findViewById(R.id.taskBir);
        EditText taskPhoneEditTxt = findViewById(R.id.taskPhone);

        String taskTitle = taskTitleEditTxt.getText().toString();
        String taskDescription = taskDescriptionEditTxt.getText().toString();
        String taskBir = taskBirEditTxt.getText().toString();
        String taskPhone = taskPhoneEditTxt.getText().toString();

        if(taskTitle.isEmpty() && taskDescription.isEmpty() && taskBir.isEmpty() && taskPhone.isEmpty()){
            Toast.makeText(getApplicationContext(), "wypelnij wszystkie pola", Toast.LENGTH_SHORT).show();
        }else{
            if(taskPhone.length()==9) {

                TaskListContent.addItem(new TaskListContent.Task( String.valueOf(TaskListContent.ITEMS.size() + 1),
                        taskTitle,
                        taskDescription,
                        taskBir,
                        taskPhone));
            }
            else {
                Toast.makeText(getApplicationContext(), "numer powinien miec 9 cyfr", Toast.LENGTH_SHORT).show();
                taskPhoneEditTxt.setText("");
            }
        }
        //((TaskFragment) getSupportFragmentManager().findFragmentById(R.id.taskFragment)).notifyDataChange();

        taskTitleEditTxt.setText("");
        taskDescriptionEditTxt.setText("");
        taskBirEditTxt.setText("");
        taskPhoneEditTxt.setText("");

        Intent intent = new Intent(addingList.this, MainActivity.class);
        startActivity(intent);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
