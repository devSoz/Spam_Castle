package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;

public class topicsListActivity extends Fragment
{
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public ArrayList<Topic> topicList;
    public RecyclerView recyclerViewTopic;
    public AdapterTopics adapterTopics;
    public String myuid;
    private Context context;
    Button btnClick;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_topics_list, container, false);
    }

    public static topicsListActivity newInstance(String param1, String param2) {
        topicsListActivity fragment = new topicsListActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        recyclerViewTopic = (RecyclerView) view.findViewById(R.id.recycler_view_topics);
        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(getContext()));
        context = getContext();

        getUserId();
        getAllTopics();

    }

    private void getAllTopics()
    {
      /*  dbReference = FirebaseDatabase.getInstance().getReference("Topics");
        List<String> usersid= new ArrayList<String>();
        usersid.add("dcLgBgvDLhNkprfbOHayJXdDQYY2");
        usersid.add(myuid);

        String timeStamp = "" + System.currentTimeMillis();
        Topic topic = new Topic("Recipes", "Topic1 desc",
                "","Topic1",timeStamp,timeStamp ,usersid);
        dbReference.child("Topic1").setValue(topic);
         topic = new Topic("Gardending", "Topic2 desc",
                "","Topic2",timeStamp,timeStamp ,usersid);

        dbReference.child("Topic2").setValue(topic);
         topic = new Topic("Chuma Topic", "Topic3 desc",
                "","Topic3",timeStamp,timeStamp ,usersid);

        dbReference.child("Topic3").setValue(topic);
*/
        topicList = new ArrayList<Topic>();
        dbReference = FirebaseDatabase.getInstance().getReference("Topics");

        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicList.clear();
                // Map<String, Topic> td = (HashMap<String,Topic>) snapshot.getValue();

                // ArrayList<Topic> topicList = new ArrayList<>(td.values());
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {


                    Topic topic = snapshot1.getValue(Topic.class);
                    if(topic.getUserId().contains(myuid))
                    {
                        topicList.add(topic);}


                }
                adapterTopics = new AdapterTopics(topicList, getActivity(), R.layout.topics_list,myuid);
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
        Intent intent = getActivity().getIntent();
        myuid = intent.getStringExtra("userId");
    }

}
