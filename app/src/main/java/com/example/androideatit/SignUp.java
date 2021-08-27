package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androideatit.Model.User;
import com.example.androideatit.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);

        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        binding.buttonSignUp.setOnClickListener(v -> {
            ProgressDialog mDialog = new ProgressDialog(SignUp.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();

            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mDialog.dismiss();

                    if (binding.editTextPhoneNoRegister.getText().toString().isEmpty() || binding.editTextNameRegister.getText().toString().isEmpty() || binding.editTextPasswordRegister.getText().toString().isEmpty()){
                        Toast.makeText(SignUp.this, "Please type in correctly.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(snapshot.child(binding.editTextPhoneNoRegister.getText().toString()).exists()){
                            Toast.makeText(SignUp.this, "This phone Number already registered, please use another one.", Toast.LENGTH_SHORT).show();
                        }else{
                            User user = new User(binding.editTextNameRegister.getText().toString(),binding.editTextPasswordRegister.getText().toString());
                            table_user.child(binding.editTextPhoneNoRegister.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "New account create successful, welcome to AndroidEatIt"+" Mr/Ms "+binding.editTextNameRegister.getText().toString()+".", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                            SignUp.this.finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

    }
}