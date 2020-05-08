package com.example.lab4_znowu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_znowu.TaskFragment.OnListFragmentInteractionListener;
import com.example.lab4_znowu.tasks.TaskListContent.Task;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyTaskRecyclerViewAdapter(List<Task> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Task task = mValues.get(position);
        holder.mItem = task;
        holder.mContentView.setText(task.name);
        final int picPath = Integer.parseInt(task.id);
        final int modu = picPath % 15;
        Context context = holder.mView.getContext();
        Drawable taskDrawable;
        switch (modu) {
            case 1:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_1);
                break;
            case 2:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_2);
                break;

            case 3:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_3);
                break;
            case 4:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_4);
                break;
            case 5:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_5);
                break;
            case 6:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_6);
                break;
            case 7:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_7);
                break;
            case 8:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_8);
                break;
            case 9:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_9);
                break;
            case 10:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_10);
                break;
            case 11:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_11);
                break;
            case 12:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_12);
                break;
            case 13:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_13);
                break;
            case 14:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_14);
                break;
            case 15:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_15);
                break;
            default:
                taskDrawable = context.getResources().getDrawable(R.drawable.avatar_16);
        }
        holder.mItemImageView.setImageDrawable(taskDrawable);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentClickInteraction(holder.mItem, position);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onListFragmentLongClickInteraction(position);
                return false;

            }
        });
        holder.mItemBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentClickBinInteraction(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mItemImageView;
        public Task mItem;
        public final ImageView mItemBin;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemImageView = view.findViewById(R.id.item_image);
            mContentView = view.findViewById(R.id.content);
            mItemBin = view.findViewById(R.id.remBtn);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
