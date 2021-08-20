package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.MyBuddy.Model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public AdapterMessage adapterMessage;
    public List<Chat> chatList;
    public RecyclerView recyclerViewChat;

    String userId = "zGzHxWh0ZtTTRliRUC27H0BkjVl2";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerViewChat = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));

        messageAlert();
        getChatData();
    }

    public void messageAlert()
    {
        EditText msgText = findViewById(R.id.msgText);
        ImageButton msgSend = findViewById(R.id.sendmsg);
        msgSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String message = String.valueOf(msgText.getText());
                if (message !="") {
                    dbReference = FirebaseDatabase.getInstance().getReference("chat");
                    String timeStamp = "" + System.currentTimeMillis();
                    Chat chat = new Chat(message, "Topic1", "dcLgBgvDLhNkprfbOHayJXdDQYY2", "t", timeStamp);
                    dbReference.child("Topic1").push().setValue(chat);
                    msgText.setText("");

                }

            }
        });
    }

    public void getChatData()
    {
        chatList = new ArrayList<Chat>();
        dbReference = FirebaseDatabase.getInstance().getReference("chat").child(("Topic1"));
        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chatList.add(chat);
                }
                adapterMessage = new AdapterMessage(chatList, userId, MessageActivity.this, "Topic1");
                recyclerViewChat.setAdapter(adapterMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
            });

    }
}

