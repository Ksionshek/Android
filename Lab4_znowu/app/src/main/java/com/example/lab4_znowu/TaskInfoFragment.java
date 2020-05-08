package com.example.lab4_znowu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.lab4_znowu.tasks.TaskListContent;


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

        TextView taskInfoTitle = activity.findViewById(R.id.taskInfoName_Surrname);
        TextView taskInfoDescription = activity.findViewById(R.id.taskInfoBirth);
        TextView taskInfoPhone = activity.findViewById(R.id.taskInfoPhone);
        ImageView taskInfoImage = activity.findViewById(R.id.taskInfoImage);

        taskInfoTitle.setText(task.name+" "+task.surrname);
        taskInfoDescription.setText(task.birth);
        taskInfoPhone.setText(task.phone);
        final int picPath = Integer.parseInt(task.id);
        final int modu = picPath % 15;
                Drawable taskDrawable;
                switch (modu){
                    case 1:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_1);
                        break;
                    case 2:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_2);
                        break;
                    case 3:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_3);
                        break;
                    case 4:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_4);
                        break;
                    case 5:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_5);
                        break;
                    case 6:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_6);
                        break;
                    case 7:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_7);
                        break;
                    case 8:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_8);
                        break;
                    case 9:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_9);
                        break;
                    case 10:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_10);
                        break;
                    case 11:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_11);
                        break;
                    case 12:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_12);
                        break;
                    case 13:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_13);
                        break;
                    case 14:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_14);
                        break;
                    case 15:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_15);
                        break;
                    default:
                        taskDrawable = activity.getResources().getDrawable(R.drawable.avatar_16);

                }
                taskInfoImage.setImageDrawable(taskDrawable);

    }
}
