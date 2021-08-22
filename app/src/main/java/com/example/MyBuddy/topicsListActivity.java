package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.MyBuddy.Model.Chat;
import com.example.MyBuddy.Model.Topic;
import com.example.MyBuddy.Model.UserID;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class topicsListActivity extends AppCompatActivity
{
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public List<Topic> topicList;
    public RecyclerView recyclerViewTopic;
    public AdapterTopics adapterTopics;
    public String myuid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_list);
        recyclerViewTopic = (RecyclerView) findViewById(R.id.recycler_view_topics);
        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext();

        getUserId();
        getAllTopics();
    }

    private void getAllTopics()
    {
        //dbReference = FirebaseDatabase.getInstance().getReference("Topics");
        /*
        List<String> usersid= new ArrayList<String>();
        usersid.add("dcLgBgvDLhNkprfbOHayJXdDQYY2");
        usersid.add(myuid);

        String timeStamp = "" + System.currentTimeMillis();
        Topic topic = new Topic("Topic test", "Topic33 desc",
                "dcLgBgvDLhNkprfbOHayJXdDQYY2","Topic44",timeStamp,timeStamp ,usersid);
        usersid.add("zGzHxWh0ZtTTRliRUC27H0BkjVl2");
       dbReference.child("Topic33").setValue(topic);
         topic = new Topic("Topic44 test", "Topic44 desc",
                "dcLgBgvDLhNkprfbOHayJXdDQYY2","Topic44",timeStamp,timeStamp ,usersid);

        dbReference.child("Topic44").setValue(topic);*/


        topicList = new ArrayList<Topic>();
        dbReference = FirebaseDatabase.getInstance().getReference("Topics");

        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    Topic topic = snapshot1.getValue(Topic.class);
                    if(topic.getUserId().contains(myuid))
                    {
                        topicList.add(topic);
                    }

                }
                adapterTopics = new AdapterTopics(topicList, topicsListActivity.this, R.layout.topics_list,myuid);
                recyclerViewTopic.setAdapter(adapterTopics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void getUserId()
    {
        Intent intent = getIntent();
        myuid = intent.getStringExtra("userId");
    }

}
