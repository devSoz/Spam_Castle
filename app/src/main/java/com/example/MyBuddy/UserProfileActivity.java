package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBuddy.Model.Chat;
import com.example.MyBuddy.Model.Topic;
import com.example.MyBuddy.Model.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private ImageButton btnupload;
    private String cameraPermission[];
    private String storagePermission[];
    private Uri imageUri;
    private Boolean flag=false;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;


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
        btnupload=findViewById(R.id.useruserupload);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), subscribe_topic.class);
              intent.putExtra("userId", myuid);
                startActivity(intent);
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                showImage();

            }
        });
    }

    @Override
    protected void onResume() {
        if(!flag) {
            getAllTopics();
            getUserDetails();
        }
        flag=true;
        super.onResume();
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

    private void showImage()
    {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
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
        else
            flag=false;
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
        boolean result = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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

        tvprofilepic.setImageURI(imageuri);
      /*  notify = true;
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
                      }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Chumo bad", String.valueOf(e));
            }
        });*/
    }
}