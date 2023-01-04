package com.example.androideatit;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androideatit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    //MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*
         binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
         myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
         binding.setData(myViewModel);
         binding.setLifecycleOwner(this);
        */


        binding.buttonSignIn.setOnClickListener(v -> {
           Intent intent = new Intent(MainActivity.this,SignIn.class);
           startActivity(intent);
        });
        binding.buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,SignUp.class);
            startActivity(intent);
        });


    }

}