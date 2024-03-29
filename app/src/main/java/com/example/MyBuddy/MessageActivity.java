package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.ByteArrayOutputStream;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBuddy.Model.Chat;
import com.example.MyBuddy.Model.Topic;
import com.example.MyBuddy.Model.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbReference, dbReferenceUser;
    private AdapterMessage adapterMessage;
    private  LinearLayoutManager linearLayoutManager;
    public List<Chat> chatList;
    private List<user> userList;
    private RecyclerView recyclerViewChat;
    private String myuid, mytopic,mytopicUrl="", mytopicName="";
    private String cameraPermission[];
    private String storagePermission[];
    private Boolean notify;
    private Uri imageUri;
    public List<Topic> topicList;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private PopupWindow popup;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        recyclerViewChat = (RecyclerView) findViewById(R.id.recycler_view_chat);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        // initialising permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        getData();
        getTopics();
        messageAlert();
        getUserList();
        getChatData();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
      //  for (int i = 0; i < list.size(); i++) {

        menu.add(0, 1, 0, "Menu Name").setShortcut('5', 'c');
        menu.add(0, 2, 0, "Menu Name").setShortcut('5', 'c');
       // }

        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        int i = 0;
       /* for (Topic t : topicList) {
            menu.add(0, i, 0, t.getTopicName());
            i++;
        }*/
        menu.add(0,1,0,"Subscribed Users");
        menu.add(0,1,0,"Signout");
        return true;
    //    return super.onCreateOptionsMenu(menu);
    }



  /*  @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        int i = 0;
        for (Topic t : topicList) {
            menu.add(0, i, 0, t.getTopicName());
            i++;
        }
     return true;

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId(); //to get the selected menu id

        String menuname = item.getTitle().toString() ; //to get the selected menu name
        switch  (id)
        {
            case 1:
            {
                //show list of users
                showUsers();
                break;
            }
            case 2:
            {
                //signout
                break;
            }

        }
        Toast.makeText(getApplicationContext(),topicList.get(id).getTopicName() ,Toast.LENGTH_LONG).show();
              return super.onOptionsItemSelected(item);

    }

    public void showUsers()
    {
        RelativeLayout rel = (RelativeLayout) findViewById(R.id.mainlay) ;


                    Display display = getWindowManager().getDefaultDisplay();

                    // Load the resolution into a Point object
                    Point size = new Point();

                    display.getSize(size);
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View custview = inflater.inflate(R.layout.popup, null);
                    popup = new PopupWindow(custview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tv = custview.findViewById(R.id.tv);
                    String msg="";
                    for(int i=0;i<userList.size();i++)
                    {

                    }
                    tv.setText(Html.fromHtml("15 mines as per difficulty.</li><br>" ));
                    ImageButton btnclose =  custview.findViewById(R.id.btnclose);
                    btnclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                        }
                    });
        popup.showAtLocation(rel, Gravity.NO_GRAVITY, 10, 10);
        popup.update(50, 50, size.x-100, size.y-100);
                }


    public void getTopics()
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


    public void messageAlert()
    {
        EditText msgText = findViewById(R.id.msgText);
        TextView tvTopicName = findViewById(R.id.tvtopicname);
        CircleImageView tvTopicUrl = findViewById(R.id.imgTopicPic);
        Picasso.get()
                .load(mytopicUrl)
                .placeholder(R.color.white)
                .into(tvTopicUrl);
        ImageButton msgSend = findViewById(R.id.sendmsg);
        ImageButton attachImage = findViewById(R.id.attachbtn);
        tvTopicName.setText(mytopicName);

        msgSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String message = String.valueOf(msgText.getText());
                if (!message.equals("")) {
                    dbReference = FirebaseDatabase.getInstance().getReference("chat");
                    String timeStamp = "" + System.currentTimeMillis();
                    Chat chat = new Chat(message, mytopic, myuid, "t", timeStamp);
                    dbReference.child(mytopic).push().setValue(chat);
                    msgText.setText("");
                }
            }
        });
        attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });
    }

    private void showImage()
    {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    if (!checkPerforCamera()) {
                        reqImagePermission(1);
                    } else {
                        getFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkPerforStorage()) {
                        reqImagePermission(2);
                    } else {
                        getFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

      //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         // request for permission if not given
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0)
                {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        getFromCamera(); // if access granted then click
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        getFromGallery(); // if access granted then pick
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                imageUri = data.getData(); // get image data to upload
                try {
                     sendImageMsg(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST)
            {
                try {
                    sendImageMsg(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private Boolean checkPerforCamera() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void getFromCamera()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }


    private void getFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }
    private Boolean checkPerforStorage() {
        boolean result = ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void reqImagePermission(Integer choice) {
        if(choice==1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(cameraPermission, CAMERA_REQUEST);
            }
        }
        else if(choice==2)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(storagePermission, STORAGE_REQUEST);
            }
    }

    private void sendImageMsg(Uri imageuri) throws IOException
    {
        notify = true;
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Sending Image");
        dialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        String filepathandname = "ChatImages/" + "post" + timestamp; // filename
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream); // compressing the image using bitmap
        final byte[] data = arrayOutputStream.toByteArray();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathandname);
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                String downloadUri = uriTask.getResult().toString(); // getting url if task is successful

                if (uriTask.isSuccessful())
                {
                    DatabaseReference re = FirebaseDatabase.getInstance().getReference();

                    String message = downloadUri;
                    dbReference = FirebaseDatabase.getInstance().getReference("chat");
                    String timeStamp = "" + System.currentTimeMillis();
                    Chat chat = new Chat(message, mytopic, myuid, "i", timeStamp);
                    dbReference.child(mytopic).push().setValue(chat);

                    /*final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("ChatList").child(uid).child(myuid);
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                ref1.child("id").setValue(myuid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ChatList").child(myuid).child(uid);
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                ref2.child("id").setValue(uid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Chumo bad", String.valueOf(e));
            }
        });
    }

    public void getChatData()
    {
        chatList = new ArrayList<Chat>();
        dbReference = FirebaseDatabase.getInstance().getReference("chat").child((mytopic));
        dbReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chatList.add(chat);
                }

                adapterMessage = new AdapterMessage(userList, chatList, myuid, MessageActivity.this, mytopic);
                recyclerViewChat.setAdapter(adapterMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
            });

    }
    public void getData()
    {
        Intent intent = getIntent();
        myuid = intent.getStringExtra("userId");
        mytopic = intent.getStringExtra("topicId");
        mytopicUrl = intent.getStringExtra("topicUrl");
        mytopicName = intent.getStringExtra("topicName");
    }

    public void getUserList()
    {
        userList = new ArrayList<user>();
        dbReferenceUser = FirebaseDatabase.getInstance().getReference("users");

        dbReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    user user1 = snapshot1.getValue(user.class);
                    userList.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

