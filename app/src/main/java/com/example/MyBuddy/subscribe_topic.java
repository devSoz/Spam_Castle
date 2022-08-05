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

        import com.example.MyBuddy.Model.Model3;
        import com.example.MyBuddy.Model.Topic;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

        import java.util.List;

public class subscribe_topic extends Fragment
{
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public List<Topic> topicList;
    public RecyclerView recyclerViewTopic;
    public AdapterSubscribe adapterTopics;
    public List<Model3> hashMap1;
    public List<Model3> hashMap2;
    public String myuid;
    private Context context;
    Button btnSubscribe;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_topics_subscribe, container, false);
    }

    public static subscribe_topic newInstance(String param1, String param2) {
        subscribe_topic fragment = new subscribe_topic();
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

        recyclerViewTopic = (RecyclerView) view.findViewById(R.id.recycler_view_topiclist);
        recyclerViewTopic.setLayoutManager(new LinearLayoutManager(getContext()));
        context = getContext();
        btnSubscribe =view.findViewById(R.id.btnSubscribe);

        getUserId();
        getAllTopics();
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashMap1=new ArrayList<Model3>();
                hashMap1=adapterTopics.SubscribeTopic();
                String cnt;
                dbReference = FirebaseDatabase.getInstance().getReference("Topics");
                 for(int i=0;i<topicList.size();i++)
                {


                    dbReference.child(hashMap1.get(i).getKeyval()).orderByChild("userId").equalTo(myuid);
                    if(hashMap1.get(i).getValue3().equals("No"))
                    {
                        cnt=String.valueOf( topicList.get(i).getUserId().indexOf(myuid));
                        dbReference.child(hashMap1.get(i).getKeyval()).child("userId").child(cnt).removeValue();

                    }
                    else {
                        if (!(hashMap1.get(i).getValue1().equals(hashMap1.get(i).getValue3()))) {
                            int len=topicList.get(i).getUserId().size()-1;
                            String tuid=String.valueOf(topicList.get(i).getUserId().size());

                             //topicList.get(i).getUserId().get(len).
                          //  cnt=String.valueOf (topicList.get(i).getUserId().size());
                            dbReference.child(hashMap1.get(i).getKeyval()).child("userId").child(tuid).setValue(myuid);
                           // dbReference.child(hashMap1.get(i).getKeyval()).child("userId").setValue(myuid);

                        }
                    }
                    hashMap1.get(i).setValue1(hashMap1.get(i).getValue3() );

            }
            adapterTopics.refreshData();
            Toast.makeText(getActivity(), "Successfully subscribed to selected topics",Toast.LENGTH_SHORT ).show();;

            }
        });
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
        hashMap1=new ArrayList<Model3>();
        dbReference = FirebaseDatabase.getInstance().getReference("Topics");

        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicList.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {

                    Model3 model3 = new Model3();
                    Topic topic = snapshot1.getValue(Topic.class);
                    topicList.add(topic);
                    model3.setKeyval(topic.getTopicId());
                    model3.setValue2( topic.getUserId().size());//count of users
                    model3.setValue1("No");   //if subscribed
                    model3.setValue3("No");
                    if((topic.getUserId().contains(myuid)))
                    {
                         model3.setValue1("Yes");   //if subscribed
                        model3.setValue3("Yes");
                    }
                    hashMap1.add(model3);


                }
                adapterTopics = new AdapterSubscribe(topicList,hashMap1, getContext(), R.layout.activity_subscribe_list,myuid);
                recyclerViewTopic.setAdapter(adapterTopics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d("error","test");
            }
        });
    }

    public void getUserId()
    {
        Intent intent = getActivity().getIntent();
        myuid = intent.getStringExtra("userId");
    }

}
