package com.bravvura.nestledtime.firebase.manager;

import android.content.Context;

import com.bravvura.nestledtime.firebase.FirebaseConstants;
import com.bravvura.nestledtime.firebase.OnValueEventListener;
import com.bravvura.nestledtime.firebase.model.MemoryItem;
import com.bravvura.nestledtime.firebase.model.WorldItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 24-03-2018.
 */

public class MyFirebaseManager {
    public static String userId = "facebookUserId::2088122474754865";

    public static void initFireBaseManager(Context context) {
        FirebaseApp.initializeApp(context);
    }

    public static void getWorldList(Context context, final OnValueEventListener<ArrayList<WorldItem>> eventListener) {
        initFireBaseManager(context);
        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE.DIARIES).orderByChild(FirebaseConstants.COLUMNS.CREATED_BY_USER_ID).equalTo(userId)/*.equalTo("", userId)*/.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                ArrayList<WorldItem> collection = new ArrayList<>();

                for (String key : values.keySet()) {
                    Object obj = values.get(key);
                    if (obj instanceof Map) {
                        Map<String, String> mapObj = (Map<String, String>) obj;
                        WorldItem item = new Gson().fromJson(new JSONObject(mapObj).toString(), WorldItem.class);
                        item.worldId = key;
                        collection.add(item);
                    }
                }
                eventListener.onValueRecived(collection);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onCancelled(databaseError.getDetails());
            }
        });
    }

    public static void getMemoryItem(Context context, final String worldId, final String memoryId, final OnValueEventListener<MemoryItem> eventListener) {
        initFireBaseManager(context);
        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE.ENTRIES).child(worldId).child(memoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                MemoryItem memoryItem = MemoryItem.from(values);
                memoryItem.worldId = worldId;
                memoryItem.memoryId = memoryId;
                eventListener.onValueRecived(memoryItem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventListener.onCancelled(databaseError.getDetails());
            }
        });
    }

    public static void getMemoryList(Context context, final String worldId, final OnValueEventListener<ArrayList<MemoryItem>> eventListener) {
        initFireBaseManager(context);
        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.TABLE.ENTRIES)
                .child(worldId).orderByChild(FirebaseConstants.COLUMNS.MODIFIED_ON)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                        ArrayList<MemoryItem> collection = new ArrayList<>();
                        for (String key : values.keySet()) {
                            Object obj = values.get(key);
                            if (obj instanceof Map) {
                                MemoryItem memoryItem = MemoryItem.from((Map<String, Object>) obj);
                                memoryItem.worldId = worldId;
                                memoryItem.memoryId = key;
                                collection.add(memoryItem);
                            }
                        }
                        eventListener.onValueRecived(collection);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        eventListener.onCancelled(databaseError.getDetails());
                    }
                });
    }
}
