package com.example.MyBuddy;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UserProfileActivity extends Fragment {

    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference dbReference;
    public List<Topic> topicList;
    public user user1;
    Query userqry;
    public String myuid;
    private Context context;
    private TextView tvusername,tvemailid, tvtopiccount;
    private ImageView tvprofilepic;
    private Button btnSubscribe, btnadd ;
    private ImageButton btnupload;
    private String cameraPermission[];
    private String storagePermission[];
    private Uri imageUri;
    private Boolean flag=false;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static UserProfileActivity newInstance(String param1, String param2) {
        UserProfileActivity fragment = new UserProfileActivity();
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
        tvusername= view.findViewById(R.id.userusername);
        tvemailid=view.findViewById(R.id.useruseremail1);
        tvprofilepic=view.findViewById(R.id.useruserprofilepic);

        btnupload=view.findViewById(R.id.useruserupload);
        btnadd=view.findViewById(R.id.btnadduser);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveUserProfile(imageUri );
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.user_profile);


        return inflater.inflate(R.layout.user_profile, container, false);
    }


    /*@Override
    public void onResume() {
        if(!flag) {
            getAllTopics();
            getUserDetails();
        }
        flag=true;
        super.onResume();
    }*/

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
        Intent intent = getActivity().getIntent();
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


            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });




    }

    private void showImage()
    {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getContext(), "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
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
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void getFromCamera()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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

    private void sendImageMsg(Uri imageuri) throws IOException {

        tvprofilepic.setImageURI(imageuri);
    }

    private void saveUserProfile(Uri imageuri ) throws IOException {
        //  notify = true;
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Saving your details");
        dialog.show();

        final String timestamp = "" + System.currentTimeMillis();
        String filepathandname = "ChatImages/" + "post" + timestamp; // filename
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageuri);
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

                if (uriTask.isSuccessful()) {

                    dbReference = FirebaseDatabase.getInstance().getReference("users");
                    String timeStamp = "" + System.currentTimeMillis();

                    String name = tvusername.getText().toString();


                    String imageUrl = "gs://my-buddy-c3898.appspot.com/ChatImages/post1630077330561";
                    Boolean typing = false;
                    user user = new user(myuid, name, tvemailid.getText().toString(), downloadUri, typing, false);
                    dbReference.child(myuid).setValue(user);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Chumo bad", String.valueOf(e));
            }
        });
        Toast.makeText(getContext(), "Profile details updated",Toast.LENGTH_SHORT ).show();;

    }
    }
