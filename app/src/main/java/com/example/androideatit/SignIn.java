package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.User;
import com.example.androideatit.databinding.ActivitySignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class SignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);

        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        //phoneNo and password
        binding.editTextPhoneNo.setText("0167632147");
        binding.editTextPassword.setText("1111");

        binding.buttonSignIn.setOnClickListener(v -> {

            ProgressDialog mDialog = new ProgressDialog(SignIn.this);
            mDialog.setMessage("Please waiting...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check if user is not exist in database
                    mDialog.dismiss();
                    try {
                        if (snapshot.child(binding.editTextPhoneNo.getText().toString()).exists()){
                            //user account existed
                            mDialog.dismiss();
                            User user = snapshot.child(binding.editTextPhoneNo.getText().toString()).getValue(User.class);

                            if (Objects.requireNonNull(user).getPassword().equals(binding.editTextPassword.getText().toString())){
                                //if user password correct
                                Toast.makeText(SignIn.this, "Sign in successfully.", Toast.LENGTH_SHORT).show();
                                binding.editTextPhoneNo.setText(null);
                                binding.editTextPassword.setText(null);

                                Intent intent = new Intent(SignIn.this,Home.class);
                                Common.User_current = user;
                                startActivity(intent);
                                finish();

                            }else{
                                //if user password incorrect
                                Toast.makeText(SignIn.this, "Password incorrect.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //user account not existed
                            Toast.makeText(SignIn.this, "User does not exist in database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(Exception e) {
                        //user leave blank to login
                        Toast.makeText(SignIn.this, "Please type in correctly.", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }
}