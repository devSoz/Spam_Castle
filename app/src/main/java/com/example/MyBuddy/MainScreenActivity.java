package com.example.MyBuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainScreenActivity extends AppCompatActivity {
    private userfrag fragment;
    private subfrag subfrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentResult = new Result();
        fragmentKeypad = new Keypad();
        setContentView(R.layout.activity_main);
        loadFragmentInstance();
    }

    private void loadFragmentInstance()
    {
      /*  FragmentManager fm = getFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.fragmentKeypad, );
        fragmentTransaction.commit(); // save the changes*/

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentKeypad, new Keypad())
                .add(R.id.fragmentResult, new Result())
                .commit();
    }

}