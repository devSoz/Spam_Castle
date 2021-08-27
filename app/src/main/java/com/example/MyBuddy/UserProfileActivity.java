package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MyBuddy.Model.Topic;
import com.example.MyBuddy.Model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public List<Topic> topicList;
    public user user1;
    Query userqry;
    public String myuid;
    private Context context;
    private TextView tvusername,tvemailid, tvtopiccount;
    private ImageView tvprofilepic;
    private Button btnSubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
       tvusername=findViewById(R.id.userusername);
        tvemailid=findViewById(R.id.useruseremail1);
        tvprofilepic=findViewById(R.id.useruserprofilepic);
        tvtopiccount=findViewById(R.id.userusertopic);
        btnSubscribe=findViewById(R.id.userusersubscribe);
        getAllTopics();
        getUserDetails();
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), subscribe_topic.class);


                intent.putExtra("userId", myuid);
                startActivity(intent);
            }
        });

    }

    private void getAllTopics()
    {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void getUserDetails()
    {
        Intent intent = getIntent();
        myuid = intent.getStringExtra("userId");

        userqry = FirebaseDatabase.getInstance().getReference("users").child(myuid);
        userqry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user1 = snapshot.getValue(user.class);
                tvusername.setText(user1.getuserName());
                tvemailid.setText(user1.getEmail());
                Picasso.get()
                        .load(user1.getUserImageUrl())
                        .placeholder(R.color.white)
                        .into(tvprofilepic);
                tvtopiccount.setText(String.valueOf(topicList.size()));

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });




    }
}