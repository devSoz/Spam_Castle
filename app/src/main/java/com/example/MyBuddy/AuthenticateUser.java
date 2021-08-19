package com.example.MyBuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyBuddy.Model.user;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class AuthenticateUser extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG="Auth";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference userDbRef;
    private Integer flagNewUser;
    private String uid, name, imageUrl;
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_user);
        txt1=findViewById(R.id.textView1);


        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.seaweed);
        txt1.setTypeface(typeface);

        getUserData();
       // if(uid !="") {
            initSignInButton();
            initGoogleSignInClient();
            firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("users");
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };

        }
   // }

    private void initSignInButton() {
        SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                //.requestIdToken("71669961602-am7bmcfmbtp54tpfpa76bmn1lef1tqb4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void signIn()
    {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.d("Chumo Authenticate user", e.getMessage());
            }
        }
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential)
    {

        firebaseAuth.signInWithCredential(googleAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("test", "signInWithCredential:onComplete:" + task.isSuccessful());
                        userDbRef = firebaseDatabase.getReference("users");
                        //Query userQuery = userDbRef.get

                        if(task.isSuccessful())
                        {
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            flagNewUser = 1;
                           //
                            // Query qry= firebaseDatabase.getReference("users").orderByChild("uid").equalTo("VW4yYHQyi1Wkw6af04dbpAnRI1w2");
                            databaseReference=FirebaseDatabase.getInstance().getReference("users");
                            databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    if ((firebaseUser != null)&&(!snapshot.exists()))
                                    //if ((firebaseUser != null)&&(!snapshot.hasChild(firebaseUser.getUid())))
                                    {
                                        String uid = firebaseUser.getUid();
                                        String name = firebaseUser.getDisplayName();
                                        String email = firebaseUser.getEmail();
                                        String imageUrl = "";
                                        Boolean typing = false;
                                        user user = new user(uid, name, email, imageUrl, typing, isNewUser);
                                        //user.isNew = isNewUser;

                                        databaseReference = firebaseDatabase.getReference("users");
                                        //databaseReference.push().setValue(user);
                                        databaseReference.child(uid).setValue(user);
                                        storeUserData(uid,name,imageUrl);
                                        showToast("Firebase success");
                                    }

                                    else
                                    {
                                        showToast("User already registered");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {

                                }
                            });

                            Intent intent = new Intent(AuthenticateUser.this, MessageActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Log.w("tt", "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            //showToast("Firebase failed");
                        }
                    }
                });

      /*  authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser);
            } else {
                goToMainActivity(authenticatedUser);
            }
        });*/
        showToast("Firebase commented");
    }

    public void storeUserData(String uid, String name, String imageurl)

    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyBuddy",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", name);
        myEdit.putString("uid", uid);
        myEdit.putString("imageUrl", imageurl);
        myEdit.commit();
    }
    void getUserData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyBuddy", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        uid = sharedPreferences.getString("uid", "");
        imageUrl = sharedPreferences.getString("imageUrl", "");


    }
    public void getData()
    {
        flagNewUser = 1;
        databaseReference = firebaseDatabase.getReference("users").child("MhO7JOPKvYvzEyeRzrT");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //for(DataSnapshot snapshot1 : snapshot.getChildren())
                //{
                    //user user1= snapshot1.getValue(user.class);
                    //if(!( user1.name == null))
                       // showToast(user1.name);
                    if(!snapshot.exists())
                    {
                        flagNewUser = 0;
                    }
                //}
                //String t = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void createNewUser(User authenticatedUser) {
       /* authViewModel.createUser(authenticatedUser);
        authViewModel.createdUserLiveData.observe(this, user -> {
            if (user.isCreated) {
                toastMessage(user.name);
            }
            goToMainActivity(user);
        });*/
    }

    private void toastMessage(String name) {
        Toast.makeText(this, "Hi " + name + "!\n" + "Your account was successfully created.", Toast.LENGTH_LONG).show();
    }
/*
    private void goToMainActivity(User user) {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }
  */
    public void showToast(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}