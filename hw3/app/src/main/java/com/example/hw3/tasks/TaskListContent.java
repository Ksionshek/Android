package com.example.hw3.tasks;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskListContent {


    public static final List<Task> ITEMS = new ArrayList<Task>();
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();
    private static final String TAG = "DELETE";



    public static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    public static void removeItem(int position){
        String itemId = ITEMS.get(position).id;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bdAuta").document(itemId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {


                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        ITEMS.remove(position);
        ITEM_MAP.remove(itemId);
    }


    public static class Task implements Parcelable {
        public  String id;
        public  String marka;
        public  String model;
        public  String silnik;
        public  String cena;


        public Task(String id, String marka, String model, String silnik,  String cena) {
            this.id = id;
            this.marka = marka;
            this.model = model;
            this.silnik = silnik;
            this.cena = cena;
        }



        protected Task(Parcel in) {
            id = in.readString();
            marka = in.readString();
            silnik = in.readString();
            model = in.readString();
            cena = in.readString();
        }

        public static final Creator<Task> CREATOR = new Creator<Task>() {
            @Override
            public Task createFromParcel(Parcel in) {
                return new Task(in);
            }

            @Override
            public Task[] newArray(int size) {
                return new Task[size];
            }
        };


        @Override
        public String toString() {
            return marka;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(marka);
            dest.writeString(silnik);
            dest.writeString(model);
            dest.writeString(cena);
        }

    }
}
