package com.example.hw3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hw3.tasks.TaskListContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskInfoFragment extends Fragment {

    public TaskInfoFragment() {

    }
    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent !=null){
            TaskListContent.Task receivedTask = intent.getParcelableExtra(MainActivity.taskExtra);
            if(receivedTask!=null){
                displayTask(receivedTask);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_info, container, false);
    }
    public void displayTask(TaskListContent.Task task){
        FragmentActivity activity = getActivity();

        TextView showMarka = activity.findViewById(R.id.showMarka_Model);
        TextView showCena = activity.findViewById(R.id.showCena);
        TextView showSilnikk = activity.findViewById(R.id.showSilnik);


        showMarka.setText(task.marka +" "+task.model);
        showCena.setText("CENA: " + task.cena);
        showSilnikk.setText("SILNIK: " + task.silnik);
    }
}
